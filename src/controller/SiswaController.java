package controller;

import model.Siswa;
import model.Presensi;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SiswaController {
    private Connection connection;

    public SiswaController() {
        connection = DatabaseConnection.getConnection();
    }

    public boolean tambahSiswa(Siswa siswa) {
        try {
            String query = "INSERT INTO siswa (nis, nama_siswa, kelas, jenis_kelamin, tahun_ajaran) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, siswa.getNis());
            stmt.setString(2, siswa.getNama());
            stmt.setString(3, siswa.getKelas());
            stmt.setString(4, siswa.getJenisKelamin());
            stmt.setString(5, siswa.getTahunAjaran());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Tambah siswa gagal: " + e.getMessage());
            return false;
        }
    }

    public List<Siswa> daftarSiswa() {
        List<Siswa> siswaList = new ArrayList<>();
        String query = "SELECT id_siswa, nis, nama_siswa, kelas, jenis_kelamin, tahun_ajaran FROM siswa";
        
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Siswa siswa = new Siswa(
                    rs.getInt("id_siswa"),
                    rs.getString("nis"),
                    rs.getString("nama_siswa"),
                    rs.getString("kelas"),
                    rs.getString("jenis_kelamin"),
                    rs.getString("tahun_ajaran")
                );
                
                // Tambahkan keterangan presensi terakhir jika ada
                String presensiQuery = "SELECT status_kehadiran, keterangan FROM presensi " +
                                       "WHERE id_siswa = ? ORDER BY tanggal DESC LIMIT 1";
                try (PreparedStatement presensiStmt = connection.prepareStatement(presensiQuery)) {
                    presensiStmt.setInt(1, siswa.getId());
                    try (ResultSet presensiRs = presensiStmt.executeQuery()) {
                        if (presensiRs.next()) {
                            String statusStr = presensiRs.getString("status_kehadiran").toUpperCase();
                            try {
                                siswa.setStatus(Presensi.StatusPresensi.valueOf(statusStr));
                            } catch (IllegalArgumentException e) {
                                // Fallback ke status default jika terjadi kesalahan
                                siswa.setStatus(Presensi.StatusPresensi.HADIR);
                            }
                            siswa.setKeterangan(presensiRs.getString("keterangan"));
                        }
                    }
                }
                
                siswaList.add(siswa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return siswaList;
    }

    public Siswa getSiswaById(int id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM siswa WHERE id_siswa = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nis = rs.getString("nis");
                String nama = rs.getString("nama_siswa");
                String kelas = rs.getString("kelas");
                String jenisKelamin = rs.getString("jenis_kelamin");
                String tahunAjaran = rs.getString("tahun_ajaran");

                return new Siswa(id, nis, nama, kelas, jenisKelamin, tahunAjaran);
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil siswa: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateSiswa(Siswa siswa) {
        try {
            String query = "UPDATE siswa SET nis = ?, nama_siswa = ?, kelas = ?, jenis_kelamin = ?, tahun_ajaran = ? WHERE id_siswa = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, siswa.getNis());
            stmt.setString(2, siswa.getNama());
            stmt.setString(3, siswa.getKelas());
            stmt.setString(4, siswa.getJenisKelamin());
            stmt.setString(5, siswa.getTahunAjaran());
            stmt.setInt(6, siswa.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update siswa gagal: " + e.getMessage());
            return false;
        }
    }

    public boolean hapusSiswa(int id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM siswa WHERE id_siswa = ?");
            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Hapus siswa gagal: " + e.getMessage());
            return false;
        }
    }

    public List<Siswa> getSiswaByKelas(String kelas) {
        List<Siswa> siswaList = new ArrayList<>();
        String query = "SELECT id_siswa, nis, nama_siswa, kelas, jenis_kelamin, tahun_ajaran FROM siswa WHERE kelas = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, kelas);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Siswa siswa = new Siswa(
                        rs.getInt("id_siswa"),
                        rs.getString("nis"),
                        rs.getString("nama_siswa"),
                        rs.getString("kelas"),
                        rs.getString("jenis_kelamin"),
                        rs.getString("tahun_ajaran")
                    );
                    
                    // Tambahkan keterangan presensi terakhir jika ada
                    String presensiQuery = "SELECT status_kehadiran, keterangan FROM presensi " +
                                           "WHERE id_siswa = ? ORDER BY tanggal DESC LIMIT 1";
                    try (PreparedStatement presensiStmt = connection.prepareStatement(presensiQuery)) {
                        presensiStmt.setInt(1, siswa.getId());
                        try (ResultSet presensiRs = presensiStmt.executeQuery()) {
                            if (presensiRs.next()) {
                                String statusStr = presensiRs.getString("status_kehadiran").toUpperCase();
                                try {
                                    siswa.setStatus(Presensi.StatusPresensi.valueOf(statusStr));
                                } catch (IllegalArgumentException e) {
                                    // Fallback ke status default jika terjadi kesalahan
                                    siswa.setStatus(Presensi.StatusPresensi.HADIR);
                                }
                                siswa.setKeterangan(presensiRs.getString("keterangan"));
                            }
                        }
                    }
                    
                    siswaList.add(siswa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return siswaList;
    }

    public List<Siswa> daftarSiswaByKelas(String kelas) {
        return getSiswaByKelas(kelas);
    }
}
