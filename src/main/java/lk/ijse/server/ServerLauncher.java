package lk.ijse.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ServerLauncher extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/serverForm.fxml"))));
        stage.setTitle("Server");
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();

        Stage stage1 = new Stage();
        stage1.initModality(Modality.WINDOW_MODAL);
        stage1.initOwner(stage.getScene().getWindow());
        stage1.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/loginForm.fxml"))));
        stage1.centerOnScreen();
        stage1.setResizable(false);
        stage1.show();

    }
}
