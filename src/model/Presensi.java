package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;

public class Presensi {
    private int id;
    private Siswa siswa;
    private LocalDate tanggal;
    private StatusPresensi status;
    private Guru pencatat;
    private String keterangan;

    private SimpleIntegerProperty idPresensiProperty;
    private SimpleIntegerProperty idSiswaProperty;
    private SimpleStringProperty namaSiswaProperty;
    private SimpleObjectProperty<LocalDate> tanggalProperty;
    private SimpleStringProperty statusKehadiranProperty;
    private SimpleStringProperty keteranganProperty;
    private SimpleIntegerProperty idGuruProperty;

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

        // Inisialisasi properti JavaFX
        this.idPresensiProperty = new SimpleIntegerProperty(id);
        this.idSiswaProperty = new SimpleIntegerProperty(siswa.getId());
        this.namaSiswaProperty = new SimpleStringProperty(siswa.getNama());
        this.tanggalProperty = new SimpleObjectProperty<>(tanggal);
        this.statusKehadiranProperty = new SimpleStringProperty(status.name());
        this.keteranganProperty = new SimpleStringProperty(keterangan);
        this.idGuruProperty = new SimpleIntegerProperty(pencatat.getId());
    }

    // Konstruktor tanpa keterangan
    public Presensi(int id, Siswa siswa, LocalDate tanggal, StatusPresensi status, Guru pencatat) {
        this(id, siswa, tanggal, status, pencatat, "");
    }

    // Konstruktor untuk tabel JavaFX
    public Presensi(int idPresensi, int idSiswa, String namaSiswa, LocalDate tanggal, 
                    String statusKehadiran, String keterangan, int idGuru) {
        this.idPresensiProperty = new SimpleIntegerProperty(idPresensi);
        this.idSiswaProperty = new SimpleIntegerProperty(idSiswa);
        this.namaSiswaProperty = new SimpleStringProperty(namaSiswa);
        this.tanggalProperty = new SimpleObjectProperty<>(tanggal);
        this.statusKehadiranProperty = new SimpleStringProperty(statusKehadiran);
        this.keteranganProperty = new SimpleStringProperty(keterangan);
        this.idGuruProperty = new SimpleIntegerProperty(idGuru);
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

    // Getter untuk properti JavaFX
    public int getIdPresensi() { return idPresensiProperty.get(); }
    public SimpleIntegerProperty idPresensiProperty() { return idPresensiProperty; }
    public void setIdPresensi(int idPresensi) { this.idPresensiProperty.set(idPresensi); }

    public int getIdSiswa() { return idSiswaProperty.get(); }
    public SimpleIntegerProperty idSiswaProperty() { return idSiswaProperty; }
    public void setIdSiswa(int idSiswa) { this.idSiswaProperty.set(idSiswa); }

    public String getNamaSiswa() { return namaSiswaProperty.get(); }
    public SimpleStringProperty namaSiswaProperty() { return namaSiswaProperty; }
    public void setNamaSiswa(String namaSiswa) { this.namaSiswaProperty.set(namaSiswa); }

    public LocalDate getTanggalFX() { return tanggalProperty.get(); }
    public SimpleObjectProperty<LocalDate> tanggalProperty() { return tanggalProperty; }
    public void setTanggalFX(LocalDate tanggal) { this.tanggalProperty.set(tanggal); }

    public String getStatusKehadiran() { return statusKehadiranProperty.get(); }
    public SimpleStringProperty statusKehadiranProperty() { return statusKehadiranProperty; }
    public void setStatusKehadiran(String statusKehadiran) { this.statusKehadiranProperty.set(statusKehadiran); }

    public String getKeteranganFX() { return keteranganProperty.get(); }
    public SimpleStringProperty keteranganProperty() { return keteranganProperty; }
    public void setKeteranganFX(String keterangan) { this.keteranganProperty.set(keterangan); }

    public int getIdGuru() { return idGuruProperty.get(); }
    public SimpleIntegerProperty idGuruProperty() { return idGuruProperty; }
    public void setIdGuru(int idGuru) { this.idGuruProperty.set(idGuru); }

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
