package serverController;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket  serverSocket; //to listen to incoming connections
    private Socket socket; //To communicate when receive connection
    private BufferedReader bufferedReader; //to read messages send to us
    private BufferedWriter bufferedWriter; //to send messages

    public Server(ServerSocket serverSocket) {

        try {
            this.serverSocket = serverSocket;
            this.socket=serverSocket.accept();
            this.bufferedReader=new BufferedReader( new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error creating Server");
            closeEverything(socket,bufferedReader,bufferedWriter);
        }


    }

    public void sendMessageToClient(String messageToClient){
        try {
            bufferedWriter.write(messageToClient);
            bufferedWriter.newLine(); //because it excepts characters until new line
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending message to client");
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public void receiveMessagesFromClient(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){


                    try {
                        String  MessageFromClient = bufferedReader.readLine();
                        ServerFormController.addLabel(MessageFromClient,vBox);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error receiving message from client");
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
