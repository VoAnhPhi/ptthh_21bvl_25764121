package lab6.bai08;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RMIClient {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Registry registry = LocateRegistry.getRegistry("localhost", 1106);
            FlightBookingService service =
                    (FlightBookingService) registry.lookup("FlightBookingService");

            while (true) {
                System.out.println("\n===== DAT VE MAY BAY =====");
                System.out.println("Chuyen bay: VN101, VN202, VN303");
                System.out.println("1. Kiem tra so ghe trong");
                System.out.println("2. Dat ve");
                System.out.println("3. Huy ve");
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
                } else if (choice >= 1 && choice <= 3) {
                    System.out.print("Nhap ma chuyen bay: ");
                    String flightNumber = scanner.nextLine().trim();

                    if (choice == 1) {
                        int seats = service.availableSeats(flightNumber);
                        if (seats < 0) {
                            System.out.println("Khong tim thay chuyen bay.");
                        } else {
                            System.out.println("So ghe con trong: " + seats);
                        }
                    } else {
                        int count = readSeatCount(scanner);
                        if (count <= 0) {
                            System.out.println("So ghe phai la so nguyen duong.");
                            continue;
                        }

                        boolean success = choice == 2
                                ? service.bookTicket(flightNumber, count)
                                : service.cancelBooking(flightNumber, count);

                        if (success) {
                            System.out.println(choice == 2 ? "Dat ve thanh cong." : "Huy ve thanh cong.");
                            int remaining = service.availableSeats(flightNumber);
                            System.out.println("So ghe con trong: " + remaining);
                        } else {
                            System.out.println("Khong the thuc hien. Kiem tra ma chuyen bay, so ghe va so ve da dat.");
                        }
                    }
                } else {
                    System.out.println("Chuc nang khong ton tai!");
                }
            }
        } catch (Exception e) {
            System.out.println("Loi RMI: " + e.getMessage());
        }
    }

    private static int readSeatCount(Scanner scanner) {
        System.out.print("Nhap so ghe: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
