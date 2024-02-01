package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.vdurmont.emoji.EmojiLoader;
import com.vdurmont.emoji.EmojiParser;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import lk.ijse.emoji.Emoji;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class ClientFormController {
    public VBox vBox;
    public TextField txtMessage;
    public ScrollPane scrollPane;
    public Label lblName;
    public AnchorPane clientFormContext;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String clientName;

    public synchronized void setClientName(String clientName) {
        this.clientName = clientName;
        lblName.setText(clientName);
        System.out.println("Name 11: "+clientName);
    }

    public void initialize(){
        //lblName.setText(clientName);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket = new Socket("localhost", 3080);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    System.out.println("Client connected");
                    ServerFormController.printMessage(clientName+" joined.");
                    String message;
                    while (socket.isConnected()){
                        message = dataInputStream.readUTF();
                        if (message.length() > 200){
                            printImage(message, ClientFormController.this.vBox);
                        }else {
                            printMessage(message, ClientFormController.this.vBox);
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();

        this.vBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                scrollPane.setVvalue((Double) newValue);
            }
        });

        //emoji();
    }

    /*public void setName(String name) {
        clientName = name;
    }*/

    public void btnImageOnAction(ActionEvent actionEvent) throws IOException {
        /*FileDialog fileDialog = new FileDialog((Frame) null, "Select image to send!");
        fileDialog.setMode(FileDialog.LOAD);
        fileDialog.setVisible(true);

        Image image =new Image(fileDialog.getDirectory()+fileDialog.getFile());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(250);
        imageView.setFitHeight(250);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5,5,5,10));
        hBox.getChildren().add(imageView);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        vBox.getChildren().add(hBox);

        try {
            dataOutputStream.writeUTF(fileDialog.getDirectory()+fileDialog.getFile());
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Send");

        try {
            File fileToSend = fileChooser.showOpenDialog(null);

            if (fileToSend != null) {
                String fileName = fileToSend.getName();

                if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                        fileName.endsWith(".png") || fileName.endsWith(".gif") ||
                        fileName.endsWith(".bmp")) {

                        sendImageFile(fileToSend);
                }
            }else{
                System.out.println("Please Select Image");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void sendImageFile(File fileToSend) throws IOException {
        Image image = new Image(fileToSend.getPath());
        dataOutputStream.writeUTF(clientName + ":"+ convertImageToString(image));
        dataOutputStream.flush();
        setImage(image);
    }

    private void setImage(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5,5,5,10));
        hBox.getChildren().add(imageView);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        vBox.getChildren().add(hBox);
    }

    private String convertImageToString(Image image) {
        try {

            double maxWidth = 600;
            double maxHeight = 400;
            double width = image.getWidth();
            double height = image.getHeight();

            double scaleFactor = (width > maxWidth || height > maxHeight) ? Math.min(maxWidth / width, maxHeight / height) : 1.0;
            width *= scaleFactor;
            height *= scaleFactor;

            BufferedImage resizedImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(SwingFXUtils.fromFXImage(image, null), 0, 0, (int) width, (int) height, null);
            g.dispose();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", outputStream);


            byte[] imageBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void printImage(String message, VBox vBox) {
        String name = message.split(":")[0];
        /*HBox hBox1 = new HBox();
        hBox1.setAlignment(Pos.CENTER_LEFT);
        hBox1.setPadding(new Insets(5,5,5,10));*/

        HBox hBoxName = new HBox();
        hBoxName.setAlignment(Pos.CENTER_LEFT);
        Text textName = new Text(name);
        TextFlow textFlowName = new TextFlow(textName);
        //textFlowName.setStyle("-fx-background-color: #EAB543; -fx-background-radius: 10px");
        hBoxName.getChildren().add(textFlowName);

        Image image = convertStringToImage(message);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));
        hBox.getChildren().add(imageView);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }

    private Image convertStringToImage(String message) {
        System.out.println(message);
        String [] splitMessage = splitMessage(message);
        String imgName = splitMessage[1];
        byte [] imgData = Base64.getDecoder().decode(imgName);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imgData);
        return new Image(byteArrayInputStream);
    }

    private String[] splitMessage(String message) {
        String[] parts = message.split(":");
        return parts;
    }

    public void left() {
        //ServerFormController.printMessage(clientName+" left.");
    }

    /*private void emoji() {
        // Create the EmojiPicker


        VBox vBox = new VBox(emojiPicker);
        vBox.setPrefSize(150,300);
        vBox.setLayoutX(400);
        vBox.setLayoutY(175);
        vBox.setStyle("-fx-font-size: 30");

        pane.getChildren().add(vBox);

        // Set the emoji picker as hidden initially
        emojiPicker.setVisible(false);

        // Show the emoji picker when the button is clicked
        emojiButton.setOnAction(event -> {
            if (emojiPicker.isVisible()){
                emojiPicker.setVisible(false);
            }else {
                emojiPicker.setVisible(true);
            }
        });

        // Set the selected emoji from the picker to the text field
        emojiPicker.getEmojiListView().setOnMouseClicked(event -> {
            String selectedEmoji = emojiPicker.getEmojiListView().getSelectionModel().getSelectedItem();
            if (selectedEmoji != null) {
                txtMsg.setText(txtMsg.getText()+selectedEmoji);
            }
            emojiPicker.setVisible(false);
        });
    }*/

    public void txtMsgOnAction(ActionEvent actionEvent) {
        btnSendOnAction(actionEvent);
    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        sendMessage(txtMessage.getText());
    }

    private void sendMessage(String msgToSend) {
        if (!msgToSend.isEmpty()){
            if (!msgToSend.matches(".*\\.(png|jpe?g|gif)$")){

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new javafx.geometry.Insets(5, 5, 5, 10));
                Text text = new Text(msgToSend);
                text.setStyle("-fx-font-size: 14");

                TextFlow textFlow = new TextFlow(text);
                textFlow.setStyle("-fx-background-color: #0097e6; -fx-font-weight: bold; -fx-color: white; -fx-background-radius: 20px");
                textFlow.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));
                text.setFill(Color.color(1, 1, 1));

                hBox.getChildren().add(textFlow);

                HBox hBoxTime = new HBox();
                hBoxTime.setAlignment(Pos.CENTER_RIGHT);
                hBoxTime.setPadding(new Insets(0, 5, 5, 10));
                String stringTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                Text time = new Text(stringTime);
                time.setStyle("-fx-font-size: 8");

                hBoxTime.getChildren().add(time);

                vBox.getChildren().add(hBox);
                vBox.getChildren().add(hBoxTime);


                try {
                    dataOutputStream.writeUTF(clientName + ":" + msgToSend);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                txtMessage.clear();
            }
        }
    }

    public static void printMessage(String msg, VBox vBox) throws IOException {
        /*if (msg.matches(".*\\.(png|jpe?g|gif)$")){
            Image image = new Image(msg);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(200);
            imageView.setFitWidth(200);
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5,5,5,10));
            hBox.getChildren().add(imageView);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBox);
                }
            });

        }else {*/

            String name = msg.split(":")[0];
            String message = msg.split(":")[1];

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5,5,5,10));

            HBox hBoxName = new HBox();
            hBoxName.setAlignment(Pos.CENTER_LEFT);
            Text textName = new Text(name);
            TextFlow textFlowName = new TextFlow(textName);
            //textFlowName.setStyle("-fx-background-color: #EAB543; -fx-background-radius: 10px");
            hBoxName.getChildren().add(textFlowName);

            Text text = new Text(message);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-background-color: #e84118; -fx-font-weight: bold; -fx-color : white; -fx-background-radius: 20px");
            textFlow.setPadding(new Insets(5,10,5,10));
            text.setFill(Color.color(0,0,0));

            hBox.getChildren().add(textFlow);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBoxName);
                    vBox.getChildren().add(hBox);
                }
            });

    }

    public void btnImojiOnAction(ActionEvent actionEvent) {
        Emoji emoji = new Emoji();
        VBox vBox =new VBox(emoji);
        vBox.setPrefSize(150,300);
        vBox.setLayoutX(400);
        vBox.setLayoutY(175);
        vBox.setStyle("-fx-font-size: 30");

        clientFormContext.getChildren().add(vBox);
        emoji.setVisible(true);

        emoji.getListView().setOnMouseClicked(event -> {
            String selectedEmoji = emoji.getListView().getSelectionModel().getSelectedItem();
            if (selectedEmoji != null) {
                txtMessage.setText(txtMessage.getText()+selectedEmoji);
            }
            emoji.setVisible(false);
        });
    }
}
