package lab6.bai09;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            AuctionService service = new AuctionServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1107);
            registry.rebind("AuctionService", service);

            System.out.println("Auction server dang chay tren cong 1107...");
        } catch (Exception e) {
            System.out.println("Loi Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
