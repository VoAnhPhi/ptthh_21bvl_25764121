package lab8.bai02;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Bai 2: Quan ly danh muc san pham SanPham(maSP, tenSP, gia, soluong) - day du CRUD.
 */
public class SanPhamCRUD {

    private static final String URL = "jdbc:sqlite:sanpham.db";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS SanPham ("
                    + "maSP INTEGER PRIMARY KEY, "
                    + "tenSP TEXT NOT NULL, "
                    + "gia REAL NOT NULL, "
                    + "soluong INTEGER NOT NULL)");

            // Create: them it nhat 3 san pham (chi them khi bang con trong)
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM SanPham")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.executeUpdate("INSERT INTO SanPham VALUES (1, 'Laptop Dell', 15000000, 10)");
                    stmt.executeUpdate("INSERT INTO SanPham VALUES (2, 'Chuot Logitech', 250000, 50)");
                    stmt.executeUpdate("INSERT INTO SanPham VALUES (3, 'Ban phim co', 900000, 30)");
                    System.out.println("Da them 3 san pham mac dinh.");
                }
            }

            while (true) {
                System.out.println("\n===== QUAN LY SAN PHAM =====");
                System.out.println("1. Liet ke tat ca");
                System.out.println("2. Tim theo ten");
                System.out.println("3. Them san pham");
                System.out.println("4. Cap nhat gia / so luong");
                System.out.println("5. Xoa san pham");
                System.out.println("0. Thoat");
                System.out.print("Chon: ");

                String choice = sc.nextLine().trim();
                if (choice.equals("0")) {
                    System.out.println("Ket thuc chuong trinh.");
                    break;
                }

                try {
                    switch (choice) {
                        case "1" -> printProducts(stmt.executeQuery(
                                "SELECT * FROM SanPham ORDER BY maSP"));
                        case "2" -> {
                            System.out.print("Nhap ten can tim: ");
                            String keyword = sc.nextLine().trim();
                            printProducts(stmt.executeQuery(
                                    "SELECT * FROM SanPham WHERE tenSP LIKE '%" + keyword + "%'"));
                        }
                        case "3" -> {
                            System.out.print("Ma SP: ");
                            int ma = Integer.parseInt(sc.nextLine().trim());
                            System.out.print("Ten SP: ");
                            String ten = sc.nextLine().trim();
                            System.out.print("Gia: ");
                            double gia = Double.parseDouble(sc.nextLine().trim());
                            System.out.print("So luong: ");
                            int sl = Integer.parseInt(sc.nextLine().trim());
                            stmt.executeUpdate("INSERT INTO SanPham VALUES (" + ma + ", '"
                                    + ten + "', " + gia + ", " + sl + ")");
                            System.out.println("Them thanh cong!");
                        }
                        case "4" -> {
                            System.out.print("Ma SP can cap nhat: ");
                            int ma = Integer.parseInt(sc.nextLine().trim());
                            System.out.print("Gia moi: ");
                            double gia = Double.parseDouble(sc.nextLine().trim());
                            System.out.print("So luong moi: ");
                            int sl = Integer.parseInt(sc.nextLine().trim());
                            int n = stmt.executeUpdate("UPDATE SanPham SET gia = " + gia
                                    + ", soluong = " + sl + " WHERE maSP = " + ma);
                            System.out.println(n > 0 ? "Cap nhat thanh cong!" : "Khong tim thay maSP = " + ma);
                        }
                        case "5" -> {
                            System.out.print("Ma SP can xoa: ");
                            int ma = Integer.parseInt(sc.nextLine().trim());
                            int n = stmt.executeUpdate("DELETE FROM SanPham WHERE maSP = " + ma);
                            System.out.println(n > 0 ? "Xoa thanh cong!" : "Khong tim thay maSP = " + ma);
                        }
                        default -> System.out.println("Chuc nang khong ton tai!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Vui long nhap so hop le!");
                } catch (SQLException e) {
                    System.out.println("Loi SQL: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Loi ket noi: " + e.getMessage());
        }
    }

    private static void printProducts(ResultSet rs) throws SQLException {
        try (rs) {
            System.out.printf("%-6s %-20s %-14s %s%n", "maSP", "tenSP", "gia", "soluong");
            boolean empty = true;
            while (rs.next()) {
                empty = false;
                System.out.printf("%-6d %-20s %-14.0f %d%n", rs.getInt("maSP"),
                        rs.getString("tenSP"), rs.getDouble("gia"), rs.getInt("soluong"));
            }
            if (empty) System.out.println("(khong co san pham nao)");
        }
    }
}
