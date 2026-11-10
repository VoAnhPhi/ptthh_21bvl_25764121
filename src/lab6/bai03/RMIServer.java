package lab6.bai03;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            PrimeService service = new PrimeServiceImpl();

            Registry registry =
                    LocateRegistry.createRegistry(1101);

            registry.rebind("PrimeService", service);

            System.out.println("Server Bai 3 dang chay...");
        } catch (Exception e) {
            System.out.println("Loi Server: " + e.getMessage());
        }
    }
}
