package lab6.bai09;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class AuctionServiceImpl extends UnicastRemoteObject implements AuctionService {
    private final Object productsLock = new Object();
    private final Map<String, AuctionProduct> products = new LinkedHashMap<>();
    private final Set<AuctionListener> listeners = new CopyOnWriteArraySet<>();

    public AuctionServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean addProduct(String seller, String productName, double startingPrice)
            throws RemoteException {
        if (isBlank(seller) || isBlank(productName)
                || !Double.isFinite(startingPrice) || startingPrice < 0) {
            return false;
        }

        String displayName = productName.trim();
        String key = normalize(displayName);
        synchronized (productsLock) {
            if (products.containsKey(key)) {
                return false;
            }
            products.put(key, new AuctionProduct(seller.trim(), displayName, startingPrice));
        }
        return true;
    }

    @Override
    public String[] listProducts() throws RemoteException {
        synchronized (productsLock) {
            String[] result = new String[products.size()];
            int index = 0;
            for (AuctionProduct product : products.values()) {
                result[index++] = product.toDisplayString();
            }
            return result;
        }
    }

    @Override
    public boolean placeBid(String bidder, String productName, double amount)
            throws RemoteException {
        if (isBlank(bidder) || isBlank(productName) || !Double.isFinite(amount)) {
            return false;
        }

        AuctionProduct product;
        String bidderName = bidder.trim();
        synchronized (productsLock) {
            product = products.get(normalize(productName));
            if (product == null || amount <= product.highestBid) {
                return false;
            }
            product.highestBid = amount;
            product.highestBidder = bidderName;
        }

        for (AuctionListener listener : listeners) {
            try {
                listener.onBidUpdated(product.name, bidderName, amount);
            } catch (RemoteException e) {
                listeners.remove(listener);
            }
        }
        return true;
    }

    @Override
    public void registerListener(AuctionListener listener) throws RemoteException {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public void unregisterListener(AuctionListener listener) throws RemoteException {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT);
    }

    private static class AuctionProduct {
        private final String seller;
        private final String name;
        private double highestBid;
        private String highestBidder;

        private AuctionProduct(String seller, String name, double startingPrice) {
            this.seller = seller;
            this.name = name;
            this.highestBid = startingPrice;
        }

        private String toDisplayString() {
            String bidder = highestBidder == null ? "Chua co" : highestBidder;
            return String.format(Locale.US,
                    "%s | Chu ban: %s | Gia cao nhat: %.2f | Nguoi tra cao nhat: %s",
                    name, seller, highestBid, bidder);
        }
    }
}
