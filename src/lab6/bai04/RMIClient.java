
package lab6.bai04;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RMIClient {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		try {
			Registry registry = LocateRegistry.getRegistry("localhost", 1102);

			ContactService service = (ContactService) registry.lookup("ContactService");

			while (true) {
				System.out.println("\n===== QUAN LY DANH BA =====");
				System.out.println("1. Them lien he");
				System.out.println("2. Sua lien he");
				System.out.println("3. Tim lien he");
				System.out.println("4. Xoa lien he");
				System.out.println("0. Thoat");
				System.out.print("Chon: ");

				int choice;

				try {
					choice = Integer.parseInt(sc.nextLine().trim());
				} catch (NumberFormatException e) {
					System.out.println("Vui long nhap so hop le!");
					continue;
				}

				if (choice == 0) {
					System.out.println("Ket thuc chuong trinh.");
					break;
				}

				if (choice < 1 || choice > 4) {
					System.out.println("Chuc nang khong ton tai!");
					continue;
				}

				System.out.print("Nhap ten: ");
				String name = sc.nextLine().trim();

				if (name.isEmpty()) {
					System.out.println("Ten khong duoc de trong!");
					continue;
				}

				switch (choice) {
					case 1:
						if (service.findContact(name) != null) {
							System.out.println("Lien he da ton tai!");
							break;
						}

						System.out.print("Nhap so dien thoai: ");
						String phone = sc.nextLine().trim();

						if (phone.isEmpty()) {
							System.out.println("So dien thoai khong duoc rong!");
							break;
						}

						service.addContact(name, phone);
						System.out.println("Them thanh cong!");
						break;

					case 2:
						if (service.findContact(name) == null) {
							System.out.println("Khong tim thay lien he!");
							break;
						}

						System.out.print("Nhap so dien thoai moi: ");
						String newPhone = sc.nextLine().trim();

						if (newPhone.isEmpty()) {
							System.out.println("So dien thoai khong duoc rong!");
							break;
						}

						service.addContact(name, newPhone);
						System.out.println("Cap nhat thanh cong!");
						break;

					case 3:
						String result = service.findContact(name);

						if (result == null) {
							System.out.println("Khong tim thay lien he!");
						} else {
							System.out.println("So dien thoai: " + result);
						}
						break;

					case 4:
						boolean deleted = service.deleteContact(name);

						if (deleted) {
							System.out.println("Xoa thanh cong!");
						} else {
							System.out.println("Khong tim thay lien he!");
						}
						break;
				}
			}

		} catch (Exception e) {
			System.out.println("Loi RMI: " + e.getMessage());
		}
	}
}
