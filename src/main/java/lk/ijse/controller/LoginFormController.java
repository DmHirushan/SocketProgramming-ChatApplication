package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    public TextField txtName;
    public static String name;

    public void btnLoginOnAction(ActionEvent actionEvent) {

        if (!txtName.getText().isEmpty()) {
            String name = txtName.getText();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/clientForm.fxml"));
                Scene scene = new Scene(loader.load());

                ClientFormController clientFormController = loader.getController();
                clientFormController.setClientName(name);

                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle(name);
                stage.setResizable(false);
                stage.centerOnScreen();
                stage.setOnCloseRequest(windowEvent -> {
                    clientFormController.left();
                });
                stage.show();

                txtName.clear();
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please Enter Your Name!").show();
        }
    }

    public void txtNameOnAction(ActionEvent actionEvent) {
        btnLoginOnAction(actionEvent);
    }
}
