
package lab6.bai04;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {

    public static void main(String[] args) {
        try {
            ContactService service = new ContactServiceImpl();

            Registry registry =
                    LocateRegistry.createRegistry(1102);

            registry.rebind("ContactService", service);

            System.out.println("Server Bai 4 dang chay...");

        } catch (Exception e) {
            System.out.println("Loi Server: " + e.getMessage());
        }
    }
}
