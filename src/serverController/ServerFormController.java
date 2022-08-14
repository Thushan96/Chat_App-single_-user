package serverController;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;


public class ServerFormController implements Initializable {

    public TextField txtServerMsg;
    public ScrollPane sp_Main;
    public VBox vbox_messages;


    private Server server;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            server=new Server(new ServerSocket(1234));
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("error creating  class server");
        }

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_Main.setVvalue((Double)newValue);
            }
        });

        server.receiveMessagesFromClient(vbox_messages);

    }



    public void sendOnAction(ActionEvent actionEvent) {
        String messagesToSend=txtServerMsg.getText();
        if (!messagesToSend.isEmpty()){
            HBox hBox=new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5,5,5,10));

            Text text=new Text(messagesToSend);
            TextFlow textFlow=new TextFlow(text);//if text big wrap it to another line
            textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                    "-fx-background-color: rgb(15,125,242); " +
                    "-fx-background-radius: 20px" );

            textFlow.setPadding(new Insets(5,10,5,10));
            text.setFill(Color.color(0.934,0.945,0.996));

            hBox.getChildren().add(textFlow);
            vbox_messages.getChildren().add(hBox);

            server.sendMessageToClient(messagesToSend); //To display message in client
            txtServerMsg.clear();

        }
    }

    public static void addLabel(String MessageFromClient,VBox vBox){
        HBox hBox=new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text=new Text(MessageFromClient);

        TextFlow textFlow=new TextFlow(text);//if text big wrap it to another line
        textFlow.setStyle(
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
}
