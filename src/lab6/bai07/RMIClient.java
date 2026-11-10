package lab6.bai07;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RMIClient {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Registry registry = LocateRegistry.getRegistry("localhost", 1105);
            GeometryService service = (GeometryService) registry.lookup("GeometryService");

            while (true) {
                System.out.println("\n===== TINH DIEN TICH =====");
                System.out.println("1. Hinh chu nhat");
                System.out.println("2. Hinh tron");
                System.out.println("3. Hinh tam giac");
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
                    double width = readPositiveValue(scanner, "Nhap chieu rong: ");
                    double height = readPositiveValue(scanner, "Nhap chieu cao: ");
                    if (!isPositiveFinite(width) || !isPositiveFinite(height)) {
                        System.out.println("Kich thuoc phai la so duong hop le.");
                        continue;
                    }
                    printArea(service.rectangleArea(width, height));
                } else if (choice == 2) {
                    double radius = readPositiveValue(scanner, "Nhap ban kinh: ");
                    if (!isPositiveFinite(radius)) {
                        System.out.println("Ban kinh phai la so duong hop le.");
                        continue;
                    }
                    printArea(service.circleArea(radius));
                } else if (choice == 3) {
                    double base = readPositiveValue(scanner, "Nhap canh day: ");
                    double height = readPositiveValue(scanner, "Nhap chieu cao: ");
                    if (!isPositiveFinite(base) || !isPositiveFinite(height)) {
                        System.out.println("Kich thuoc phai la so duong hop le.");
                        continue;
                    }
                    printArea(service.triangleArea(base, height));
                } else {
                    System.out.println("Chuc nang khong ton tai!");
                }
            }
        } catch (Exception e) {
            System.out.println("Loi RMI: " + e.getMessage());
        }
    }

    private static double readPositiveValue(Scanner scanner, String prompt) {
        System.out.print(prompt);
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    private static boolean isPositiveFinite(double value) {
        return Double.isFinite(value) && value > 0;
    }

    private static void printArea(double area) {
        System.out.printf("Dien tich = %.2f%n", area);
    }
}
