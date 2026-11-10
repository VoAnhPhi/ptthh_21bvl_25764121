package lab6.bai06;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            BankService service = new BankServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1104);
            registry.rebind("BankService", service);

            System.out.println("Bank server dang chay tren cong 1104...");
            System.out.println("So du ban dau: 1,000,000 VND");
        } catch (Exception e) {
            System.out.println("Loi Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
