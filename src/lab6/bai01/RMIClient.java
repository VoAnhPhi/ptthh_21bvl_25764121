
package lab6.bai01;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {

	public static void main(String[] args) {
		try {
			// Ket noi toi Registry tren Server
			Registry registry = LocateRegistry.getRegistry("localhost", 1099);

			// Tim dich vu
			HelloService service = (HelloService) registry.lookup("HelloService");

			// Goi phuong thuc tu xa
			String result = service.sayHello();

			// Hien thi ket qua
			System.out.println("Ket qua tu Server: " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
