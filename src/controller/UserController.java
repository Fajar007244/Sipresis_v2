package controller;

import model.Admin;
import model.Guru;
import model.User;
import database.DatabaseConnection;
import utils.PasswordUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    private Connection connection;

    public UserController() {
        connection = DatabaseConnection.getConnection();
    }

    public User login(String username, String password, String role) {
        try {
            // Debug logging
            // System.out.println("Login attempt: username=" + username + ", role=" + role);

            // Sesuaikan dengan nama tabel di database
            String query = "SELECT * FROM pengguna WHERE username = ? AND level = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            
            // Konversi role untuk sesuai dengan enum di database
            String databaseRole = role.toUpperCase().equals("ADMIN") ? "Admin" : "Guru";
            stmt.setString(2, databaseRole);
            
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Debug logging
                // System.out.println("User found in database: username=" + username + ", role=" + databaseRole);

                // Validasi password sesuai database
                String storedPassword = rs.getString("password");
                
                if (password.equals(storedPassword)) {
                    int id = rs.getInt("id_pengguna");
                    String nama = rs.getString("nama_lengkap");

                    if ("Admin".equals(databaseRole)) {
                        return new Admin(id, nama, username, storedPassword);
                    } else if ("Guru".equals(databaseRole)) {
                        // Ambil kelas dari database jika tersedia
                        String kelas = rs.getString("kelas") != null ? rs.getString("kelas") : "X-A";
                        return new Guru(id, nama, username, storedPassword, kelas);
                    }
                } else {
                    // Debug logging
                    // System.out.println("Password tidak cocok untuk username: " + username);
                    // System.out.println("Stored password: " + storedPassword);
                    // System.out.println("Input password: " + password);
                }
            } else {
                // Debug logging
                // System.out.println("Tidak ditemukan user dengan username: " + username + " dan role: " + databaseRole);
            }
        } catch (SQLException e) {
            System.err.println("Login gagal: " + e.getMessage());
            // e.printStackTrace(); // Hapus atau comment out untuk mengurangi output
        }
        return null;
    }

    public boolean tambahPengguna(User user, String plainPassword) {
        try {
            String hashedPassword = PasswordUtils.hashPassword(plainPassword);
            String query = "INSERT INTO pengguna (username, password, nama_lengkap, level) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getNama());
            stmt.setString(4, user instanceof Admin ? "Admin" : "Guru");
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Tambah pengguna gagal: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePengguna(User user, String plainPassword) {
        try {
            String query;
            PreparedStatement stmt;

            if (plainPassword != null && !plainPassword.isEmpty()) {
                // Update dengan password baru
                query = "UPDATE pengguna SET nama_lengkap = ?, username = ?, password = ?, level = ? WHERE id_pengguna = ?";
                stmt = connection.prepareStatement(query);
                stmt.setString(1, user.getNama());
                stmt.setString(2, user.getUsername());
                
                // Hash password
                String hashedPassword = PasswordUtils.hashPassword(plainPassword);
                stmt.setString(3, hashedPassword);
                
                stmt.setString(4, user.getRole());
                stmt.setInt(5, user.getId());
            } else {
                // Update tanpa password
                query = "UPDATE pengguna SET nama_lengkap = ?, username = ?, level = ? WHERE id_pengguna = ?";
                stmt = connection.prepareStatement(query);
                stmt.setString(1, user.getNama());
                stmt.setString(2, user.getUsername());
                stmt.setString(3, user.getRole());
                stmt.setInt(4, user.getId());
            }

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Update pengguna gagal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean hapusPengguna(int idPengguna) {
        try {
            String query = "DELETE FROM pengguna WHERE id_pengguna = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, idPengguna);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Hapus pengguna gagal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<User> daftarPengguna() {
        List<User> daftarPengguna = new ArrayList<>();
        try {
            String query = "SELECT * FROM pengguna";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id_pengguna");
                String nama = rs.getString("nama_lengkap");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("level");

                User user;
                if (role.equalsIgnoreCase("ADMIN")) {
                    user = new Admin(id, nama, username, password);
                } else {
                    user = new Guru(id, nama, username, password, "Tidak Ditentukan"); // Default kelas
                }

                daftarPengguna.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil daftar pengguna: " + e.getMessage());
            e.printStackTrace();
        }
        return daftarPengguna;
    }
}
