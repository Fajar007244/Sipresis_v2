package model;

public class Guru extends User {
    private String kelasYangDiajar;

    public Guru(int id, String nama, String username, String password, String kelasYangDiajar) {
        super(id, nama, username, password, "GURU");
        this.kelasYangDiajar = kelasYangDiajar;
    }

    @Override
    public boolean login() {
        // Implementasi login untuk guru
        // Akan diimplementasikan dengan logika autentikasi di controller
        return true;
    }

    // Metode spesifik guru
    public void inputPresensi() {
        System.out.println("Input presensi siswa kelas " + kelasYangDiajar);
    }

    public void lihatKehadiran() {
        System.out.println("Melihat data kehadiran kelas " + kelasYangDiajar);
    }

    public String getKelasYangDiajar() {
        return kelasYangDiajar;
    }

    public void setKelasYangDiajar(String kelasYangDiajar) {
        this.kelasYangDiajar = kelasYangDiajar;
    }
}
