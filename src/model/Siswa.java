package model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Siswa {
    private int id;
    private String nis;
    private String nama;
    private String kelas;
    private String jenisKelamin;
    private String tahunAjaran;
    private String keterangan;
    private Presensi.StatusPresensi status;

    // Properti tambahan untuk presensi
    private transient String statusKehadiran;
    private transient String keteranganPresensi;

    // Properti JavaFX
    private SimpleIntegerProperty idProperty;
    private SimpleStringProperty nisProperty;
    private SimpleStringProperty namaSiswaProperty;
    private SimpleStringProperty kelasProperty;
    private SimpleStringProperty jenisKelaminProperty;
    private SimpleStringProperty tahunAjaranProperty;
    private SimpleStringProperty statusKehadiranProperty;
    private SimpleStringProperty keteranganProperty;
    private SimpleBooleanProperty isAktifProperty;

    // Tambahkan field is_aktif
    private boolean isAktif = true;  // Default true sesuai migration script

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
        
        this.idProperty = new SimpleIntegerProperty(id);
        this.nisProperty = new SimpleStringProperty(nis);
        this.namaSiswaProperty = new SimpleStringProperty(nama);
        this.kelasProperty = new SimpleStringProperty(kelas);
        this.jenisKelaminProperty = new SimpleStringProperty(jenisKelamin);
        this.tahunAjaranProperty = new SimpleStringProperty(tahunAjaran);
        
        // Inisialisasi properti tambahan
        this.statusKehadiranProperty = new SimpleStringProperty("");
        this.keteranganProperty = new SimpleStringProperty("");
        
        // Inisialisasi is_aktif property
        this.isAktifProperty = new SimpleBooleanProperty(true);
    }

    // Konstruktor untuk membuat siswa baru tanpa ID
    public Siswa(String nis, String nama, String kelas, String jenisKelamin, String tahunAjaran) {
        this.id = 0; // ID akan diset oleh database
        this.nis = nis;
        this.nama = nama;
        this.kelas = kelas;
        this.jenisKelamin = jenisKelamin;
        this.tahunAjaran = tahunAjaran;

        // Inisialisasi properti JavaFX
        this.idProperty = new SimpleIntegerProperty(this.id);
        this.nisProperty = new SimpleStringProperty(this.nis);
        this.namaSiswaProperty = new SimpleStringProperty(this.nama);
        this.kelasProperty = new SimpleStringProperty(this.kelas);
        this.jenisKelaminProperty = new SimpleStringProperty(this.jenisKelamin);
        this.tahunAjaranProperty = new SimpleStringProperty(this.tahunAjaran);
        
        // Inisialisasi is_aktif property
        this.isAktifProperty = new SimpleBooleanProperty(true);
    }

    // Konstruktor untuk tabel presensi
    public Siswa(int idSiswa, String nis, String namaSiswa, String kelas, 
                 String jenisKelamin, String tahunAjaran, 
                 String statusKehadiran, String keterangan) {
        this(idSiswa, nis, namaSiswa, kelas, jenisKelamin, tahunAjaran);
        this.statusKehadiran = statusKehadiran;
        this.keteranganPresensi = keterangan;
        
        this.statusKehadiranProperty = new SimpleStringProperty(statusKehadiran);
        this.keteranganProperty = new SimpleStringProperty(keterangan);
    }

    // Getter dan Setter
    public int getId() { return id; }
    public SimpleIntegerProperty idProperty() { return idProperty; }
    public void setId(int id) { 
        this.id = id; 
        this.idProperty.set(id); 
    }

    public String getNis() { return nis; }
    public SimpleStringProperty nisProperty() { return nisProperty; }
    public void setNis(String nis) { 
        this.nis = nis; 
        this.nisProperty.set(nis); 
    }

    public String getNama() { return nama; }
    public SimpleStringProperty namaSiswaProperty() { return namaSiswaProperty; }
    public void setNama(String nama) { 
        this.nama = nama; 
        this.namaSiswaProperty.set(nama); 
    }

    public String getKelas() { return kelas; }
    public SimpleStringProperty kelasProperty() { return kelasProperty; }
    public void setKelas(String kelas) { 
        this.kelas = kelas; 
        this.kelasProperty.set(kelas); 
    }

    public String getJenisKelamin() { return jenisKelamin; }
    public SimpleStringProperty jenisKelaminProperty() { return jenisKelaminProperty; }
    public void setJenisKelamin(String jenisKelamin) { 
        this.jenisKelamin = jenisKelamin; 
        this.jenisKelaminProperty.set(jenisKelamin); 
    }

    public String getTahunAjaran() { return tahunAjaran; }
    public SimpleStringProperty tahunAjaranProperty() { return tahunAjaranProperty; }
    public void setTahunAjaran(String tahunAjaran) { 
        this.tahunAjaran = tahunAjaran; 
        this.tahunAjaranProperty.set(tahunAjaran); 
    }

    public String getKeterangan() { return keterangan; }
    public SimpleStringProperty keteranganProperty() { return keteranganProperty; }
    public void setKeterangan(String keterangan) { 
        this.keterangan = keterangan; 
        this.keteranganProperty.set(keterangan); 
    }

    public Presensi.StatusPresensi getStatus() { return status; }
    public void setStatus(Presensi.StatusPresensi status) { this.status = status; }

    // Getter dan setter untuk status kehadiran
    public String getStatusKehadiran() { return statusKehadiran; }
    public SimpleStringProperty statusKehadiranProperty() { return statusKehadiranProperty; }
    public void setStatusKehadiran(String statusKehadiran) { 
        this.statusKehadiran = statusKehadiran; 
        this.statusKehadiranProperty.set(statusKehadiran); 
    }

    // Getter dan setter untuk keterangan presensi
    public String getKeteranganPresensi() { return keteranganPresensi; }
    public void setKeteranganPresensi(String keteranganPresensi) { this.keteranganPresensi = keteranganPresensi; }

    // Getter dan Setter untuk is_aktif
    public boolean isIsAktif() { return isAktif; }
    public SimpleBooleanProperty isAktifProperty() { return isAktifProperty; }
    public void setIsAktif(boolean isAktif) { 
        this.isAktif = isAktif; 
        this.isAktifProperty.set(isAktif); 
    }

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
                ", statusKehadiran='" + statusKehadiran + '\'' +
                ", keteranganPresensi='" + keteranganPresensi + '\'' +
                ", isAktif=" + isAktif +
                '}';
    }
}
