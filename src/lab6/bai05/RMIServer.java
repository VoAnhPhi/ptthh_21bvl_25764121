
package lab6.bai05;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {

    public static void main(String[] args) {
        try {
            ChatService service = new ChatServiceImpl();

            Registry registry =
                    LocateRegistry.createRegistry(1103);

            registry.rebind("ChatService", service);

            System.out.println("Server Chat dang chay...");
            System.out.println("Dang cho Client ket noi...");

        } catch (Exception e) {
            System.out.println("Loi Server: " + e.getMessage());
        }
    }
}
