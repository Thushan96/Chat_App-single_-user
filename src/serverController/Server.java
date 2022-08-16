package serverController;

import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

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

    public void receiveImageFromServer(VBox vBox) throws IOException {

        InputStream inputStream = socket.getInputStream();

        System.out.println("Reading: " + System.currentTimeMillis());

        byte[] sizeAr = new byte[4];
        inputStream.read(sizeAr);
        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

        byte[] imageAr = new byte[size];
        inputStream.read(imageAr);

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));

        System.out.println("Received " + image.getHeight() + "x" + image.getWidth() + ": " + System.currentTimeMillis());
        ImageIO.write(image, "jpg", new File("C:\\Users\\Jakub\\Pictures\\test2.jpg"));

        closeEverything(socket,bufferedReader,bufferedWriter);


    }

    public void receiveMessagesFromClient(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){


                    try {
                        String  MessageFromClient = bufferedReader.readLine();
                        if (MessageFromClient.equals(Float) ){
                            System.out.println(MessageFromClient);
                            ServerFormController.addLabel(MessageFromClient,vBox);
                        }


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
