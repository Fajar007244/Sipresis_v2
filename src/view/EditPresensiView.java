package view;

import controller.PresensiController;
import controller.SiswaController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Admin;
import model.Presensi;
import model.Siswa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class EditPresensiView {
    private Stage primaryStage;
    private AdminDashboardView adminDashboardView;
    private Admin adminUser;
    private PresensiController presensiController;
    private SiswaController siswaController;

    public EditPresensiView(Stage primaryStage, AdminDashboardView adminDashboardView, Admin adminUser) {
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

        Label titleLabel = new Label("Edit Data Kehadiran");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Pilih Siswa
        Label siswaLabel = new Label("Pilih Siswa:");
        ComboBox<Siswa> siswaComboBox = new ComboBox<>();
        List<Siswa> daftarSiswa = siswaController.daftarSiswa();
        siswaComboBox.getItems().addAll(daftarSiswa);
        siswaComboBox.setPromptText("Pilih Siswa");

        // Pilih Tanggal
        Label tanggalLabel = new Label("Pilih Tanggal:");
        DatePicker tanggalPicker = new DatePicker();
        tanggalPicker.setPromptText("Pilih Tanggal");

        // Tabel Presensi
        TableView<Presensi> presensiTableView = new TableView<>();
        setupPresensiTableView(presensiTableView);

        // Tombol Cari
        Button cariButton = new Button("Cari Presensi");
        cariButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        cariButton.setOnAction(e -> {
            Siswa selectedSiswa = siswaComboBox.getValue();
            LocalDate selectedDate = tanggalPicker.getValue();

            if (selectedSiswa == null || selectedDate == null) {
                showAlert("Harap pilih siswa dan tanggal!");
                return;
            }

            List<Presensi> daftarPresensi = presensiController.cariPresensi(selectedSiswa.getId(), selectedDate);
            presensiTableView.getItems().setAll(daftarPresensi);
        });

        // Tombol Edit
        Button editButton = new Button("Edit Presensi");
        editButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        editButton.setOnAction(e -> {
            Presensi selectedPresensi = presensiTableView.getSelectionModel().getSelectedItem();
            if (selectedPresensi == null) {
                showAlert("Pilih presensi yang akan diedit!");
                return;
            }
            tampilkanDialogEditPresensi(selectedPresensi);
        });

        // Tombol Kembali
        Button kembaliButton = new Button("Kembali");
        kembaliButton.setOnAction(e -> {
            primaryStage.setScene(adminDashboardView.createDashboardScene());
        });

        HBox buttonBox = new HBox(20, cariButton, editButton, kembaliButton);
        buttonBox.setAlignment(Pos.CENTER);

        mainLayout.getChildren().addAll(
            titleLabel, 
            new HBox(20, siswaLabel, siswaComboBox, tanggalLabel, tanggalPicker),
            presensiTableView,
            buttonBox
        );

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Edit Data Kehadiran");
        primaryStage.show();
    }

    private void setupPresensiTableView(TableView<Presensi> tableView) {
        TableColumn<Presensi, String> namaColumn = new TableColumn<>("Nama Siswa");
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("namaSiswa"));

        TableColumn<Presensi, LocalDate> tanggalColumn = new TableColumn<>("Tanggal");
        tanggalColumn.setCellValueFactory(new PropertyValueFactory<>("tanggal"));

        TableColumn<Presensi, String> statusColumn = new TableColumn<>("Status Kehadiran");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusKehadiran"));

        TableColumn<Presensi, String> keteranganColumn = new TableColumn<>("Keterangan");
        keteranganColumn.setCellValueFactory(new PropertyValueFactory<>("keterangan"));

        tableView.getColumns().setAll(namaColumn, tanggalColumn, statusColumn, keteranganColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void tampilkanDialogEditPresensi(Presensi presensi) {
        Dialog<Presensi> dialog = new Dialog<>();
        dialog.setTitle("Edit Presensi");
        dialog.setHeaderText("Edit Data Kehadiran Siswa");

        // Set tombol OK dan Batal
        ButtonType simpanButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(simpanButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Status Kehadiran
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Hadir", "Izin", "Sakit", "Alfa");
        statusComboBox.setValue(presensi.getStatusKehadiran());

        // Keterangan
        TextArea keteranganArea = new TextArea(presensi.getKeterangan());
        keteranganArea.setPromptText("Keterangan (opsional)");
        keteranganArea.setPrefRowCount(3);

        grid.add(new Label("Status Kehadiran:"), 0, 0);
        grid.add(statusComboBox, 1, 0);
        grid.add(new Label("Keterangan:"), 0, 1);
        grid.add(keteranganArea, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Konversi hasil dialog ke Presensi
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == simpanButtonType) {
                presensi.setStatusKehadiran(statusComboBox.getValue());
                presensi.setKeterangan(keteranganArea.getText());
                return presensi;
            }
            return null;
        });

        Optional<Presensi> result = dialog.showAndWait();
        result.ifPresent(editedPresensi -> {
            boolean berhasil = presensiController.updatePresensi(editedPresensi);
            if (berhasil) {
                showAlert("Berhasil mengupdate presensi!");
            } else {
                showAlert("Gagal mengupdate presensi!");
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
