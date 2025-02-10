package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Admin;
import model.Guru;
import model.User;
import controller.UserController;

public class TambahPenggunaView {
    private Stage primaryStage;
    private AdminDashboardView adminDashboardView;
    private UserController userController;
    private Admin admin;

    public TambahPenggunaView(Stage primaryStage, AdminDashboardView adminDashboardView) {
        this.primaryStage = primaryStage;
        this.adminDashboardView = adminDashboardView;
        this.userController = new UserController();
        initUI();
    }

    public TambahPenggunaView(Stage primaryStage, AdminDashboardView adminDashboardView, Admin admin) {
        this.primaryStage = primaryStage;
        this.adminDashboardView = adminDashboardView;
        this.admin = admin;
        this.userController = new UserController();
        initUI();
    }

    private void initUI() {
        primaryStage.setTitle("SIPRESIS - Tambah Pengguna");

        // Layout utama dengan gradient background
        VBox mainLayout = new VBox(20);
        mainLayout.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #3498db, #2c3e50);" +
            "-fx-padding: 30px;"
        );
        mainLayout.setAlignment(Pos.CENTER);

        // Judul
        Label titleLabel = new Label("Tambah Pengguna Baru");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: white;");

        // Grid untuk form
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 10;");
        grid.setPadding(new Insets(30));

        // Gaya input field
        String inputStyle = 
            "-fx-background-color: rgba(255, 255, 255, 0.2);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 10px;";

        // Label dengan gaya modern
        String labelStyle = "-fx-text-fill: white; -fx-font-weight: bold;";

        // Username
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle(labelStyle);
        TextField usernameField = new TextField();
        usernameField.setStyle(inputStyle);
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);

        // Password
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle(labelStyle);
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle(inputStyle);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);

        // Tipe Pengguna
        Label tipeLabel = new Label("Tipe Pengguna:");
        tipeLabel.setStyle(labelStyle);
        ComboBox<String> tipeComboBox = new ComboBox<>();
        tipeComboBox.getItems().addAll("Admin", "Guru");
        tipeComboBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.2);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 5;"
        );
        grid.add(tipeLabel, 0, 2);
        grid.add(tipeComboBox, 1, 2);

        // Tombol dengan gaya modern
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button tambahButton = new Button("Tambah Pengguna");
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
            String username = usernameField.getText();
            String password = passwordField.getText();
            String tipe = tipeComboBox.getValue();

            if (username.isEmpty() || password.isEmpty() || tipe == null) {
                showAlert("Harap isi semua field!");
                return;
            }

            User user;
            if (tipe.equals("Admin")) {
                user = new Admin(0, "Admin Baru", username, password);
            } else {
                user = new Guru(0, "Guru Baru", username, password, "Kelas Belum Ditentukan");
            }

            boolean berhasil = userController.tambahPengguna(user, password);

            if (berhasil) {
                showAlert("Pengguna berhasil ditambahkan!");
                new AdminDashboardView(primaryStage, admin);
            } else {
                showAlert("Gagal menambahkan pengguna!");
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

        buttonBox.getChildren().addAll(tambahButton, kembaliButton);

        // Tambahkan semua komponen ke layout utama
        mainLayout.getChildren().addAll(titleLabel, grid, buttonBox);

        Scene scene = new Scene(mainLayout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(String pesan) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }
}
