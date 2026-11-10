package lab6.bai02;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
	public static void main(String[] args) {
		try {
			AddService service = new AddServiceImpl();
			Registry registry =
                    LocateRegistry.createRegistry(1100);

            registry.rebind("AddService", service);
			System.out.println("Server dang chay...");
		} catch (Exception e) {
			System.out.println("Server loi: " + e.getMessage());
		} 
	}
}
