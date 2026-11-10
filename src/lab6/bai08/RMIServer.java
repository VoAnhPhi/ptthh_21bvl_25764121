package lab6.bai08;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            FlightBookingService service = new FlightBookingServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1106);
            registry.rebind("FlightBookingService", service);

            System.out.println("Flight server dang chay tren cong 1106...");
            System.out.println("Chuyen bay: VN101 (180 ghe), VN202 (120 ghe), VN303 (90 ghe)");
        } catch (Exception e) {
            System.out.println("Loi Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
