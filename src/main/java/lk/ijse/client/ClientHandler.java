package lk.ijse.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientHandler {
    private String message = " ";
    private Socket socket;
    private List <ClientHandler> clients;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ClientHandler(Socket socket, List <ClientHandler> clients){
        try{
            this.socket = socket;
            this.clients = clients;
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (socket.isConnected()){
                        message = dataInputStream.readUTF();
                        for (ClientHandler clientHandler : clients){
                            if (clientHandler.socket.getPort() != socket.getPort()){
                                clientHandler.dataOutputStream.writeUTF(message);
                                clientHandler.dataOutputStream.flush();
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
