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
            SiswaController siswaController = new SiswaController();
            UserController userController = new UserController();

            while (rs.next()) {
                LocalDate tanggal = rs.getDate("tanggal").toLocalDate();
                Presensi.StatusPresensi status = Presensi.StatusPresensi.valueOf(rs.getString("status_kehadiran"));
                
                Guru pencatat = (Guru) userController.login(
                    rs.getString("username"), 
                    rs.getString("password"), 
                    "GURU"
                );

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
}
