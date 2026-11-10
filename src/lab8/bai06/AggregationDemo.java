package lab8.bai06;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Bai 6: Truy van nang cao - ham tong hop SUM, AVG, MIN, MAX, COUNT + GROUP BY tren bang NhanVien(luong).
 */
public class AggregationDemo {

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:aggregation.db");
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("DROP TABLE IF EXISTS NhanVien");
            stmt.executeUpdate("CREATE TABLE NhanVien (id INTEGER PRIMARY KEY, ten TEXT NOT NULL, "
                    + "chuc_vu TEXT, luong REAL)");

            Object[][] data = {
                {1, "Nguyen Van An", "Giam doc", 40000000.0},
                {2, "Tran Thi Binh", "Ke toan", 15000000.0},
                {3, "Le Van Cuong", "Lap trinh vien", 20000000.0},
                {4, "Pham Thi Dung", "Lap trinh vien", 18000000.0},
                {5, "Hoang Van Em", "Lap trinh vien", 25000000.0},
                {6, "Vo Thi Phuong", "Ke toan", 12000000.0}
            };
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO NhanVien(id, ten, chuc_vu, luong) VALUES (?, ?, ?, ?)")) {
                for (Object[] r : data) {
                    ps.setInt(1, (Integer) r[0]);
                    ps.setString(2, (String) r[1]);
                    ps.setString(3, (String) r[2]);
                    ps.setDouble(4, (Double) r[3]);
                    ps.addBatch();
                }
                ps.executeBatch();
                conn.commit();
            }

            System.out.println("===== DANH SACH NHAN VIEN =====");
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM NhanVien")) {
                while (rs.next()) {
                    System.out.printf("%d | %-15s | %-15s | %,.0f%n", rs.getInt("id"),
                            rs.getString("ten"), rs.getString("chuc_vu"), rs.getDouble("luong"));
                }
            }

            System.out.println("\n===== HAM TONG HOP =====");
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT SUM(luong) AS tong, AVG(luong) AS tb, COUNT(*) AS sl FROM NhanVien")) {
                rs.next();
                System.out.printf("Tong luong      : %,.0f%n", rs.getDouble("tong"));
                System.out.printf("Luong trung binh: %,.0f%n", rs.getDouble("tb"));
                System.out.println("So nhan vien    : " + rs.getInt("sl"));
            }

            System.out.println("\n===== LUONG CAO NHAT / THAP NHAT =====");
            printEmployee(stmt, "Cao nhat ", "SELECT * FROM NhanVien WHERE luong = (SELECT MAX(luong) FROM NhanVien)");
            printEmployee(stmt, "Thap nhat", "SELECT * FROM NhanVien WHERE luong = (SELECT MIN(luong) FROM NhanVien)");

            System.out.println("\n===== SO NHAN VIEN THEO CHUC VU (GROUP BY) =====");
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT chuc_vu, COUNT(*) AS so_luong FROM NhanVien GROUP BY chuc_vu ORDER BY so_luong DESC")) {
                while (rs.next()) {
                    System.out.printf("%-15s : %d%n", rs.getString("chuc_vu"), rs.getInt("so_luong"));
                }
            }
        }
    }

    private static void printEmployee(Statement stmt, String label, String sql) throws SQLException {
        try (ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("%s: %s (%s) - %,.0f%n", label, rs.getString("ten"),
                        rs.getString("chuc_vu"), rs.getDouble("luong"));
            }
        }
    }
}
