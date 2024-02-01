package lk.ijse.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lk.ijse.server.Server;

import java.awt.*;

public class ServerFormController {
    private static VBox vBox;
    public ScrollPane scrollPane;
    private Server server;
    public VBox vBox1;

    public void initialize(){
        vBox = vBox1;
        printMessage("server starting....");
        vBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                scrollPane.setVvalue((Double) newValue);
            }
        });

        new Thread(() -> {
            try{
                Server.getInstance().makeSocket();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        printMessage("Server Running..");
    }

    public static void printMessage(String message){
        HBox hBox = new HBox();
        hBox.setPadding(new javafx.geometry.Insets(5, 5, 5, 10));
        hBox.setAlignment(Pos.CENTER_LEFT);
        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: #abb8c3; -fx-font-weight: bold; -fx-background-radius: 20px");
        textFlow.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));
        text.setFill(Color.color(0,0,0));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }
}
