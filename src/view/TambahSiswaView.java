package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Siswa;
import controller.SiswaController;
import model.Admin;

public class TambahSiswaView {
    private Stage primaryStage;
    private AdminDashboardView adminDashboardView;
    private SiswaController siswaController;
    private Admin admin;

    public TambahSiswaView(Stage primaryStage, AdminDashboardView adminDashboardView) {
        this.primaryStage = primaryStage;
        this.adminDashboardView = adminDashboardView;
        this.siswaController = new SiswaController();
        initUI();
    }

    public TambahSiswaView(Stage primaryStage, AdminDashboardView adminDashboardView, Admin admin) {
        this.primaryStage = primaryStage;
        this.adminDashboardView = adminDashboardView;
        this.admin = admin;
        this.siswaController = new SiswaController();
        initUI();
    }

    private void initUI() {
        primaryStage.setTitle("SIPRESIS - Tambah Siswa Baru");

        // Layout utama dengan gradien dan padding
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f4f8, #e0e8f0);");
        mainLayout.setAlignment(Pos.CENTER);

        // Judul halaman
        Label titleLabel = new Label("Tambah Siswa Baru");
        titleLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Grid untuk input
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setAlignment(Pos.CENTER);

        // Gaya input field yang konsisten
        String inputStyle = 
            "-fx-background-color: white; " +
            "-fx-background-radius: 5; " +
            "-fx-border-color: #bdc3c7; " +
            "-fx-border-radius: 5; " +
            "-fx-padding: 10px; " +
            "-fx-font-size: 14px;";

        // Label dan input field
        Label nisLabel = new Label("NIS:");
        nisLabel.setStyle("-fx-text-fill: #34495e; -fx-font-weight: bold;");
        TextField nisField = new TextField();
        nisField.setStyle(inputStyle);
        nisField.setPromptText("Masukkan NIS Siswa");

        Label namaLabel = new Label("Nama Lengkap:");
        namaLabel.setStyle("-fx-text-fill: #34495e; -fx-font-weight: bold;");
        TextField namaField = new TextField();
        namaField.setStyle(inputStyle);
        namaField.setPromptText("Masukkan Nama Lengkap");

        Label kelasLabel = new Label("Kelas:");
        kelasLabel.setStyle("-fx-text-fill: #34495e; -fx-font-weight: bold;");
        ComboBox<String> kelasComboBox = new ComboBox<>();
        kelasComboBox.getItems().addAll("X-A", "X-B", "XI-A", "XI-B", "XII-A", "XII-B");
        kelasComboBox.setStyle(
            inputStyle + 
            "-fx-background-color: white; " +
            "-fx-background-radius: 5;"
        );
        kelasComboBox.setPromptText("Pilih Kelas");

        Label jenisKelaminLabel = new Label("Jenis Kelamin:");
        jenisKelaminLabel.setStyle("-fx-text-fill: #34495e; -fx-font-weight: bold;");
        ComboBox<String> jenisKelaminComboBox = new ComboBox<>();
        jenisKelaminComboBox.getItems().addAll("Laki-laki", "Perempuan");
        jenisKelaminComboBox.setStyle(
            inputStyle + 
            "-fx-background-color: white; " +
            "-fx-background-radius: 5;"
        );
        jenisKelaminComboBox.setPromptText("Pilih Jenis Kelamin");

        // Tambahkan komponen ke grid
        formGrid.add(nisLabel, 0, 0);
        formGrid.add(nisField, 1, 0);
        formGrid.add(namaLabel, 0, 1);
        formGrid.add(namaField, 1, 1);
        formGrid.add(kelasLabel, 0, 2);
        formGrid.add(kelasComboBox, 1, 2);
        formGrid.add(jenisKelaminLabel, 0, 3);
        formGrid.add(jenisKelaminComboBox, 1, 3);

        // Tombol dengan gaya modern
        Button tambahButton = new Button("Tambah Siswa");
        tambahButton.setStyle(
            "-fx-background-color: #2ecc71; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 12px 24px; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 2);"
        );
        tambahButton.setOnAction(e -> {
            String nis = nisField.getText();
            String nama = namaField.getText();
            String kelas = kelasComboBox.getValue();
            String jenisKelamin = jenisKelaminComboBox.getValue();

            if (nis.isEmpty() || nama.isEmpty() || kelas == null || jenisKelamin == null) {
                showAlert("Harap isi semua field!");
                return;
            }

            Siswa siswa = new Siswa(nis, nama, kelas, jenisKelamin, jenisKelamin);
            boolean berhasil = siswaController.tambahSiswa(siswa);

            if (berhasil) {
                showAlert("Siswa berhasil ditambahkan!");
                nisField.clear();
                namaField.clear();
                kelasComboBox.setValue(null);
                jenisKelaminComboBox.setValue(null);
            } else {
                showAlert("Gagal menambahkan siswa. Periksa kembali data.");
            }
        });

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
        kembaliButton.setOnAction(e -> {
            // Buat ulang dashboard admin dengan admin yang sama
            new AdminDashboardView(primaryStage, admin);
        });

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(tambahButton, kembaliButton);

        // Tambahkan semua komponen ke layout utama
        mainLayout.getChildren().addAll(
            titleLabel, 
            formGrid, 
            buttonBox
        );

        Scene scene = new Scene(mainLayout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Metode untuk menampilkan alert
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SIPRESIS");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle(
            "-fx-font-size: 14px; " +
            "-fx-font-family: Arial;"
        );
        alert.showAndWait();
    }
}
