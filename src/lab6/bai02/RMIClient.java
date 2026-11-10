
package lab6.bai02;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RMIClient {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            Registry registry =
                    LocateRegistry.getRegistry("localhost", 1100);
            AddService service =
                    (AddService) registry.lookup("AddService");

            System.out.print("Nhap so a: ");
            int a = sc.nextInt();

            System.out.print("Nhap so b: ");
            int b = sc.nextInt();

            int result = service.add(a, b);

            System.out.println("Tong = " + result);
			sc.close();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Loi: Vui long nhap so nguyen!");
        } catch (Exception e) {
            System.out.println("Loi ket noi RMI: " + e.getMessage());
        }
    }
}
