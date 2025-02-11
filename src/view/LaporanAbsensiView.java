package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Admin;
import model.Presensi;
import model.Siswa;
import controller.PresensiController;
import controller.SiswaController;

import java.time.LocalDate;
import java.util.List;

public class LaporanAbsensiView {
    private Stage primaryStage;
    private Admin adminUser;
    private PresensiController presensiController;
    private SiswaController siswaController;

    public LaporanAbsensiView(Stage primaryStage, Admin adminUser) {
        this.primaryStage = primaryStage;
        this.adminUser = adminUser;
        this.presensiController = new PresensiController();
        this.siswaController = new SiswaController();
        initUI();
    }

    private void initUI() {
        primaryStage.setTitle("SIPRESIS - Laporan Absensi");

        // Layout utama
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f4f8, #e0e8f0);");
        mainLayout.setAlignment(Pos.CENTER);

        // Judul
        Label titleLabel = new Label("Laporan Absensi Siswa");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Grid untuk input
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setAlignment(Pos.CENTER);

        // Dropdown Siswa
        Label siswaLabel = new Label("Pilih Siswa:");
        ComboBox<Siswa> siswaComboBox = new ComboBox<>();
        siswaComboBox.getItems().addAll(siswaController.daftarSiswa());
        siswaComboBox.setPromptText("Pilih Siswa");

        // Date Picker untuk rentang tanggal
        Label startDateLabel = new Label("Tanggal Mulai:");
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setValue(LocalDate.now().minusMonths(1)); // Default 1 bulan lalu

        Label endDateLabel = new Label("Tanggal Selesai:");
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setValue(LocalDate.now()); // Default hari ini

        // Tabel untuk menampilkan laporan
        TableView<Presensi> presensiTableView = new TableView<>();
        
        // Kolom untuk tabel
        TableColumn<Presensi, LocalDate> tanggalColumn = new TableColumn<>("Tanggal");
        tanggalColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTanggal())
        );

        TableColumn<Presensi, Presensi.StatusPresensi> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getStatus())
        );

        TableColumn<Presensi, String> keteranganColumn = new TableColumn<>("Keterangan");
        keteranganColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKeterangan())
        );

        TableColumn<Presensi, String> pencatatColumn = new TableColumn<>("Pencatat");
        pencatatColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPencatat().getNama())
        );

        presensiTableView.getColumns().addAll(
            tanggalColumn, statusColumn, keteranganColumn, pencatatColumn
        );

        // Tombol Tampilkan Laporan
        Button tampilkanButton = new Button("Tampilkan Laporan");
        tampilkanButton.setStyle(
            "-fx-background-color: #2ecc71; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold;"
        );
        tampilkanButton.setOnAction(e -> {
            Siswa siswa = siswaComboBox.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            // Validasi input
            if (siswa == null || startDate == null || endDate == null) {
                showAlert("Harap pilih siswa dan rentang tanggal!");
                return;
            }

            // Ambil laporan presensi
            List<Presensi> presensiList = presensiController.laporanPresensiSiswa(siswa, startDate, endDate);

            // Tampilkan di tabel
            if (presensiList.isEmpty()) {
                showAlert("Tidak ada data presensi untuk siswa dan rentang tanggal yang dipilih.");
            } else {
                presensiTableView.getItems().setAll(presensiList);
            }
        });

        // Tombol Kembali
        Button kembaliButton = new Button("Kembali");
        kembaliButton.setStyle(
            "-fx-background-color: #3498db; " +
            "-fx-text-fill: white;"
        );
        kembaliButton.setOnAction(e -> new AdminDashboardView(primaryStage, adminUser));

        // Tambahkan komponen ke grid
        inputGrid.addRow(0, siswaLabel, siswaComboBox);
        inputGrid.addRow(1, startDateLabel, startDatePicker);
        inputGrid.addRow(2, endDateLabel, endDatePicker);

        // Layout horizontal untuk tombol
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(tampilkanButton, kembaliButton);

        // Tambahkan semua komponen ke layout utama
        mainLayout.getChildren().addAll(
            titleLabel, 
            inputGrid, 
            buttonBox, 
            new Label("Rincian Laporan:"),
            presensiTableView
        );

        // Atur scene
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SIPRESIS");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
