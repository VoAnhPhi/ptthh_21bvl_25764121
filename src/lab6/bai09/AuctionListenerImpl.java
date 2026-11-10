package lab6.bai09;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Locale;

public class AuctionListenerImpl extends UnicastRemoteObject implements AuctionListener {
    public AuctionListenerImpl() throws RemoteException {
        super();
    }

    @Override
    public void onBidUpdated(String productName, String bidder, double amount)
            throws RemoteException {
        System.out.printf(Locale.US,
                "%n[THONG BAO] %s vua dat gia %.2f cho san pham \"%s\".%n",
                bidder, amount, productName);
    }
}
