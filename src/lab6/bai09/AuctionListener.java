package lab6.bai09;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuctionListener extends Remote {
    void onBidUpdated(String productName, String bidder, double amount)
            throws RemoteException;
}
