package lab8.bai03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Bai 3: Giao dich (transaction) - chuyen so luong ton kho tu san pham nay sang san pham khac.
 * Hai lenh UPDATE phai cung thanh cong (commit) hoac cung bi huy (rollback).
 */
public class TransactionDemo {

    private static final String URL = "jdbc:sqlite:transaction.db";

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS SanPham");
            stmt.executeUpdate("CREATE TABLE SanPham (maSP INTEGER PRIMARY KEY, tenSP TEXT NOT NULL, "
                    + "gia REAL, soluong INTEGER NOT NULL CHECK (soluong >= 0))");
            stmt.executeUpdate("INSERT INTO SanPham VALUES (1, 'Kho A - Laptop', 15000000, 10)");
            stmt.executeUpdate("INSERT INTO SanPham VALUES (2, 'Kho B - Laptop', 15000000, 5)");
        }

        System.out.println("=== TRUOC KHI CHUYEN ===");
        printStock();

        System.out.println("\n--- Chuyen 4 san pham tu SP1 sang SP2 (hop le) ---");
        transfer(1, 2, 4);
        printStock();

        System.out.println("\n--- Chuyen 20 san pham tu SP1 sang SP2 (vuot ton kho, phai rollback) ---");
        transfer(1, 2, 20);
        printStock();
    }

    static void transfer(int from, int to, int qty) {
        Connection conn = null;
        PreparedStatement minus = null;
        PreparedStatement plus = null;
        try {
            conn = DriverManager.getConnection(URL);
            conn.setAutoCommit(false); // tat auto-commit, bat dau giao dich

            minus = conn.prepareStatement("UPDATE SanPham SET soluong = soluong - ? WHERE maSP = ?");
            minus.setInt(1, qty);
            minus.setInt(2, from);
            minus.executeUpdate(); // CHECK (soluong >= 0) se nem SQLException neu am

            plus = conn.prepareStatement("UPDATE SanPham SET soluong = soluong + ? WHERE maSP = ?");
            plus.setInt(1, qty);
            plus.setInt(2, to);
            plus.executeUpdate();

            conn.commit();
            System.out.println("COMMIT: chuyen " + qty + " san pham thanh cong.");
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("ROLLBACK: da huy toan bo thay doi.");
                }
            } catch (SQLException ex) {
                System.out.println("Loi rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (minus != null) minus.close();
                if (plus != null) plus.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Loi khi dong ket noi: " + e.getMessage());
            }
        }
    }

    static void printStock() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT maSP, tenSP, soluong FROM SanPham ORDER BY maSP")) {
            while (rs.next()) {
                System.out.printf("SP%d | %-16s | soluong = %d%n",
                        rs.getInt("maSP"), rs.getString("tenSP"), rs.getInt("soluong"));
            }
        }
    }
}
