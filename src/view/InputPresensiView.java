package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import model.Guru;
import model.Presensi;
import model.Siswa;
import controller.PresensiController;
import controller.SiswaController;

import java.time.LocalDate;
import java.util.List;

public class InputPresensiView {
    private Stage primaryStage;
    private GuruDashboardView guruDashboardView;
    private Guru guru;
    private PresensiController presensiController;
    private SiswaController siswaController;
    
    // Tambahkan field untuk daftarSiswa dan siswaTableView
    private List<Siswa> daftarSiswa;
    private TableView<Siswa> siswaTableView;
    private boolean presensiSudahDiperiksa = false;

    public InputPresensiView(Stage primaryStage, GuruDashboardView guruDashboardView, Guru guru) {
        this.primaryStage = primaryStage;
        this.guruDashboardView = guruDashboardView;
        this.guru = guru;
        this.presensiController = new PresensiController();
        this.siswaController = new SiswaController();
        initUI();
    }

    private void initUI() {
        primaryStage.setTitle("SIPRESIS - Input Presensi");

        // Layout utama dengan gradient background
        VBox mainLayout = new VBox(20);
        mainLayout.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #f0f4f8, #e0e8f0);" +
            "-fx-padding: 30px;"
        );
        mainLayout.setAlignment(Pos.CENTER);

