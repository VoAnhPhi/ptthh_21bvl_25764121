package lab8.bai01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Bai 1: Ket noi SQLite, tao bang NhanVien, them 3 nhan vien, truy van va hien thi.
 */
public class NhanVienDemo {

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // B1. Nap driver va mo ket noi
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:nhanvien.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Ket noi SQLite thanh cong: " + url);

            // B2. Tao Statement
            stmt = conn.createStatement();

            // B3.1. Tao bang
            stmt.executeUpdate("DROP TABLE IF EXISTS NhanVien");
            String sql_create_table = "CREATE TABLE NhanVien ("
                    + "id INTEGER PRIMARY KEY, "
                    + "ten TEXT NOT NULL, "
                    + "chuc_vu TEXT)";
            stmt.executeUpdate(sql_create_table);
            System.out.println("Da tao bang NhanVien.");

            // B3.2. Them du lieu
            String sql_insert1 = "INSERT INTO NhanVien(id, ten, chuc_vu) VALUES (1, 'Nguyen Van An', 'Giam doc')";
            String sql_insert2 = "INSERT INTO NhanVien(id, ten, chuc_vu) VALUES (2, 'Tran Thi Binh', 'Ke toan')";
            String sql_insert3 = "INSERT INTO NhanVien(id, ten, chuc_vu) VALUES (3, 'Le Van Cuong', 'Lap trinh vien')";
            stmt.executeUpdate(sql_insert1);
            stmt.executeUpdate(sql_insert2);
            stmt.executeUpdate(sql_insert3);
            System.out.println("Da them 3 nhan vien.");

            // B4. Truy van va hien thi
            String sql_select = "SELECT id, ten, chuc_vu FROM NhanVien";
            rs = stmt.executeQuery(sql_select);

            System.out.println("\n===== DANH SACH NHAN VIEN =====");
            System.out.printf("%-4s %-20s %s%n", "ID", "TEN", "CHUC VU");
            while (rs.next()) {
                System.out.printf("%-4d %-20s %s%n",
                        rs.getInt("id"), rs.getString("ten"), rs.getString("chuc_vu"));
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Khong tim thay driver SQLite: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Loi SQL: " + e.getMessage());
        } finally {
            // B5. Dong tai nguyen theo thu tu nguoc lai
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
                System.out.println("\nDa dong ResultSet, Statement, Connection.");
            } catch (SQLException e) {
                System.out.println("Loi khi dong ket noi: " + e.getMessage());
            }
        }
    }
}
