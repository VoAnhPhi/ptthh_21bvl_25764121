package lab6.bai09;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuctionService extends Remote {
    boolean addProduct(String seller, String productName, double startingPrice)
            throws RemoteException;

    String[] listProducts() throws RemoteException;

    boolean placeBid(String bidder, String productName, double amount)
            throws RemoteException;

    void registerListener(AuctionListener listener) throws RemoteException;

    void unregisterListener(AuctionListener listener) throws RemoteException;
}