        // Judul
        Label titleLabel = new Label("Input Presensi Kelas " + guru.getKelasYangDiajar());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Container untuk form
        VBox formContainer = new VBox(15);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 30px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        // Tanggal Presensi
        Label tanggalLabel = new Label("Tanggal Presensi:");
        tanggalLabel.setStyle("-fx-font-weight: bold;");
        DatePicker tanggalPicker = new DatePicker();
        tanggalPicker.setValue(LocalDate.now());
        tanggalPicker.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #bdc3c7;" +
            "-fx-border-radius: 5;" +
            "-fx-padding: 10px;"
        );

        // Tambahkan listener untuk mengecek presensi saat tanggal berubah
        tanggalPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Cek apakah sudah ada presensi untuk tanggal ini
                boolean sudahAdaPresensi = presensiController.cekPresensiSudahAda(guru.getKelasYangDiajar(), newValue);
                
                if (!sudahAdaPresensi) {
                    // Jika belum ada presensi, reset status dan keterangan untuk setiap siswa
                    for (Siswa siswa : daftarSiswa) {
                        siswa.setStatus(null);
                        siswa.setKeterangan(null);
                    }
                    
                    // Refresh tabel
                    siswaTableView.refresh();
                } else {
                    // Jika sudah ada presensi, ambil data presensi dari database
                    List<Presensi> presensiList = presensiController.cariPresensiByKelasAndTanggal(
                        guru.getKelasYangDiajar(), 
                        newValue,
                        guru
                    );
                    
                    // Update status dan keterangan siswa sesuai data presensi
                    for (Siswa siswa : daftarSiswa) {
                        for (Presensi presensi : presensiList) {
                            if (presensi.getSiswa().getId() == siswa.getId()) {
                                siswa.setStatus(presensi.getStatus());
                                siswa.setKeterangan(presensi.getKeterangan());
                                break;
                            }
                        }
                    }
                    
                    // Refresh tabel
                    siswaTableView.refresh();
                }
            }
        });

        // Daftar Siswa
        daftarSiswa = siswaController.daftarSiswaByKelas(guru.getKelasYangDiajar());

        siswaTableView = new TableView<>();
        siswaTableView.setStyle(
            "-fx-background-color: white;" +
            "-fx-control-inner-background: white;" +
            "-fx-selection-bar: #e0e0e0;"
        );

        // Kolom Nama
        TableColumn<Siswa, String> namaColumn = new TableColumn<>("Nama");
        namaColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNama())
        );
        namaColumn.setPrefWidth(200);

        // Kolom Status
        TableColumn<Siswa, Presensi.StatusPresensi> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(Presensi.StatusPresensi.values()));
        statusColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(
                cellData.getValue().getStatus() != null ? 
                cellData.getValue().getStatus() : 
                Presensi.StatusPresensi.HADIR
            )
        );
        statusColumn.setOnEditCommit(event -> {
            Siswa siswa = event.getRowValue();
            siswa.setStatus(event.getNewValue());
            siswaTableView.refresh();
        });
        statusColumn.setPrefWidth(150);

        // Kolom Keterangan
        TableColumn<Siswa, String> keteranganColumn = new TableColumn<>("Keterangan");
        keteranganColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        keteranganColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getKeterangan() != null ? 
                cellData.getValue().getKeterangan() : 
                ""
            )
        );
        keteranganColumn.setOnEditCommit(event -> {
            Siswa siswa = event.getRowValue();
            siswa.setKeterangan(event.getNewValue());
            siswaTableView.refresh();
        });
        keteranganColumn.setPrefWidth(200);

        // Tambahkan kolom ke tabel
        siswaTableView.getColumns().setAll(namaColumn, statusColumn, keteranganColumn);
        siswaTableView.getItems().setAll(daftarSiswa);
        siswaTableView.setEditable(true);
        siswaTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Tombol Simpan Presensi
        Button simpanPresensiButton = new Button("Simpan Presensi");
        simpanPresensiButton.setStyle(
            "-fx-background-color: #2ecc71; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 12px 24px; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 2);"
        );
        simpanPresensiButton.setOnAction(__ -> {
            LocalDate tanggal = tanggalPicker.getValue();
            if (tanggal == null) {
                showAlert("Harap pilih tanggal presensi!");
                return;
            }

            // Cek apakah sudah ada presensi untuk tanggal ini
            boolean sudahAdaPresensi = presensiController.cekPresensiSudahAda(guru.getKelasYangDiajar(), tanggal);
            if (sudahAdaPresensi) {
                showAlert("Presensi untuk kelas " + guru.getKelasYangDiajar() + " pada tanggal " + tanggal + " sudah pernah diinput!");
                return;
            }

            int berhasilDitambahkan = 0;
            int gagalDitambahkan = 0;

            for (Siswa siswa : daftarSiswa) {
                Presensi.StatusPresensi status = siswa.getStatus() != null ? 
                    siswa.getStatus() : Presensi.StatusPresensi.HADIR;
                String keterangan = siswa.getKeterangan() != null ? 
                    siswa.getKeterangan() : "";

                Presensi presensi = new Presensi(
                    0, 
                    siswa, 
                    tanggal, 
                    status, 
                    guru,
                    keterangan
                );

                boolean berhasilInput = presensiController.inputPresensi(presensi);
                if (berhasilInput) {
                    berhasilDitambahkan++;
                } else {
                    gagalDitambahkan++;
                }
            }

            if (gagalDitambahkan == 0) {
                showAlert("Presensi berhasil disimpan untuk " + berhasilDitambahkan + " siswa!");
                
                // Reset status dan keterangan untuk setiap siswa
                for (Siswa siswa : daftarSiswa) {
                    siswa.setStatus(null);
                    siswa.setKeterangan(null);
                }
                
                // Refresh tabel
                siswaTableView.refresh();
                
                // Reset tanggal ke hari ini
                tanggalPicker.setValue(LocalDate.now());
            } else {
                showAlert("Presensi sebagian berhasil:\n" +
                          "Berhasil: " + berhasilDitambahkan + "\n" +
                          "Gagal: " + gagalDitambahkan);
            }
        });

        // Tombol Kembali
        Button kembaliButton = new Button("Kembali");
        kembaliButton.setStyle(
            "-fx-background-color: #e74c3c; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 12px 24px; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 2);"
        );
        kembaliButton.setOnAction(__ -> guruDashboardView.kembaliKeDashboard());

        // Tambahkan komponen ke form container
        formContainer.getChildren().addAll(
            tanggalLabel, 
            tanggalPicker, 
            siswaTableView, 
            simpanPresensiButton, 
            kembaliButton
        );

        // Tambahkan semua komponen ke layout utama
        mainLayout.getChildren().addAll(titleLabel, formContainer);

        Scene scene = new Scene(mainLayout, 800, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi Presensi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
