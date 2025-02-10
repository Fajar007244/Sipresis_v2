-- Buat database
CREATE DATABASE IF NOT EXISTS sipresis;
USE sipresis;

-- Tabel Pengguna (User)
CREATE TABLE IF NOT EXISTS pengguna (
    id_pengguna INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nama_lengkap VARCHAR(100) NOT NULL,
    level ENUM('Admin', 'Guru') NOT NULL,
    status ENUM('Aktif', 'Nonaktif') DEFAULT 'Aktif',
    kelas VARCHAR(20)
);

-- Tabel Siswa
CREATE TABLE IF NOT EXISTS siswa (
    id_siswa INT AUTO_INCREMENT PRIMARY KEY,
    nis VARCHAR(20) UNIQUE NOT NULL,
    nama_siswa VARCHAR(100) NOT NULL,
    kelas VARCHAR(20) NOT NULL,
    jenis_kelamin ENUM('Laki-laki', 'Perempuan') NOT NULL,
    tahun_ajaran VARCHAR(20) NOT NULL
);

-- Tabel Presensi
CREATE TABLE IF NOT EXISTS presensi (
    id_presensi INT AUTO_INCREMENT PRIMARY KEY,
    id_siswa INT NOT NULL,
    tanggal DATE NOT NULL,
    status_kehadiran ENUM('Hadir', 'Izin', 'Sakit', 'Alfa') NOT NULL,
    keterangan TEXT,
    id_guru INT NOT NULL,
    FOREIGN KEY (id_siswa) REFERENCES siswa(id_siswa),
    FOREIGN KEY (id_guru) REFERENCES pengguna(id_pengguna)
);

-- Data Awal Pengguna
INSERT INTO pengguna (username, password, nama_lengkap, level, status, kelas) VALUES 
('admin', 'admin123', 'Administrator Utama', 'Admin', 'Aktif', NULL),
('guru1', 'guru123', 'Budi Santoso', 'Guru', 'Aktif', 'X-A'),
('guru2', 'guru456', 'Sri Rahayu', 'Guru', 'Aktif', 'X-B');

-- Data Awal Siswa
INSERT INTO siswa (nis, nama_siswa, kelas, jenis_kelamin, tahun_ajaran) VALUES 
('2023001', 'Ahmad Fauzi', 'X-A', 'Laki-laki', '2023/2024'),
('2023002', 'Siti Nurhaliza', 'X-B', 'Perempuan', '2023/2024'),
('2023003', 'Rendi Pratama', 'X-A', 'Laki-laki', '2023/2024'),
('2023004', 'Dewi Anggraini', 'X-B', 'Perempuan', '2023/2024');

-- Data Awal Presensi (contoh)
INSERT INTO presensi (id_siswa, tanggal, status_kehadiran, keterangan, id_guru) VALUES 
(1, '2024-01-15', 'Hadir', NULL, 2),
(2, '2024-01-15', 'Hadir', NULL, 2),
(3, '2024-01-15', 'Izin', 'Sakit demam', 3),
(4, '2024-01-15', 'Hadir', NULL, 3);

-- Tambahkan index untuk optimasi query
CREATE INDEX idx_siswa_nis ON siswa(nis);
CREATE INDEX idx_presensi_tanggal ON presensi(tanggal);
CREATE INDEX idx_pengguna_username ON pengguna(username);
