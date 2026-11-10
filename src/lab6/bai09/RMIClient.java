package lab6.bai09;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RMIClient {
    public static void main(String[] args) {
        AuctionService service = null;
        AuctionListenerImpl listener = null;

        try (Scanner scanner = new Scanner(System.in)) {
            Registry registry = LocateRegistry.getRegistry("localhost", 1107);
            service = (AuctionService) registry.lookup("AuctionService");
            listener = new AuctionListenerImpl();
            service.registerListener(listener);

            System.out.println("Da ket noi den Auction Server. Ban se nhan thong bao khi co gia moi.");
            while (true) {
                System.out.println("\n===== DAU GIA TRUC TUYEN =====");
                System.out.println("1. Them san pham (Seller)");
                System.out.println("2. Xem danh sach san pham");
                System.out.println("3. Dat gia (Bidder)");
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
                    System.out.print("Ten nguoi ban: ");
                    String seller = scanner.nextLine().trim();
                    System.out.print("Ten san pham: ");
                    String productName = scanner.nextLine().trim();
                    double startingPrice = readAmount(scanner, "Gia khoi diem: ");

                    if (!Double.isFinite(startingPrice) || startingPrice < 0) {
                        System.out.println("Gia khoi diem phai la so khong am hop le.");
                    } else if (service.addProduct(seller, productName, startingPrice)) {
                        System.out.println("Them san pham thanh cong.");
                    } else {
                        System.out.println("Khong them duoc. Hay kiem tra ten va gia, san pham co the da ton tai.");
                    }
                } else if (choice == 2) {
                    String[] products = service.listProducts();
                    if (products.length == 0) {
                        System.out.println("Chua co san pham dau gia.");
                    } else {
                        for (String product : products) {
                            System.out.println(product);
                        }
                    }
                } else if (choice == 3) {
                    System.out.print("Ten nguoi dau gia: ");
                    String bidder = scanner.nextLine().trim();
                    System.out.print("Ten san pham: ");
                    String productName = scanner.nextLine().trim();
                    double amount = readAmount(scanner, "Gia muon dat: ");

                    if (!Double.isFinite(amount) || amount < 0) {
                        System.out.println("Gia dat phai la so khong am hop le.");
                    } else if (service.placeBid(bidder, productName, amount)) {
                        System.out.println("Dat gia thanh cong.");
                    } else {
                        System.out.println("Dat gia that bai. San pham khong ton tai hoac gia chua cao hon gia hien tai.");
                    }
                } else {
                    System.out.println("Chuc nang khong ton tai!");
                }
            }
        } catch (Exception e) {
            System.out.println("Loi RMI: " + e.getMessage());
        } finally {
            if (service != null && listener != null) {
                try {
                    service.unregisterListener(listener);
                } catch (Exception ignored) {
                    // Server may already be unavailable while the client exits.
                }
            }
            if (listener != null) {
                try {
                    UnicastRemoteObject.unexportObject(listener, true);
                } catch (Exception ignored) {
                    // The listener may already have been unexported.
                }
            }
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
}
