package controller;

import model.Guru;
import model.Presensi;
import model.Siswa;
import database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PresensiController {
    private Connection connection;

    public PresensiController() {
        connection = DatabaseConnection.getConnection();
    }

    public boolean inputPresensi(Presensi presensi) {
        try {
            String query = "INSERT INTO presensi (id_siswa, tanggal, status_kehadiran, id_guru, keterangan) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, presensi.getSiswa().getId());
            stmt.setDate(2, Date.valueOf(presensi.getTanggal()));
            stmt.setString(3, presensi.getStatus().name());
            stmt.setInt(4, presensi.getPencatat().getId());
            stmt.setString(5, presensi.getKeterangan());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Input presensi gagal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Presensi> laporanPresensiSiswa(Siswa siswa, LocalDate startDate, LocalDate endDate) {
        List<Presensi> presensiList = new ArrayList<>();
        try {
            String query = "SELECT p.*, s.*, g.* FROM presensi p " +
                           "JOIN siswa s ON p.id_siswa = s.id_siswa " +
                           "JOIN pengguna g ON p.id_guru = g.id_pengguna " +
                           "WHERE p.id_siswa = ? AND p.tanggal BETWEEN ? AND ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, siswa.getId());
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));

            ResultSet rs = stmt.executeQuery();
            UserController userController = new UserController();

            while (rs.next()) {
                LocalDate tanggal = rs.getDate("tanggal").toLocalDate();
                String statusStr = rs.getString("status_kehadiran");
                
                // Logging tambahan untuk debug
                System.out.println("Status Presensi dari Database: " + statusStr);
                
                Presensi.StatusPresensi status;
                try {
                    status = Presensi.StatusPresensi.valueOf(statusStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.err.println("Status presensi tidak valid: " + statusStr);
                    // Default ke ALFA jika status tidak valid
                    status = Presensi.StatusPresensi.ALFA;
                }
                
                Guru pencatat = (Guru) userController.login(
                    rs.getString("username"), 
                    rs.getString("password"), 
                    "GURU"
                );

                if (pencatat == null) {
                    // Buat objek Guru default jika login gagal
                    pencatat = new Guru(
                        rs.getInt("id_guru"), 
                        "Guru Tidak Dikenal", 
                        rs.getString("username"), 
                        "", 
                        "Tidak Ditentukan"
                    );
                }

                Presensi presensi = new Presensi(
                    rs.getInt("id_presensi"),
                    siswa,
                    tanggal,
                    status,
                    pencatat,
                    rs.getString("keterangan")
                );

                presensiList.add(presensi);
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil laporan presensi: " + e.getMessage());
            e.printStackTrace();
        }
        return presensiList;
    }

    public List<Presensi> laporanPresensiKelas(String kelas, LocalDate tanggal) {
        List<Presensi> presensiList = new ArrayList<>();
        try {
            String query = "SELECT p.*, s.*, g.* FROM presensi p " +
                           "JOIN siswa s ON p.id_siswa = s.id_siswa " +
                           "JOIN pengguna g ON p.id_guru = g.id_pengguna " +
                           "WHERE s.kelas = ? AND p.tanggal = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, kelas);
            stmt.setDate(2, Date.valueOf(tanggal));

            ResultSet rs = stmt.executeQuery();
            UserController userController = new UserController();

            while (rs.next()) {
                Siswa siswa = new Siswa(
                    rs.getInt("id_siswa"),
                    rs.getString("nis"),
                    rs.getString("nama_siswa"),
                    kelas,
                    rs.getString("jenis_kelamin"),
                    rs.getString("tahun_ajaran")
                );

                Guru pencatat = null;
                try {
                    pencatat = (Guru) userController.login(
                        rs.getString("username"), 
                        rs.getString("password"), 
                        "GURU"
                    );
                } catch (Exception e) {
                    System.err.println("Failed to retrieve Guru: " + e.getMessage());
                }

                // If pencatat is null, create a default Guru
                if (pencatat == null) {
                    pencatat = new Guru(
                        rs.getInt("id_pengguna"),
                        "Guru Tidak Dikenal",
                        rs.getString("username"),
                        "",
                        "Tidak Ditentukan"
                    );
                }

                String statusStr = rs.getString("status_kehadiran").toUpperCase();
                Presensi.StatusPresensi status;
                try {
                    status = Presensi.StatusPresensi.valueOf(statusStr);
                } catch (IllegalArgumentException e) {
                    // Fallback ke status default jika terjadi kesalahan
                    status = Presensi.StatusPresensi.HADIR;
                }

                Presensi presensi = new Presensi(
                    rs.getInt("id_presensi"),
                    siswa,
                    tanggal,
                    status,
                    pencatat,
                    rs.getString("keterangan")
                );

                presensiList.add(presensi);
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil laporan presensi kelas: " + e.getMessage());
            e.printStackTrace();
        }
        return presensiList;
    }

    public List<Presensi> laporanPresensiKelasBulanan(String kelas, int bulan, int tahun) {
        List<Presensi> presensiList = new ArrayList<>();
        try {
            String query = "SELECT p.*, s.*, g.* FROM presensi p " +
                           "JOIN siswa s ON p.id_siswa = s.id_siswa " +
                           "JOIN pengguna g ON p.id_guru = g.id_pengguna " +
                           "WHERE s.kelas = ? AND MONTH(p.tanggal) = ? AND YEAR(p.tanggal) = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, kelas);
            stmt.setInt(2, bulan);
            stmt.setInt(3, tahun);

            ResultSet rs = stmt.executeQuery();
            UserController userController = new UserController();

            while (rs.next()) {
                Siswa siswa = new Siswa(
                    rs.getInt("id_siswa"),
                    rs.getString("nis"),
                    rs.getString("nama_siswa"),
                    kelas,
                    rs.getString("jenis_kelamin"),
                    rs.getString("tahun_ajaran")
                );

                Guru pencatat = (Guru) userController.login(
                    rs.getString("username"), 
                    rs.getString("password"), 
                    "GURU"
                );

                String statusStr = rs.getString("status_kehadiran").toUpperCase();
                Presensi.StatusPresensi status;
                try {
                    status = Presensi.StatusPresensi.valueOf(statusStr);
                } catch (IllegalArgumentException e) {
                    // Fallback ke status default jika terjadi kesalahan
                    status = Presensi.StatusPresensi.HADIR;
                }

                Presensi presensi = new Presensi(
                    rs.getInt("id_presensi"),
                    siswa,
                    rs.getDate("tanggal").toLocalDate(),
                    status,
                    pencatat,
                    rs.getString("keterangan")
                );

                presensiList.add(presensi);
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil laporan presensi kelas bulanan: " + e.getMessage());
            e.printStackTrace();
        }
        return presensiList;
    }

    public boolean cekPresensiSudahAda(String kelas, LocalDate tanggal) {
        try {
            String query = "SELECT COUNT(*) FROM presensi p " +
                           "JOIN siswa s ON p.id_siswa = s.id_siswa " +
                           "WHERE s.kelas = ? AND p.tanggal = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, kelas);
            stmt.setDate(2, Date.valueOf(tanggal));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Gagal memeriksa presensi: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean cekPresensiSudahAda(int idSiswa, LocalDate tanggal) {
        try {
            String query = "SELECT COUNT(*) FROM presensi WHERE id_siswa = ? AND tanggal = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, idSiswa);
            stmt.setDate(2, Date.valueOf(tanggal));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Gagal memeriksa presensi: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public List<Presensi> cariPresensi(int idSiswa, LocalDate tanggal) {
        List<Presensi> daftarPresensi = new ArrayList<>();
        try {
            String query = "SELECT p.*, s.nama_siswa, g.nama_lengkap AS nama_guru " +
                           "FROM presensi p " +
                           "JOIN siswa s ON p.id_siswa = s.id_siswa " +
                           "JOIN pengguna g ON p.id_guru = g.id_pengguna " +
                           "WHERE p.id_siswa = ? AND p.tanggal = ?";
            
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, idSiswa);
            stmt.setDate(2, Date.valueOf(tanggal));

            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Presensi presensi = new Presensi(
                    rs.getInt("id_presensi"),
                    rs.getInt("id_siswa"),
                    rs.getString("nama_siswa"),
                    rs.getDate("tanggal").toLocalDate(),
                    rs.getString("status_kehadiran"),
                    rs.getString("keterangan"),
                    rs.getInt("id_guru")
                );
                daftarPresensi.add(presensi);
            }
        } catch (SQLException e) {
            System.err.println("Gagal mencari presensi: " + e.getMessage());
            e.printStackTrace();
        }
        return daftarPresensi;
    }

    public boolean updatePresensi(Presensi presensi) {
        try {
            String query = "UPDATE presensi SET status_kehadiran = ?, keterangan = ? WHERE id_presensi = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, presensi.getStatusKehadiran());
            stmt.setString(2, presensi.getKeterangan());
            stmt.setInt(3, presensi.getIdPresensi());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Gagal update presensi: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean tambahPresensi(int idSiswa, LocalDate tanggal, String statusKehadiran, String keterangan, int idGuru) {
        // Cek apakah presensi sudah ada untuk siswa pada tanggal tersebut
        if (cekPresensiSudahAda(idSiswa, tanggal)) {
            System.err.println("Presensi untuk siswa " + idSiswa + " pada tanggal " + tanggal + " sudah ada.");
            return false;
        }

        try {
            String query = "INSERT INTO presensi (id_siswa, tanggal, status_kehadiran, id_guru, keterangan) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, idSiswa);
            stmt.setDate(2, Date.valueOf(tanggal));
            stmt.setString(3, statusKehadiran);
            stmt.setInt(4, idGuru);
            stmt.setString(5, keterangan);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Input presensi gagal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Metode pembantu untuk mengonversi status
    private Presensi.StatusPresensi convertStatusKehadiran(String statusKehadiran) {
        if (statusKehadiran == null) {
            return Presensi.StatusPresensi.ALFA;
        }
        
        // Normalisasi string status
        String normalizedStatus = statusKehadiran.toUpperCase().trim();
        
        switch (normalizedStatus) {
            case "HADIR":
                return Presensi.StatusPresensi.HADIR;
            case "IZIN":
                return Presensi.StatusPresensi.IZIN;
            case "SAKIT":
                return Presensi.StatusPresensi.SAKIT;
            case "ALFA":
            default:
                return Presensi.StatusPresensi.ALFA;
        }
    }

    public List<Presensi> cariPresensiByKelasAndTanggal(String kelas, LocalDate tanggal, Guru guru) {
        List<Presensi> presensiList = new ArrayList<>();
        
        // Updated query with correct column names
        String query = "SELECT presensi.*, siswa.* FROM presensi " +
                       "INNER JOIN siswa ON presensi.id_siswa = siswa.id_siswa " +
                       "WHERE siswa.kelas = ? AND presensi.tanggal = ? AND siswa.is_aktif = 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, kelas);
            stmt.setDate(2, Date.valueOf(tanggal));
            
            System.out.println("Executing query: " + query);
            System.out.println("Parameters: kelas=" + kelas + ", tanggal=" + tanggal);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Siswa siswa = new Siswa(
                    rs.getInt("id_siswa"),
                    rs.getString("nis"),
                    rs.getString("nama_siswa"),
                    rs.getString("kelas"),
                    rs.getString("jenis_kelamin"),
                    rs.getString("tahun_ajaran")
                );
                
                // Set is_aktif from the database
                siswa.setIsAktif(rs.getBoolean("is_aktif"));
                
                // Konversi status dengan metode pembantu
                String statusKehadiranStr = rs.getString("status_kehadiran");
                Presensi.StatusPresensi statusKehadiran = convertStatusKehadiran(statusKehadiranStr);
                
                Presensi presensi = new Presensi(
                    rs.getInt("id_presensi"),
                    siswa,
                    rs.getDate("tanggal").toLocalDate(),
                    statusKehadiran,
                    guru,
                    rs.getString("keterangan")
                );
                
                presensiList.add(presensi);
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + query);
            e.printStackTrace(System.err);
        }
        
        return presensiList;
    }
}
