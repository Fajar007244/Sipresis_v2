package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Admin;
import model.Guru;
import model.User;
import controller.SiswaController;
import controller.UserController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.Arrays;
import java.util.stream.Collectors;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class AdminDashboardView {
    private Stage primaryStage;
    private Admin adminUser;
    private SiswaController siswaController;
    private UserController userController;

    public AdminDashboardView(Stage primaryStage, Admin adminUser) {
        this.primaryStage = primaryStage;
        this.adminUser = adminUser;
        this.siswaController = new SiswaController();
        this.userController = new UserController();
        initUI();
    }

    public Scene createDashboardScene() {
        primaryStage.setTitle("SIPRESIS - Dashboard Admin");

        // Layout utama dengan gradien dan padding
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f4f8, #e0e8f0);");
        mainLayout.setAlignment(Pos.CENTER);

        // Judul dan informasi admin
        Label titleLabel = new Label("Dashboard Admin");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Label welcomeLabel = new Label("Selamat Datang, " + adminUser.getNama());
        welcomeLabel.setFont(Font.font("Arial", 18));
        welcomeLabel.setStyle("-fx-text-fill: #34495e;");

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

        String hoverStyle = 
            "-fx-background-color: #2980b9; " +
            "-fx-text-fill: white;";

        // Tombol-tombol dashboard
        Button tambahSiswaButton = createStyledButton("Tambah Siswa", buttonStyle, hoverStyle, 
            e -> new TambahSiswaView(primaryStage, this, adminUser));

        Button tambahPenggunaButton = createStyledButton("Tambah Pengguna", buttonStyle, hoverStyle, 
            e -> new TambahPenggunaView(primaryStage, this, adminUser));

        Button lihatLaporanButton = createStyledButton("Lihat Laporan Absensi", buttonStyle, hoverStyle, 
            e -> new LaporanAbsensiView(primaryStage, adminUser));

        Button daftarSiswaButton = createStyledButton("Daftar Siswa", buttonStyle, hoverStyle, 
            e -> new DaftarSiswaView(primaryStage, adminUser));

        Button logoutButton = createStyledButton("Logout", 
            "-fx-background-color: #e74c3c; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 12px 24px; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 2);",
            "-fx-background-color: #c0392b; " +
            "-fx-text-fill: white;",
            e -> {
                LoginView loginView = new LoginView(primaryStage);
                primaryStage.setScene(loginView.getScene());
            });

        // Tambahkan tombol ke container
        buttonContainer.getChildren().addAll(
            tambahSiswaButton, 
            tambahPenggunaButton, 
            lihatLaporanButton, 
            daftarSiswaButton
        );

        // Tambahkan statistik singkat
        HBox statsContainer = createStatsContainer();

        // Container untuk menu
        VBox menuContainer = new VBox(20);
        menuContainer.setAlignment(Pos.CENTER);
        menuContainer.setPadding(new Insets(20));

        // Menu Manajemen Siswa
        setupMenuManajemenSiswa(menuContainer);

        // Menu Manajemen Pengguna
        setupMenuManajemenPengguna(menuContainer);

        // Menu Manajemen Presensi
        setupMenuManajemenPresensi(menuContainer);

        // Tambahkan semua komponen ke layout utama
        mainLayout.getChildren().addAll(
            titleLabel, 
            welcomeLabel, 
            statsContainer,
            menuContainer,
            logoutButton
        );

        return new Scene(mainLayout, 600, 700);
    }

    public void initUI() {
        Scene dashboardScene = createDashboardScene();
        primaryStage.setScene(dashboardScene);
        primaryStage.show();
    }

    // Metode untuk membuat tombol dengan gaya konsisten
    private Button createStyledButton(String text, String normalStyle, String hoverStyle, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.setStyle(normalStyle);
        button.setOnAction(action);
        return button;
    }

    // Metode untuk membuat container statistik
    private HBox createStatsContainer() {
        HBox statsContainer = new HBox(20);
        statsContainer.setAlignment(Pos.CENTER);

        // Statistik jumlah siswa
        VBox siswaStats = createStatBox("Jumlah Siswa", getSiswaCount());

        // Statistik jumlah guru
        VBox guruStats = createStatBox("Jumlah Guru", getGuruCount());

        // Statistik jumlah kelas
        VBox kelasStats = createStatBox("Jumlah Kelas", getKelasCount());

        statsContainer.getChildren().addAll(siswaStats, guruStats, kelasStats);
        return statsContainer;
    }

    // Metode untuk membuat box statistik
    private VBox createStatBox(String label, int value) {
        VBox statBox = new VBox(10);
        statBox.setAlignment(Pos.CENTER);
        statBox.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-padding: 15px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        Label titleLabel = new Label(label);
        titleLabel.setStyle(
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #2c3e50;"
        );

        Label valueLabel = new Label(String.valueOf(value));
        valueLabel.setStyle(
            "-fx-font-size: 24px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #3498db;"
        );

        statBox.getChildren().addAll(titleLabel, valueLabel);
        return statBox;
    }

    // Metode untuk mendapatkan jumlah siswa
    private int getSiswaCount() {
        return siswaController.daftarSiswa().size();
    }

    // Metode untuk mendapatkan jumlah guru
    private int getGuruCount() {
        return userController.daftarPengguna().stream()
            .filter(user -> user instanceof Guru)
            .collect(Collectors.toList())
            .size();
    }

    // Metode untuk mendapatkan jumlah kelas
    private int getKelasCount() {
        return Arrays.asList("X-A", "X-B", "XI-A", "XI-B", "XII-A", "XII-B").size();
    }

    // Metode untuk menampilkan alert
    private void showAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("SIPRESIS");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void kembaliKeDashboard() {
        // Buat ulang dashboard admin dengan admin yang sama
        new AdminDashboardView(primaryStage, adminUser);
    }

    private void setupMenuManajemenSiswa(VBox menuContainer) {
        Label manajemenSiswaLabel = createMenuSectionLabel("Manajemen Siswa");
        
        Button tambahSiswaButton = createMenuButton("Tambah Siswa");
        tambahSiswaButton.setOnAction(e -> new TambahSiswaView(primaryStage, this, adminUser));
        
        Button daftarSiswaButton = createMenuButton("Daftar Siswa");
        daftarSiswaButton.setOnAction(e -> new DaftarSiswaView(primaryStage, adminUser));

        HBox menuSiswaContainer = new HBox(15);
        menuSiswaContainer.setAlignment(Pos.CENTER);
        menuSiswaContainer.getChildren().addAll(tambahSiswaButton, daftarSiswaButton);

        menuContainer.getChildren().addAll(
            manajemenSiswaLabel, 
            menuSiswaContainer
        );
    }

    private void setupMenuManajemenPengguna(VBox menuContainer) {
        Label manajemenPenggunaLabel = createMenuSectionLabel("Manajemen Pengguna");
        
        Button tambahPenggunaButton = createMenuButton("Tambah Pengguna");
        tambahPenggunaButton.setOnAction(e -> new TambahPenggunaView(primaryStage, this, adminUser));
        
        Button daftarPenggunaButton = createMenuButton("Daftar Pengguna");
        daftarPenggunaButton.setOnAction(e -> new DaftarPenggunaView(primaryStage, adminUser));

        HBox menuPenggunaContainer = new HBox(15);
        menuPenggunaContainer.setAlignment(Pos.CENTER);
        menuPenggunaContainer.getChildren().addAll(tambahPenggunaButton, daftarPenggunaButton);

        menuContainer.getChildren().addAll(
            manajemenPenggunaLabel, 
            menuPenggunaContainer
        );
    }

    private void setupMenuManajemenPresensi(VBox menuContainer) {
        Label manajemenPresensiLabel = createMenuSectionLabel("Manajemen Presensi");
        
        Button tambahPresensiButton = createMenuButton("Tambah Presensi");
        tambahPresensiButton.setOnAction(e -> new TambahPresensiView(primaryStage, this, adminUser));
        
        Button editPresensiButton = createMenuButton("Edit Presensi");
        editPresensiButton.setOnAction(e -> new EditPresensiView(primaryStage, this, adminUser));
        
        Button laporanPresensiButton = createMenuButton("Laporan Presensi");
        laporanPresensiButton.setOnAction(e -> new LaporanAbsensiView(primaryStage, adminUser));

        HBox menuPresensiContainer = new HBox(15);
        menuPresensiContainer.setAlignment(Pos.CENTER);
        menuPresensiContainer.getChildren().addAll(tambahPresensiButton, editPresensiButton, laporanPresensiButton);

        menuContainer.getChildren().addAll(
            manajemenPresensiLabel, 
            menuPresensiContainer
        );
    }

    private Label createMenuSectionLabel(String text) {
        Label label = new Label(text);
        label.setStyle(
            "-fx-font-size: 18px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #2c3e50;"
        );
        return label;
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: #3498db; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 12px 24px; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 2);"
        );
        return button;
    }
}
