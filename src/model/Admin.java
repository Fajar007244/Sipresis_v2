package model;

public class Admin extends User {
    public Admin(int id, String nama, String username, String password) {
        super(id, nama, username, password, "ADMIN");
    }

    @Override
    public boolean login() {
        // Implementasi login untuk admin
        // Akan diimplementasikan dengan logika autentikasi di controller
        return true;
    }

    // Metode spesifik admin
    public void kelolaSiswa() {
        System.out.println("Mengelola data siswa");
    }

    public void kelolaAkun() {
        System.out.println("Mengelola akun pengguna");
    }

    public void lihatLaporanAbsensi() {
        System.out.println("Melihat laporan absensi siswa");
    }
}
