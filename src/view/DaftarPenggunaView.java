package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Admin;
import model.Guru;
import model.User;
import controller.UserController;

import java.util.List;
import java.util.Optional;

public class DaftarPenggunaView {
    private Stage primaryStage;
    private AdminDashboardView adminDashboardView;
    private UserController userController;
    private Admin admin;

    public DaftarPenggunaView(Stage primaryStage, Admin admin) {
        this.primaryStage = primaryStage;
        this.admin = admin;
        this.userController = new UserController();
        initUI();
    }

    private void initUI() {
        primaryStage.setTitle("SIPRESIS - Daftar Pengguna");

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f4f8, #e0e8f0);");
        mainLayout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Daftar Pengguna");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

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

        // Tabel Pengguna
        TableView<User> tableView = createPenggunaTableView();

        mainLayout.getChildren().addAll(titleLabel, backButton, tableView);

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<User> createPenggunaTableView() {
        TableView<User> tableView = new TableView<>();
        List<User> daftarPengguna = userController.daftarPengguna();
        tableView.getItems().addAll(daftarPengguna);

        // Kolom Nama
        TableColumn<User, String> namaColumn = new TableColumn<>("Nama");
        namaColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNama())
        );

        // Kolom Username
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsername())
        );

        // Kolom Role
        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRole())
        );

        // Kolom Aksi (Edit dan Hapus)
        TableColumn<User, Void> aksiColumn = new TableColumn<>("Aksi");
        aksiColumn.setCellFactory(param -> new TableCell<User, Void>() {
            private final HBox actionButtons = new HBox(10);
            private final Button editButton = new Button("Edit");
            private final Button hapusButton = new Button("Hapus");

            {
                editButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
                hapusButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    tampilkanFormEditPengguna(user);
                });

                hapusButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    konfirmasiHapusPengguna(user);
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

        tableView.getColumns().addAll(namaColumn, usernameColumn, roleColumn, aksiColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        return tableView;
    }

    private void tampilkanFormEditPengguna(User user) {
        new EditPenggunaView(primaryStage, adminDashboardView, admin, user);
    }

    private void konfirmasiHapusPengguna(User user) {
        Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION);
        konfirmasi.setTitle("Konfirmasi Hapus Pengguna");
        konfirmasi.setHeaderText("Anda yakin ingin menghapus pengguna " + user.getNama() + "?");
        konfirmasi.setContentText("Tindakan ini tidak dapat dibatalkan.");

        Optional<ButtonType> result = konfirmasi.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean berhasil = userController.hapusPengguna(user.getId());
            if (berhasil) {
                showAlert("Berhasil menghapus pengguna!");
                initUI(); // Refresh view
            } else {
                showAlert("Gagal menghapus pengguna!");
            }
        }
    }

    private void kembaliKeDashboard() {
        AdminDashboardView dashboardView = new AdminDashboardView(primaryStage, admin);
        Scene dashboardScene = dashboardView.createDashboardScene();
        primaryStage.setScene(dashboardScene);
    }

    private void showAlert(String pesan) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }
}
