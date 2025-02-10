package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Admin;
import model.Presensi;
import model.Siswa;
import controller.PresensiController;
import controller.SiswaController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class LaporanAbsensiView {
    private Stage primaryStage;
    private Admin admin;
    private PresensiController presensiController;
    private SiswaController siswaController;

    public LaporanAbsensiView(Stage primaryStage, Admin admin) {
        this.primaryStage = primaryStage;
        this.admin = admin;
        initUI();
    }

    private void initUI() {
        this.presensiController = new PresensiController();
        this.siswaController = new SiswaController();

        primaryStage.setTitle("SIPRESIS - Laporan Absensi");

        // Layout utama dengan gradient background
        VBox mainLayout = new VBox(20);
        mainLayout.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #3498db, #2c3e50);" +
            "-fx-padding: 30px;"
        );
        mainLayout.setAlignment(Pos.CENTER);

        // Judul
        Label titleLabel = new Label("Laporan Absensi Siswa");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: white;");

        // Container untuk form
        VBox formContainer = new VBox(15);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.1);" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 30px;"
        );

        // Gaya input field
        String inputStyle = 
            "-fx-background-color: rgba(255, 255, 255, 0.2);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 10px;";

        // Label dengan gaya modern
        String labelStyle = "-fx-text-fill: white; -fx-font-weight: bold;";

        // Pilih Siswa
        Label siswaLabel = new Label("Pilih Siswa:");
        siswaLabel.setStyle(labelStyle);
        ComboBox<Siswa> siswaComboBox = new ComboBox<>();
        siswaComboBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.2);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 5;"
        );
        List<Siswa> daftarSiswa = siswaController.daftarSiswa();
        siswaComboBox.getItems().addAll(daftarSiswa);
        siswaComboBox.setPromptText("Pilih Siswa");

        // Tanggal Mulai
        Label startDateLabel = new Label("Tanggal Mulai:");
        startDateLabel.setStyle(labelStyle);
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setStyle(inputStyle);

        // Tanggal Selesai
        Label endDateLabel = new Label("Tanggal Selesai:");
        endDateLabel.setStyle(labelStyle);
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setStyle(inputStyle);

        // Tombol Tampilkan
        Button tampilkanButton = new Button("Tampilkan Laporan");
        tampilkanButton.setStyle(
            "-fx-background-color: #2ecc71; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 12px 24px; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 2);"
        );
        tampilkanButton.setOnAction(e -> {
            Siswa siswa = siswaComboBox.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (siswa == null || startDate == null || endDate == null) {
                showAlert("Harap pilih siswa dan rentang tanggal!");
                return;
            }

            List<Presensi> presensiList = presensiController.laporanPresensiSiswa(siswa, startDate, endDate);
            tampilkanLaporan(presensiList);
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
        kembaliButton.setOnAction(event -> {
            // Buat ulang dashboard admin dengan admin yang sama
            new AdminDashboardView(primaryStage, admin);
        });

        // Tambahkan komponen ke form container
        formContainer.getChildren().addAll(
            siswaLabel, 
            siswaComboBox, 
            startDateLabel, 
            startDatePicker, 
            endDateLabel, 
            endDatePicker, 
            tampilkanButton,
            kembaliButton
        );

        // Tambahkan semua komponen ke layout utama
        mainLayout.getChildren().addAll(titleLabel, formContainer);

        Scene scene = new Scene(mainLayout, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void tampilkanLaporan(List<Presensi> presensiList) {
        // Buat TableView untuk menampilkan laporan dengan detail lebih lengkap
        Stage laporanStage = new Stage();
        laporanStage.setTitle("Laporan Presensi Siswa");

        if (presensiList.isEmpty()) {
            showAlert("Tidak ada data presensi untuk rentang tanggal yang dipilih.");
            return;
        }

        TableView<Presensi> tableView = new TableView<>();
        ObservableList<Presensi> observablePresensiList = FXCollections.observableArrayList(presensiList);
        tableView.setItems(observablePresensiList);

        tableView.setStyle(
            "-fx-background-color: white;" +
            "-fx-control-inner-background: white;" +
            "-fx-selection-bar: #e0e0e0;"
        );

        // Kolom Tanggal
        TableColumn<Presensi, LocalDate> tanggalColumn = new TableColumn<>("Tanggal");
        tanggalColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTanggal())
        );
        tanggalColumn.setPrefWidth(120);

        // Kolom Status
        TableColumn<Presensi, Presensi.StatusPresensi> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getStatus())
        );
        statusColumn.setPrefWidth(100);

        // Kolom Keterangan
        TableColumn<Presensi, String> keteranganColumn = new TableColumn<>("Keterangan");
        keteranganColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getKeterangan() != null ? 
                cellData.getValue().getKeterangan() : 
                "-"
            )
        );
        keteranganColumn.setPrefWidth(200);

        // Kolom Pencatat
        TableColumn<Presensi, String> pencatatColumn = new TableColumn<>("Pencatat");
        pencatatColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getPencatat() != null ? 
                cellData.getValue().getPencatat().getNama() : 
                "-"
            )
        );
        pencatatColumn.setPrefWidth(150);

        tableView.getColumns().setAll(tanggalColumn, statusColumn, keteranganColumn, pencatatColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox layout = new VBox(10);
        layout.getChildren().add(tableView);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 600, 400);
        laporanStage.setScene(scene);
        laporanStage.show();
    }

    private void showAlert(String pesan) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }
}
