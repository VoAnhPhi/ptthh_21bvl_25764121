package lab8.bai05;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Bai 5: PreparedStatement - tham so hoa cau lenh SQL, chong SQL Injection va tai su dung cau lenh.
 */
public class PreparedStatementDemo {

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:prepared.db")) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DROP TABLE IF EXISTS SanPham");
                stmt.executeUpdate("CREATE TABLE SanPham (maSP INTEGER PRIMARY KEY, "
                        + "tenSP TEXT NOT NULL, gia REAL, soluong INTEGER)");
            }

            // 1. INSERT bang PreparedStatement + batch (cau lenh duoc bien dich 1 lan, dung nhieu lan)
            String sqlInsert = "INSERT INTO SanPham(maSP, tenSP, gia, soluong) VALUES (?, ?, ?, ?)";
            Object[][] data = {
                {1, "Laptop Dell", 15000000.0, 10},
                {2, "Chuot Logitech", 250000.0, 50},
                {3, "Ban phim co", 900000.0, 30},
                {4, "Man hinh LG", 3500000.0, 15}
            };
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                for (Object[] row : data) {
                    ps.setInt(1, (Integer) row[0]);
                    ps.setString(2, (String) row[1]);
                    ps.setDouble(3, (Double) row[2]);
                    ps.setInt(4, (Integer) row[3]);
                    ps.addBatch();
                }
                int[] counts = ps.executeBatch();
                conn.commit();
                System.out.println("Da them " + counts.length + " san pham bang PreparedStatement (batch).");
            }
            conn.setAutoCommit(true);

            // 2. UPDATE
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE SanPham SET gia = ?, soluong = ? WHERE maSP = ?")) {
                ps.setDouble(1, 14500000.0);
                ps.setInt(2, 8);
                ps.setInt(3, 1);
                System.out.println("UPDATE maSP=1: " + ps.executeUpdate() + " dong duoc cap nhat.");
            }

            // 3. SELECT co tham so
            System.out.println("\nTim san pham co ten chua 'o':");
            search(conn, "o");

            // 4. Demo SQL Injection
            String attack = "zzz' OR '1'='1";
            System.out.println("\nChuoi tan cong: " + attack);

            System.out.println("\n[Statement - noi chuoi] -> bi SQL Injection:");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT * FROM SanPham WHERE tenSP = '" + attack + "'")) {
                int n = 0;
                while (rs.next()) {
                    n++;
                    System.out.println("  Lo du lieu: " + rs.getString("tenSP"));
                }
                System.out.println("  => tra ve " + n + " dong (sai, dang le phai la 0).");
            }

            System.out.println("\n[PreparedStatement - tham so ?] -> an toan:");
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM SanPham WHERE tenSP = ?")) {
                ps.setString(1, attack);
                try (ResultSet rs = ps.executeQuery()) {
                    int n = 0;
                    while (rs.next()) n++;
                    System.out.println("  => tra ve " + n + " dong (dung).");
                }
            }
        }
    }

    static void search(Connection conn, String keyword) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT maSP, tenSP, gia, soluong FROM SanPham WHERE tenSP LIKE ?")) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("  %d | %-16s | %.0f | %d%n", rs.getInt("maSP"),
                            rs.getString("tenSP"), rs.getDouble("gia"), rs.getInt("soluong"));
                }
            }
        }
    }
}
