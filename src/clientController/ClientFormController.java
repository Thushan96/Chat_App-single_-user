package clientController;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.jnlp.FileContents;
import javax.swing.text.html.Option;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientFormController implements Initializable {

    public TextField txtClientMsg;
    public VBox vbox_messages;
    public ScrollPane sp_Main;
    public Button btnAttach;

    private Client client;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            client=new Client(new Socket("localhost",1234));
            System.out.println("connected to server");


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_Main.setVvalue((Double)newValue);
            }
        });

        client.receiveMessageFromServer(vbox_messages);


    }

    public void sendOnAction(ActionEvent actionEvent) {
        String messagesToSend=txtClientMsg.getText();

        if (!messagesToSend.isEmpty()){
            HBox hBox=new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5,5,5,10));

            Text text=new Text(messagesToSend);
            TextFlow textFlow=new TextFlow(text);//if text big wrap it to another line
            textFlow.setStyle(
                    "-fx-background-color: rgb(15,125,242); " +
                    "-fx-background-radius: 20px" );

            textFlow.setPadding(new Insets(5,10,5,10));
            text.setFill(Color.color(0.934,0.945,0.996));

            hBox.getChildren().add(textFlow);
            vbox_messages.getChildren().add(hBox);

            client.sendMessageToServer(messagesToSend); //To display message in client
            txtClientMsg.clear();

        }
    }



    public static void addLabel(String MessageFromServer, VBox vBox){
        HBox hBox=new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text=new Text(MessageFromServer);


        TextFlow textFlow=new TextFlow(text);//if text big wrap it to another line
        textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                "-fx-background-color: rgb(15,125,242); " +
                "-fx-background-radius: 20px" );
        textFlow.setPadding(new Insets(5,10,5,10));

        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        }); //static method to run later


    }



    public void btnAttachOnAction(ActionEvent actionEvent) throws IOException, InterruptedException {
        Stage stage = (Stage) btnAttach.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            txtClientMsg.setText(selectedFile.getAbsolutePath());
        }

        System.out.println(txtClientMsg.getText());

        client.sendImageToServer(txtClientMsg.getText());
    }


}
