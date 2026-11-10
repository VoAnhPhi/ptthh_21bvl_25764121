package lab6.bai06;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RMIClient {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Registry registry = LocateRegistry.getRegistry("localhost", 1104);
            BankService service = (BankService) registry.lookup("BankService");

            while (true) {
                System.out.println("\n===== QUAN LY TAI KHOAN =====");
                System.out.println("1. Xem so du");
                System.out.println("2. Gui tien");
                System.out.println("3. Rut tien");
                System.out.println("0. Thoat");
                System.out.print("Chon: ");

                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Vui long nhap so hop le!");
                    continue;
                }

                if (choice == 0) {
                    break;
                } else if (choice == 1) {
                    printBalance(service.getBalance());
                } else if (choice == 2) {
                    double amount = readAmount(scanner, "Nhap so tien can gui: ");
                    if (!isPositiveAmount(amount)) {
                        System.out.println("So tien phai lon hon 0.");
                        continue;
                    }
                    service.deposit(amount);
                    System.out.println("Gui tien thanh cong.");
                    printBalance(service.getBalance());
                } else if (choice == 3) {
                    double amount = readAmount(scanner, "Nhap so tien can rut: ");
                    if (!isPositiveAmount(amount)) {
                        System.out.println("So tien phai lon hon 0.");
                        continue;
                    }
                    if (service.withdraw(amount)) {
                        System.out.println("Rut tien thanh cong.");
                    } else {
                        System.out.println("Rut tien that bai: so du khong du.");
                    }
                    printBalance(service.getBalance());
                } else {
                    System.out.println("Chuc nang khong ton tai!");
                }
            }
        } catch (Exception e) {
            System.out.println("Loi RMI: " + e.getMessage());
        }
    }

    private static double readAmount(Scanner scanner, String prompt) {
        System.out.print(prompt);
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    private static boolean isPositiveAmount(double amount) {
        return Double.isFinite(amount) && amount > 0;
    }

    private static void printBalance(double balance) {
        System.out.printf("So du hien tai: %,.2f VND%n", balance);
    }
}
