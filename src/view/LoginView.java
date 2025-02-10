package view;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import controller.UserController;

public class LoginView {
    private Stage primaryStage;
    private UserController userController;
    private Scene loginScene;

    public LoginView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userController = new UserController();
        initUI();
    }

    private void initUI() {
        primaryStage.setTitle("SIPRESIS - Login");

        // Layout utama
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f4f8, #e0e8f0);");

        // Judul
        Label titleLabel = new Label("SIPRESIS");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Subtitle
        Label subtitleLabel = new Label("Sistem Presensi Siswa");
        subtitleLabel.setFont(Font.font("Arial", 18));
        subtitleLabel.setStyle("-fx-text-fill: #34495e;");

        // Grid untuk login form
        GridPane loginGrid = new GridPane();
        loginGrid.setHgap(10);
        loginGrid.setVgap(15);
        loginGrid.setAlignment(Pos.CENTER);

        // Username
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-text-fill: #2c3e50;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Masukkan username");
        usernameField.setStyle("-fx-background-radius: 5; -fx-padding: 8px;");
        loginGrid.add(usernameLabel, 0, 0);
        loginGrid.add(usernameField, 1, 0);

        // Password
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-text-fill: #2c3e50;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Masukkan password");
        passwordField.setStyle("-fx-background-radius: 5; -fx-padding: 8px;");
        loginGrid.add(passwordLabel, 0, 1);
        loginGrid.add(passwordField, 1, 1);

        // Role
        Label roleLabel = new Label("Role:");
        roleLabel.setStyle("-fx-text-fill: #2c3e50;");
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Admin", "Guru");
        roleComboBox.setPromptText("Pilih Role");
        roleComboBox.setStyle("-fx-background-radius: 5; -fx-padding: 8px;");
        loginGrid.add(roleLabel, 0, 2);
        loginGrid.add(roleComboBox, 1, 2);

        // Tombol Login
        Button loginButton = new Button("Login");
        loginButton.setStyle(
            "-fx-background-color: #3498db; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 10px 20px; " +
            "-fx-font-weight: bold;"
        );
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleComboBox.getValue();

            if (username.isEmpty() || password.isEmpty() || role == null) {
                showAlert("Harap isi semua field!");
                return;
            }

            // Debug logging
            // System.out.println("Login attempt: username=" + username + ", role=" + role);
            // System.out.println("Input password: " + password);

            Object user = userController.login(username, password, role);
            if (user != null) {
                // Debug logging
                // System.out.println("Login berhasil: username=" + username + ", role=" + role);

                if ("Admin".equals(role)) {
                    new AdminDashboardView(primaryStage, (model.Admin) user);
                } else {
                    new GuruDashboardView(primaryStage, (model.Guru) user);
                }
            } else {
                // Debug logging
                // System.out.println("Login gagal: username=" + username + ", role=" + role);
                showAlert("Login gagal. Periksa kembali username, password, dan role.");
            }
        });

        GridPane.setHalignment(loginButton, HPos.CENTER);
        loginGrid.add(loginButton, 1, 3);

        // Tambahkan semua komponen ke layout utama
        mainLayout.getChildren().addAll(titleLabel, subtitleLabel, loginGrid);

        loginScene = new Scene(mainLayout, 500, 600);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void showAlert(String pesan) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi Login");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }

    public Scene getScene() {
        return loginScene;
    }
}
