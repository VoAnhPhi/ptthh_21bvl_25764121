package lab8.bai04;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Bai 4: Xu ly ngoai le triet de - lam lai bai 1 (SanPham) voi try-catch-finally nghiem ngat.
 * Moi tai nguyen JDBC deu duoc dong trong finally, ke ca khi SQLException xay ra.
 */
public class ExceptionHandlingDemo {

    public static void main(String[] args) {
        System.out.println("=== Lan 1: chay binh thuong ===");
        run("SELECT maSP, tenSP, gia, soluong FROM SanPham ORDER BY maSP");

        System.out.println("\n=== Lan 2: SQL sai (bang KhongTonTai) -> SQLException ===");
        run("SELECT * FROM KhongTonTai");
    }

    static void run(String sqlSelect) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:exception.db");
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS SanPham (maSP INTEGER PRIMARY KEY, "
                    + "tenSP TEXT NOT NULL, gia REAL, soluong INTEGER)");
            stmt.executeUpdate("DELETE FROM SanPham");
            stmt.executeUpdate("INSERT INTO SanPham VALUES (1, 'Laptop Dell', 15000000, 10)");
            stmt.executeUpdate("INSERT INTO SanPham VALUES (2, 'Chuot Logitech', 250000, 50)");
            stmt.executeUpdate("INSERT INTO SanPham VALUES (3, 'Ban phim co', 900000, 30)");

            rs = stmt.executeQuery(sqlSelect);
            while (rs.next()) {
                System.out.printf("%d | %-16s | %.0f | %d%n", rs.getInt("maSP"),
                        rs.getString("tenSP"), rs.getDouble("gia"), rs.getInt("soluong"));
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("  SQLState = " + e.getSQLState() + ", ErrorCode = " + e.getErrorCode());
        } finally {
            // Moi lenh close() nam trong try rieng: mot tai nguyen loi khong lam ro ri cac tai nguyen con lai
            close(rs, "ResultSet");
            close(stmt, "Statement");
            close(conn, "Connection");
        }
    }

    static void close(AutoCloseable resource, String name) {
        if (resource == null) {
            System.out.println("  " + name + ": chua duoc tao, bo qua.");
            return;
        }
        try {
            resource.close();
            System.out.println("  " + name + ": da dong.");
        } catch (Exception e) {
            System.out.println("  " + name + ": loi khi dong - " + e.getMessage());
        }
    }
}
