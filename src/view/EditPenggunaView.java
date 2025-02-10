package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Admin;
import model.Guru;
import model.User;
import controller.UserController;

public class EditPenggunaView {
    private Stage primaryStage;
    private AdminDashboardView adminDashboardView;
    private UserController userController;
    private Admin admin;
    private User userYangDiedit;

    public EditPenggunaView(Stage primaryStage, AdminDashboardView adminDashboardView, Admin admin, User userYangDiedit) {
        this.primaryStage = primaryStage;
        this.adminDashboardView = adminDashboardView;
        this.admin = admin;
        this.userYangDiedit = userYangDiedit;
        this.userController = new UserController();
        initUI();
    }

    private void initUI() {
        primaryStage.setTitle("SIPRESIS - Edit Pengguna");

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f4f8, #e0e8f0);");
        mainLayout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Edit Pengguna");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField namaField = new TextField(userYangDiedit.getNama());
        namaField.setPromptText("Nama Lengkap");

        TextField usernameField = new TextField(userYangDiedit.getUsername());
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password Baru (Kosongkan jika tidak diubah)");

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Admin", "Guru");
        roleComboBox.setValue(userYangDiedit.getRole());
        roleComboBox.setPromptText("Role");

        TextField kelasField = null;

        if (userYangDiedit instanceof Guru) {
            Guru guru = (Guru) userYangDiedit;
            kelasField = new TextField(guru.getKelasYangDiajar());
            kelasField.setPromptText("Kelas yang Diajar");
        }

        final TextField finalKelasField = kelasField;

        Button simpanButton = new Button("Simpan Perubahan");
        simpanButton.setStyle(
            "-fx-background-color: #2ecc71; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold;"
        );
        simpanButton.setOnAction(e -> {
            String nama = namaField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleComboBox.getValue();

            // Validasi input
            if (nama.isEmpty() || username.isEmpty()) {
                showAlert("Nama dan Username tidak boleh kosong!");
                return;
            }

            // Update user
            userYangDiedit.setNama(nama);
            userYangDiedit.setUsername(username);

            // Jika password diisi, update password
            if (!password.isEmpty()) {
                userYangDiedit.setPassword(password);
            }

            // Update role khusus untuk Admin atau Guru
            if (userYangDiedit instanceof Admin) {
                ((Admin) userYangDiedit).setRole(role.toUpperCase());
            } else if (userYangDiedit instanceof Guru) {
                Guru guru = (Guru) userYangDiedit;
                guru.setRole(role.toUpperCase());
                
                // Update kelas yang diajar jika ada
                if (finalKelasField != null && !finalKelasField.getText().isEmpty()) {
                    guru.setKelasYangDiajar(finalKelasField.getText());
                }
            }

            // Simpan perubahan
            boolean berhasil = userController.updatePengguna(userYangDiedit, password);
            if (berhasil) {
                showAlert("Berhasil mengupdate pengguna!");
                kembaliKeDaftarPengguna();
            } else {
                showAlert("Gagal mengupdate pengguna!");
            }
        });

        Button kembaliButton = new Button("Kembali");
        kembaliButton.setStyle(
            "-fx-background-color: #e74c3c; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold;"
        );
        kembaliButton.setOnAction(e -> kembaliKeDaftarPengguna());

        // Tambahkan elemen ke layout
        VBox inputLayout = new VBox(10);
        inputLayout.getChildren().addAll(
            new Label("Nama:"), namaField,
            new Label("Username:"), usernameField,
            new Label("Password Baru:"), passwordField,
            new Label("Role:"), roleComboBox
        );

        // Tambahkan field kelas untuk Guru jika ada
        if (finalKelasField != null) {
            inputLayout.getChildren().addAll(
                new Label("Kelas yang Diajar:"), finalKelasField
            );
        }

        inputLayout.getChildren().addAll(
            simpanButton,
            kembaliButton
        );

        mainLayout.getChildren().addAll(titleLabel, inputLayout);

        Scene scene = new Scene(mainLayout, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void kembaliKeDaftarPengguna() {
        // Buat view daftar pengguna
        new DaftarPenggunaView(primaryStage, admin);
    }

    private void showAlert(String pesan) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }
}
