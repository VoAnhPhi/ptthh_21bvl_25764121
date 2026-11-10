package lab6.bai07;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            GeometryService service = new GeometryServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1105);
            registry.rebind("GeometryService", service);

            System.out.println("Geometry server dang chay tren cong 1105...");
        } catch (Exception e) {
            System.out.println("Loi Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
