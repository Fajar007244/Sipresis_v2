package view;

import controller.PresensiController;
import controller.SiswaController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import model.Admin;
import model.Siswa;

import java.time.LocalDate;
import java.util.List;

public class TambahPresensiView {
    private Stage primaryStage;
    private AdminDashboardView adminDashboardView;
    private Admin adminUser;
    private PresensiController presensiController;
    private SiswaController siswaController;

    public TambahPresensiView(Stage primaryStage, AdminDashboardView adminDashboardView, Admin adminUser) {
        this.primaryStage = primaryStage;
        this.adminDashboardView = adminDashboardView;
        this.adminUser = adminUser;
        this.presensiController = new PresensiController();
        this.siswaController = new SiswaController();

        setupUI();
    }

    private void setupUI() {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f4f8, #e0e8f0);");

        Label titleLabel = new Label("Tambah Data Kehadiran");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Pilih Kelas
        Label kelasLabel = new Label("Pilih Kelas:");
        ComboBox<String> kelasComboBox = new ComboBox<>();
        kelasComboBox.getItems().addAll("X-A", "X-B", "XI-A", "XI-B", "XII-A", "XII-B");
        kelasComboBox.setPromptText("Pilih Kelas");

        // Pilih Tanggal
        Label tanggalLabel = new Label("Pilih Tanggal:");
        DatePicker tanggalPicker = new DatePicker(LocalDate.now());
        tanggalPicker.setPromptText("Pilih Tanggal");

        // Tabel Siswa untuk Presensi
        TableView<Siswa> siswaTableView = new TableView<>();
        setupSiswaTableView(siswaTableView);

        // Tombol Cari Siswa
        Button cariSiswaButton = new Button("Cari Siswa");
        cariSiswaButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        cariSiswaButton.setOnAction(e -> {
            String selectedKelas = kelasComboBox.getValue();
            if (selectedKelas == null) {
                showAlert("Harap pilih kelas!");
                return;
            }

            List<Siswa> daftarSiswa = siswaController.cariSiswaByKelas(selectedKelas);
            siswaTableView.getItems().setAll(daftarSiswa);
        });

        // Tombol Simpan Presensi
        Button simpanButton = new Button("Simpan Presensi");
        simpanButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        simpanButton.setOnAction(e -> simpanPresensi(siswaTableView, tanggalPicker));

        // Tombol Kembali
        Button kembaliButton = new Button("Kembali");
        kembaliButton.setOnAction(e -> {
            primaryStage.setScene(adminDashboardView.createDashboardScene());
        });

        HBox buttonBox = new HBox(20, cariSiswaButton, simpanButton, kembaliButton);
        buttonBox.setAlignment(Pos.CENTER);

        mainLayout.getChildren().addAll(
            titleLabel, 
            new HBox(20, kelasLabel, kelasComboBox, tanggalLabel, tanggalPicker),
            siswaTableView,
            buttonBox
        );

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tambah Data Kehadiran");
        primaryStage.show();
    }

    private void setupSiswaTableView(TableView<Siswa> tableView) {
        TableColumn<Siswa, String> nisColumn = new TableColumn<>("NIS");
        nisColumn.setCellValueFactory(new PropertyValueFactory<>("nis"));

        TableColumn<Siswa, String> namaColumn = new TableColumn<>("Nama Siswa");
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("nama"));

        TableColumn<Siswa, String> statusColumn = new TableColumn<>("Status Kehadiran");
        statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn("Hadir", "Izin", "Sakit", "Alfa"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusKehadiran"));
        statusColumn.setOnEditCommit(event -> {
            Siswa siswa = event.getRowValue();
            siswa.setStatusKehadiran(event.getNewValue());
        });

        TableColumn<Siswa, String> keteranganColumn = new TableColumn<>("Keterangan");
        keteranganColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        keteranganColumn.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        keteranganColumn.setOnEditCommit(event -> {
            Siswa siswa = event.getRowValue();
            siswa.setKeterangan(event.getNewValue());
        });

        tableView.getColumns().setAll(nisColumn, namaColumn, statusColumn, keteranganColumn);
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setColumnResizePolicy(param -> true);
    }

    private void simpanPresensi(TableView<Siswa> siswaTableView, DatePicker tanggalPicker) {
        LocalDate tanggal = tanggalPicker.getValue();
        if (tanggal == null) {
            showAlert("Harap pilih tanggal!");
            return;
        }

        List<Siswa> daftarSiswa = siswaTableView.getItems();
        int berhasilDitambahkan = 0;
        int gagalDitambahkan = 0;
        int sudahAdaDitambahkan = 0;

        for (Siswa siswa : daftarSiswa) {
            String statusKehadiran = siswa.getStatusKehadiran();
            String keterangan = siswa.getKeterangan();

            if (statusKehadiran == null) {
                statusKehadiran = "Alfa"; // Default jika tidak dipilih
            }

            boolean berhasil = presensiController.tambahPresensi(
                siswa.getId(), 
                tanggal, 
                statusKehadiran, 
                keterangan != null ? keterangan : "", 
                adminUser.getId()
            );

            if (berhasil) {
                berhasilDitambahkan++;
            } else {
                // Cek apakah sudah ada di database
                if (presensiController.cekPresensiSudahAda(siswa.getId(), tanggal)) {
                    sudahAdaDitambahkan++;
                } else {
                    gagalDitambahkan++;
                }
            }
        }

        showAlert(String.format(
            "Presensi Disimpan\nBerhasil: %d\nSudah Ada: %d\nGagal: %d", 
            berhasilDitambahkan, 
            sudahAdaDitambahkan,
            gagalDitambahkan
        ));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
