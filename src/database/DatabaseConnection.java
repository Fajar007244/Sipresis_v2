package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = System.getenv().getOrDefault("DATABASE_URL", "jdbc:mysql://localhost:3306/sipresis");
    private static final String USERNAME = System.getenv().getOrDefault("DATABASE_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("DATABASE_PASSWORD", "");
    private static Connection connection;

    static {
        try {
            // Pastikan driver MySQL tersedia
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver tidak ditemukan: " + e.getMessage());
            throw new RuntimeException("MySQL JDBC Driver tidak ditemukan", e);
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                
                // Tambahkan kondisi koneksi berhasil
                if (connection != null) {
                    System.out.println("Koneksi database berhasil!");
                    // Tambahan: Cek apakah koneksi valid
                    if (connection.isValid(5)) {
                        System.out.println("Koneksi database aktif dan valid.");
                    } else {
                        System.err.println("Koneksi database tidak valid.");
                    }
                }
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("Koneksi database gagal: " + e.getMessage());
            System.err.println("URL: " + URL);
            System.err.println("Username: " + USERNAME);
            System.err.println("Password: " + (PASSWORD.isEmpty() ? "[KOSONG]" : "[TERSEDIA]"));
            return null;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Koneksi database ditutup.");
            }
        } catch (SQLException e) {
            System.err.println("Gagal menutup koneksi database: " + e.getMessage());
        }
    }

    // Metode tambahan untuk memeriksa status koneksi
    public static boolean testConnection() {
        try {
            Connection testConn = getConnection();
            return testConn != null && !testConn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
