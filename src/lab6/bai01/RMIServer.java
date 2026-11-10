
package lab6.bai01;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {

	public static void main(String[] args) {
		try {
			// Tao RMI Registry tren cong 1099
			Registry registry = LocateRegistry.createRegistry(1099);

			// Tao doi tuong dich vu
			HelloService service = new HelloServiceImpl();

			// Dang ky dich vu
			registry.rebind("HelloService", service);

			System.out.println("RMI Server dang chay...");
			System.out.println("Dang cho Client ket noi...");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
