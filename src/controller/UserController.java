package controller;

import model.Admin;
import model.Guru;
import model.User;
import database.DatabaseConnection;

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
            String query = "SELECT * FROM pengguna WHERE username = ? AND password = ? AND level = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);  
            stmt.setString(3, role);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id_pengguna");
                String nama = rs.getString("nama_lengkap");
                String kelas = rs.getString("kelas");

                if (role.equalsIgnoreCase("ADMIN")) {
                    return new Admin(id, nama, username, password);
                } else if (role.equalsIgnoreCase("GURU")) {
                    return new Guru(id, nama, username, password, 
                        kelas != null ? kelas : "Tidak Ditentukan");
                }
            }
        } catch (SQLException e) {
            System.err.println("Login gagal: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean tambahPengguna(User user, String plainPassword) {
        try {
            String query = "INSERT INTO pengguna (nama_lengkap, username, password, level) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, user.getNama());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, plainPassword);  
            stmt.setString(4, user.getRole());

            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Tambah pengguna gagal: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePengguna(User user, String plainPassword) {
        try {
            String query;
            PreparedStatement stmt;

            if (plainPassword != null && !plainPassword.isEmpty()) {
                // Update dengan password baru
                if (user instanceof Guru) {
                    query = "UPDATE pengguna SET nama_lengkap = ?, username = ?, password = ?, level = ?, kelas = ? WHERE id_pengguna = ?";
                    stmt = connection.prepareStatement(query);
                    stmt.setString(1, user.getNama());
                    stmt.setString(2, user.getUsername());
                    stmt.setString(3, plainPassword);
                    stmt.setString(4, user.getRole());
                    stmt.setString(5, ((Guru) user).getKelasYangDiajar());
                    stmt.setInt(6, user.getId());
                } else {
                    query = "UPDATE pengguna SET nama_lengkap = ?, username = ?, password = ?, level = ? WHERE id_pengguna = ?";
                    stmt = connection.prepareStatement(query);
                    stmt.setString(1, user.getNama());
                    stmt.setString(2, user.getUsername());
                    stmt.setString(3, plainPassword);
                    stmt.setString(4, user.getRole());
                    stmt.setInt(5, user.getId());
                }
            } else {
                // Update tanpa password
                if (user instanceof Guru) {
                    query = "UPDATE pengguna SET nama_lengkap = ?, username = ?, level = ?, kelas = ? WHERE id_pengguna = ?";
                    stmt = connection.prepareStatement(query);
                    stmt.setString(1, user.getNama());
                    stmt.setString(2, user.getUsername());
                    stmt.setString(3, user.getRole());
                    stmt.setString(4, ((Guru) user).getKelasYangDiajar());
                    stmt.setInt(5, user.getId());
                } else {
                    query = "UPDATE pengguna SET nama_lengkap = ?, username = ?, level = ? WHERE id_pengguna = ?";
                    stmt = connection.prepareStatement(query);
                    stmt.setString(1, user.getNama());
                    stmt.setString(2, user.getUsername());
                    stmt.setString(3, user.getRole());
                    stmt.setInt(4, user.getId());
                }
            }

            int rowsAffected = stmt.executeUpdate();
            
            // Tambahkan logging untuk debug
            if (rowsAffected > 0) {
                System.out.println("Berhasil update pengguna: " + user.getNama());
                if (user instanceof Guru) {
                    System.out.println("Kelas yang diajar: " + ((Guru) user).getKelasYangDiajar());
                }
            } else {
                System.err.println("Gagal update pengguna: " + user.getNama());
            }
            
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
                String kelas = rs.getString("kelas");

                User user;
                if (role.equalsIgnoreCase("ADMIN")) {
                    user = new Admin(id, nama, username, password);
                } else {
                    user = new Guru(id, nama, username, password, 
                        kelas != null ? kelas : "Tidak Ditentukan");
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
