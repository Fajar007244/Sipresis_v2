package model;

public class Siswa {
    private int id;
    private String nis;
    private String nama;
    private String kelas;
    private String jenisKelamin;
    private String tahunAjaran;
    private String keterangan;
    private Presensi.StatusPresensi status;

    // Konstruktor lengkap
    public Siswa(int id, String nis, String nama, String kelas, String jenisKelamin, String tahunAjaran) {
        this.id = id;
        this.nis = nis;
        this.nama = nama;
        this.kelas = kelas;
        this.jenisKelamin = jenisKelamin;
        this.tahunAjaran = tahunAjaran;
        this.keterangan = "";
        this.status = null;
    }

    // Konstruktor untuk menambah siswa baru
    public Siswa(String nis, String nama, String kelas, String jenisKelamin, String tahunAjaran) {
        this.nis = nis;
        this.nama = nama;
        this.kelas = kelas;
        this.jenisKelamin = jenisKelamin;
        this.tahunAjaran = tahunAjaran;
        this.keterangan = "";
        this.status = null;
    }

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNis() { return nis; }
    public void setNis(String nis) { this.nis = nis; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getKelas() { return kelas; }
    public void setKelas(String kelas) { this.kelas = kelas; }

    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public String getTahunAjaran() { return tahunAjaran; }
    public void setTahunAjaran(String tahunAjaran) { this.tahunAjaran = tahunAjaran; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    public Presensi.StatusPresensi getStatus() { return status; }
    public void setStatus(Presensi.StatusPresensi status) { this.status = status; }

    @Override
    public String toString() {
        return "Siswa{" +
                "id=" + id +
                ", nis='" + nis + '\'' +
                ", nama='" + nama + '\'' +
                ", kelas='" + kelas + '\'' +
                ", jenisKelamin='" + jenisKelamin + '\'' +
                ", tahunAjaran='" + tahunAjaran + '\'' +
                ", keterangan='" + keterangan + '\'' +
                ", status=" + status +
                '}';
    }
}
