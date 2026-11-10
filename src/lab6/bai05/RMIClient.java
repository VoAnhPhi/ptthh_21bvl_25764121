
package lab6.bai05;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RMIClient {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		try {
			Registry registry = LocateRegistry.getRegistry("localhost", 1103);

			ChatService service = (ChatService) registry.lookup("ChatService");

			System.out.println("Da ket noi den Server!");
			System.out.println("Nhap /exit de thoat.");

			while (true) {
				System.out.print("\nClient: ");
				String message = sc.nextLine();

				if (message.equalsIgnoreCase("/exit")) {
					System.out.println("Ket thuc chat.");
					break;
				}

				if (message.trim().isEmpty()) {
					System.out.println("Tin nhan khong duoc rong!");
					continue;
				}

				service.sendMessage(message);

				String reply = service.getReply();

				System.out.println("Server: " + reply);
			}

		} catch (Exception e) {
			System.out.println("Loi RMI: " + e.getMessage());
		}
	}
}
