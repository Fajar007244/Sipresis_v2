import javafx.application.Application;
import javafx.stage.Stage;
import view.LoginView;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginView loginView = new LoginView(primaryStage);
        primaryStage.setScene(loginView.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
