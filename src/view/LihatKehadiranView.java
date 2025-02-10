package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Guru;
import model.Presensi;
import model.Siswa;
import controller.PresensiController;
import controller.SiswaController;

import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class LihatKehadiranView {
    private Stage primaryStage;
    private GuruDashboardView guruDashboardView;
    private Guru guruUser;
    private PresensiController presensiController;

    public LihatKehadiranView(Stage primaryStage, GuruDashboardView guruDashboardView, Guru guruUser) {
        this.primaryStage = primaryStage;
        this.guruDashboardView = guruDashboardView;
        this.guruUser = guruUser;
        this.presensiController = new PresensiController();
        initUI();
    }

    private void initUI() {
        primaryStage.setTitle("SIPRESIS - Lihat Kehadiran");

        // Layout utama dengan gradient background
        VBox mainLayout = new VBox(20);
        mainLayout.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #3498db, #2c3e50);" +
            "-fx-padding: 30px;"
        );
        mainLayout.setAlignment(Pos.CENTER);

        // Judul
        Label titleLabel = new Label("Lihat Kehadiran Siswa");
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

        // Label dengan gaya modern
        String labelStyle = "-fx-text-fill: white; -fx-font-weight: bold;";

        // Pilih Kelas
        Label kelasLabel = new Label("Kelas: " + guruUser.getKelasYangDiajar());
        kelasLabel.setStyle(labelStyle);

        // Tanggal Mulai
        Label tanggalLabel = new Label("Pilih Tanggal:");
        tanggalLabel.setStyle(labelStyle);
        DatePicker tanggalPicker = new DatePicker();
        tanggalPicker.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.2);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 10px;"
        );

        // Tombol Tampilkan
        Button tampilkanButton = new Button("Tampilkan Kehadiran");
        tampilkanButton.setStyle(
            "-fx-background-color: #2ecc71; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 12px 24px; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 2);"
        );
        tampilkanButton.setOnAction(event -> {
            LocalDate tanggal = tanggalPicker.getValue();

            if (tanggal == null) {
                showAlert("Harap pilih tanggal!");
                return;
            }

            List<Presensi> presensiList = presensiController.laporanPresensiKelas(guruUser.getKelasYangDiajar(), tanggal);
            tampilkanLaporan(presensiList, tanggal);
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
        kembaliButton.setOnAction(__ -> {
            guruDashboardView.kembaliKeDashboard();
        });

        // Tambahkan komponen ke form container
        formContainer.getChildren().addAll(
            kelasLabel, 
            tanggalLabel, 
            tanggalPicker, 
            tampilkanButton,
            kembaliButton
        );

        // Tambahkan semua komponen ke layout utama
        mainLayout.getChildren().addAll(titleLabel, formContainer);

        Scene scene = new Scene(mainLayout, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void tampilkanLaporan(List<Presensi> presensiList, LocalDate tanggal) {
        Stage laporanStage = new Stage();
        laporanStage.setTitle("Laporan Kehadiran " + tanggal);

        // Buat TableView untuk menampilkan laporan dengan detail lebih lengkap
        TableView<Presensi> tableView = new TableView<>();
        tableView.setStyle(
            "-fx-background-color: white;" +
            "-fx-control-inner-background: white;" +
            "-fx-selection-bar: #e0e0e0;"
        );

        // Kolom Nama Siswa
        TableColumn<Presensi, String> namaColumn = new TableColumn<>("Nama Siswa");
        namaColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getSiswa().getNama())
        );
        namaColumn.setPrefWidth(200);

        // Kolom Status
        TableColumn<Presensi, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStatus().toString())
        );
        statusColumn.setPrefWidth(100);

        // Kolom Keterangan
        TableColumn<Presensi, String> keteranganColumn = new TableColumn<>("Keterangan");
        keteranganColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(
                cellData.getValue().getKeterangan() != null ? 
                cellData.getValue().getKeterangan() : 
                "-"
            )
        );
        keteranganColumn.setPrefWidth(250);

        // Tambahkan kolom ke tabel
        tableView.getColumns().setAll(namaColumn, statusColumn, keteranganColumn);

        // Tambahkan data ke tabel
        if (presensiList.isEmpty()) {
            Label emptyLabel = new Label("Tidak ada data kehadiran untuk tanggal " + tanggal);
            emptyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
            
            VBox emptyContainer = new VBox(emptyLabel);
            emptyContainer.setAlignment(Pos.CENTER);
            emptyContainer.setPadding(new Insets(20));
            
            Scene emptyScene = new Scene(emptyContainer, 400, 200);
            laporanStage.setScene(emptyScene);
        } else {
            tableView.getItems().setAll(presensiList);

            // Hitung statistik
            int hadir = 0, izin = 0, sakit = 0, alfa = 0;
            for (Presensi p : presensiList) {
                switch (p.getStatus()) {
                    case HADIR: hadir++; break;
                    case IZIN: izin++; break;
                    case SAKIT: sakit++; break;
                    case ALFA: alfa++; break;
                }
            }

            // Buat layout untuk statistik
            VBox statistikContainer = new VBox(10);
            statistikContainer.setPadding(new Insets(10));
            statistikContainer.setStyle("-fx-background-color: #f0f4f8;");

            // Label statistik
            Label statistikLabel = new Label("Statistik Kehadiran:");
            statistikLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label hadirlabel = new Label(String.format("Hadir: %d", hadir));
            Label izinLabel = new Label(String.format("Izin: %d", izin));
            Label sakitLabel = new Label(String.format("Sakit: %d", sakit));
            Label alfaLabel = new Label(String.format("Alfa: %d", alfa));

            statistikContainer.getChildren().addAll(
                statistikLabel, 
                hadirlabel, 
                izinLabel, 
                sakitLabel, 
                alfaLabel
            );

            // Layout utama
            VBox mainLayout = new VBox(10);
            mainLayout.getChildren().addAll(tableView, statistikContainer);

            // Buat scene
            Scene laporanScene = new Scene(mainLayout, 600, 500);
            laporanStage.setScene(laporanScene);
        }

        laporanStage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
