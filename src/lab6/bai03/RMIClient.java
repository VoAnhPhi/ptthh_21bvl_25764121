package lab6.bai03;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RMIClient {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Registry registry =
                    LocateRegistry.getRegistry("localhost", 1101);

            PrimeService service =
                    (PrimeService) registry.lookup("PrimeService");

            System.out.print("Nhap so nguyen n: ");
            int n = sc.nextInt();

            boolean result = service.isPrime(n);

            if (result) {
                System.out.println(n + " la so nguyen to.");
            } else {
                System.out.println(n + " khong phai so nguyen to.");
            }

			sc.close();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Loi: Vui long nhap so nguyen!");
        } catch (Exception e) {
            System.out.println("Loi RMI: " + e.getMessage());
        }
    }
}
