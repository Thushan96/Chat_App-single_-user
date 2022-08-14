package clientController;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import serverController.ServerFormController;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;


    public Client(Socket socket) {

        try {
            this.socket=socket;
            this.bufferedReader=new BufferedReader( new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error creating Client");
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public void sendMessageToServer(String MessagesToServer){
        try {
            bufferedWriter.write(MessagesToServer);
            bufferedWriter.newLine(); //because it excepts characters until new line
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending message to Server");
            closeEverything(socket,bufferedReader,bufferedWriter);
        }

    }

    public void receiveMessageFromServer(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){


                    try {
                        String  MessageFromServer = bufferedReader.readLine();
                        ClientFormController.addLabel(MessageFromServer,vBox);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error receiving message from server");
                        closeEverything(socket,bufferedReader,bufferedWriter);
                        break;
                    }


                }
            }
        }).start();
    }

    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        try {
            if (bufferedReader !=null){
                bufferedReader.close();
            }

            if (bufferedWriter !=null){
                bufferedWriter.close();
            }

            if (socket !=null){
                socket.close();
            }


        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
