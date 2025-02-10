package view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Admin;
import model.Siswa;
import controller.SiswaController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DaftarSiswaView {
    private Stage primaryStage;
    private AdminDashboardView adminDashboardView;
    private SiswaController siswaController;
    private Admin admin;

    public DaftarSiswaView(Stage primaryStage, Admin admin) {
        this.primaryStage = primaryStage;
        this.admin = admin;
        this.adminDashboardView = new AdminDashboardView(primaryStage, admin);
        this.siswaController = new SiswaController();
        initUI();
    }

    private void initUI() {
        primaryStage.setTitle("SIPRESIS - Daftar Siswa");

        // Layout utama dengan gradien dan padding
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f4f8, #e0e8f0);");
        mainLayout.setAlignment(Pos.CENTER);

        // Judul halaman
        Label titleLabel = new Label("Daftar Siswa");
        titleLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Tombol kembali
        Button backButton = new Button("Kembali ke Dashboard");
        backButton.setStyle(
            "-fx-background-color: #3498db; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 10 20; " +
            "-fx-font-weight: bold;"
        );
        backButton.setOnAction(e -> kembaliKeDashboard());

        // Tab untuk mengelompokkan siswa berdasarkan kelas
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: transparent;");

        // Ambil daftar siswa dan kelompokkan berdasarkan kelas
        List<Siswa> semuaSiswa = siswaController.daftarSiswa();
        Map<String, List<Siswa>> siswaByKelas = semuaSiswa.stream()
            .collect(Collectors.groupingBy(Siswa::getKelas));

        // Buat tab untuk setiap kelas
        for (Map.Entry<String, List<Siswa>> entry : siswaByKelas.entrySet()) {
            Tab kelasTab = new Tab(entry.getKey());
            kelasTab.setClosable(false);

            TableView<Siswa> tableView = createSiswaTableView(entry.getValue());
            kelasTab.setContent(tableView);
            tabPane.getTabs().add(kelasTab);
        }

        // Tambahkan elemen ke layout utama
        mainLayout.getChildren().addAll(titleLabel, backButton, tabPane);

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<Siswa> createSiswaTableView(List<Siswa> siswaList) {
        TableView<Siswa> tableView = new TableView<>();
        ObservableList<Siswa> observableSiswaList = FXCollections.observableArrayList(siswaList);
        tableView.setItems(observableSiswaList);

        // Kolom NIS
        TableColumn<Siswa, String> nisColumn = new TableColumn<>("NIS");
        nisColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNis()));

        // Kolom Nama Siswa
        TableColumn<Siswa, String> namaColumn = new TableColumn<>("Nama Siswa");
        namaColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNama()));

        // Kolom Kelas
        TableColumn<Siswa, String> kelasColumn = new TableColumn<>("Kelas");
        kelasColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKelas()));

        // Kolom Jenis Kelamin
        TableColumn<Siswa, String> jenisKelaminColumn = new TableColumn<>("Jenis Kelamin");
        jenisKelaminColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getJenisKelamin()));

        // Kolom Tahun Ajaran
        TableColumn<Siswa, String> tahunAjaranColumn = new TableColumn<>("Tahun Ajaran");
        tahunAjaranColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTahunAjaran()));

        // Kolom Aksi (Edit dan Hapus)
        TableColumn<Siswa, Void> aksiColumn = new TableColumn<>("Aksi");
        aksiColumn.setCellFactory(param -> new TableCell<Siswa, Void>() {
            private final HBox actionButtons = new HBox(10);
            private final Button editButton = new Button("Edit");
            private final Button hapusButton = new Button("Hapus");

            {
                editButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
                hapusButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    Siswa siswa = getTableView().getItems().get(getIndex());
                    tampilkanFormEditSiswa(siswa);
                });

                hapusButton.setOnAction(event -> {
                    Siswa siswa = getTableView().getItems().get(getIndex());
                    konfirmasiHapusSiswa(siswa);
                });

                actionButtons.getChildren().addAll(editButton, hapusButton);
                actionButtons.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionButtons);
                }
            }
        });

        // Atur gaya tabel
        tableView.setStyle("-fx-background-color: white; -fx-control-inner-background: white;");
        tableView.getColumns().setAll(nisColumn, namaColumn, kelasColumn, jenisKelaminColumn, tahunAjaranColumn, aksiColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        return tableView;
    }

    private void tampilkanFormEditSiswa(Siswa siswa) {
        Stage editStage = new Stage();
        editStage.setTitle("Edit Siswa");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField nisField = new TextField(siswa.getNis());
        nisField.setPromptText("NIS");
        TextField namaField = new TextField(siswa.getNama());
        namaField.setPromptText("Nama Siswa");
        TextField kelasField = new TextField(siswa.getKelas());
        kelasField.setPromptText("Kelas");
        ComboBox<String> jenisKelaminCombo = new ComboBox<>();
        jenisKelaminCombo.getItems().addAll("Laki-laki", "Perempuan");
        jenisKelaminCombo.setValue(siswa.getJenisKelamin());
        TextField tahunAjaranField = new TextField(siswa.getTahunAjaran());
        tahunAjaranField.setPromptText("Tahun Ajaran");

        Button simpanButton = new Button("Simpan");
        simpanButton.setOnAction(e -> {
            siswa.setNis(nisField.getText());
            siswa.setNama(namaField.getText());
            siswa.setKelas(kelasField.getText());
            siswa.setJenisKelamin(jenisKelaminCombo.getValue());
            siswa.setTahunAjaran(tahunAjaranField.getText());

            boolean berhasil = siswaController.updateSiswa(siswa);
            if (berhasil) {
                showAlert("Berhasil mengupdate siswa!");
                editStage.close();
                initUI(); // Refresh view
            } else {
                showAlert("Gagal mengupdate siswa!");
            }
        });

        layout.getChildren().addAll(
            new Label("NIS:"), nisField,
            new Label("Nama Siswa:"), namaField,
            new Label("Kelas:"), kelasField,
            new Label("Jenis Kelamin:"), jenisKelaminCombo,
            new Label("Tahun Ajaran:"), tahunAjaranField,
            simpanButton
        );

        Scene scene = new Scene(layout, 300, 450);
        editStage.setScene(scene);
        editStage.show();
    }

    private void konfirmasiHapusSiswa(Siswa siswa) {
        Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION);
        konfirmasi.setTitle("Konfirmasi Hapus Siswa");
        konfirmasi.setHeaderText("Anda yakin ingin menghapus siswa " + siswa.getNama() + "?");
        konfirmasi.setContentText("Tindakan ini tidak dapat dibatalkan.");

        Optional<ButtonType> result = konfirmasi.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean berhasil = siswaController.hapusSiswa(siswa.getId());
            if (berhasil) {
                showAlert("Berhasil menghapus siswa!");
                initUI(); // Refresh view
            } else {
                showAlert("Gagal menghapus siswa!");
            }
        }
    }

    private void showAlert(String pesan) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }

    private void kembaliKeDashboard() {
        AdminDashboardView dashboardView = new AdminDashboardView(primaryStage, admin);
        Scene dashboardScene = dashboardView.createDashboardScene();
        primaryStage.setScene(dashboardScene);
    }
}
