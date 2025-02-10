package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Guru;
import model.User;
import controller.PresensiController;
import controller.SiswaController;

public class GuruDashboardView {
    private Stage primaryStage;
    private Guru guruUser;
    private SiswaController siswaController;
    private PresensiController presensiController;

    public GuruDashboardView(Stage primaryStage, User guruUser) {
        this.primaryStage = primaryStage;
        this.guruUser = (Guru) guruUser;
        this.siswaController = new SiswaController();
        this.presensiController = new PresensiController();
        initUI();
    }

    private void initUI() {
        primaryStage.setTitle("SIPRESIS - Dashboard Guru");

        // Layout utama dengan gradien dan padding
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f4f8, #e0e8f0);");
        mainLayout.setAlignment(Pos.CENTER);

        // Judul dan informasi guru
        Label titleLabel = new Label("Dashboard Guru");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Label welcomeLabel = new Label("Selamat Datang, " + guruUser.getNama());
        welcomeLabel.setFont(Font.font("Arial", 18));
        welcomeLabel.setStyle("-fx-text-fill: #34495e;");

        Label kelasLabel = new Label("Kelas yang Diajar: " + guruUser.getKelasYangDiajar());
        kelasLabel.setFont(Font.font("Arial", 16));
        kelasLabel.setStyle("-fx-text-fill: #34495e;");

        // Container untuk tombol dengan gaya modern
        VBox buttonContainer = new VBox(15);
        buttonContainer.setAlignment(Pos.CENTER);

        // Gaya tombol yang konsisten
        String buttonStyle = 
            "-fx-background-color: #3498db; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 12px 24px; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 2);";

        // Tombol Input Presensi
        Button inputPresensiBtn = new Button("Input Presensi");
        inputPresensiBtn.setStyle(buttonStyle);
        inputPresensiBtn.setOnAction(e -> new InputPresensiView(primaryStage, this, guruUser));

        // Tombol Lihat Kehadiran
        Button lihatKehadiranBtn = new Button("Lihat Kehadiran");
        lihatKehadiranBtn.setStyle(buttonStyle);
        lihatKehadiranBtn.setOnAction(e -> new LihatKehadiranView(primaryStage, this, guruUser));

        // Tombol Logout
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle(
            "-fx-background-color: #e74c3c; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 12px 24px; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 2);"
        );
        logoutBtn.setOnAction(e -> {
            LoginView loginView = new LoginView(primaryStage);
            primaryStage.setScene(loginView.getScene());
        });

        // Tambahkan tombol ke container
        buttonContainer.getChildren().addAll(
            inputPresensiBtn, 
            lihatKehadiranBtn, 
            logoutBtn
        );

        // Tambahkan semua komponen ke layout utama
        mainLayout.getChildren().addAll(
            titleLabel, 
            welcomeLabel, 
            kelasLabel, 
            buttonContainer
        );

        Scene scene = new Scene(mainLayout, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void kembaliKeDashboard() {
        initUI();
    }

    public Guru getGuruUser() {
        return guruUser;
    }
}
