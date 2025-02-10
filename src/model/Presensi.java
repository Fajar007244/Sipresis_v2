package model;

import java.time.LocalDate;

public class Presensi {
    private int id;
    private Siswa siswa;
    private LocalDate tanggal;
    private StatusPresensi status;
    private Guru pencatat;
    private String keterangan;

    public enum StatusPresensi {
        HADIR, IZIN, SAKIT, ALFA
    }

    public Presensi(int id, Siswa siswa, LocalDate tanggal, StatusPresensi status, Guru pencatat, String keterangan) {
        this.id = id;
        this.siswa = siswa;
        this.tanggal = tanggal;
        this.status = status;
        this.pencatat = pencatat;
        this.keterangan = keterangan;
    }

    // Konstruktor tanpa keterangan
    public Presensi(int id, Siswa siswa, LocalDate tanggal, StatusPresensi status, Guru pencatat) {
        this(id, siswa, tanggal, status, pencatat, "");
    }

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Siswa getSiswa() { return siswa; }
    public void setSiswa(Siswa siswa) { this.siswa = siswa; }
    
    public LocalDate getTanggal() { return tanggal; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }
    
    public StatusPresensi getStatus() { return status; }
    public void setStatus(StatusPresensi status) { this.status = status; }
    
    public Guru getPencatat() { return pencatat; }
    public void setPencatat(Guru pencatat) { this.pencatat = pencatat; }
    
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    @Override
    public String toString() {
        return "Presensi{" +
                "id=" + id +
                ", siswa=" + siswa +
                ", tanggal=" + tanggal +
                ", status=" + status +
                ", pencatat=" + pencatat +
                ", keterangan='" + keterangan + '\'' +
                '}';
    }
}
