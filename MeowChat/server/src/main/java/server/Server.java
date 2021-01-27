package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    private final int PORT = 8880;
    private ServerSocket server;
    private Socket socket;
    private List<ClientHandler> clients;
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private AuthService authService;

    public Server() {
        clients = new CopyOnWriteArrayList<>();
        authService = new SimpleAuthService();

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server started");

            while (true) {
                socket = server.accept();
                //System.out.println("Client " + socket.getRemoteSocketAddress() + " connected");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMsg(ClientHandler clientHandler, String msg){
        String message = String.format("%s [%s]: %s", dateFormat.format(new Date(System.currentTimeMillis())), clientHandler.getNickname(), msg);
        for (ClientHandler client : clients) {
            client.sendMsg(message);
        }
    }

    public void broadcastMsg(ClientHandler clientHandler, String msg, String recipientLogin){
        String message = String.format("%s [%s]: %s", dateFormat.format(new Date(System.currentTimeMillis())), clientHandler.getNickname(), msg);
        for (ClientHandler client : clients) {
            if (client.getNickname().equals(recipientLogin) || client.getNickname().equals(clientHandler.getNickname())){
                client.sendMsg(message);
            }
        }
    }

    void subscribeClient(ClientHandler clientHandler){
        clients.add(clientHandler);
    }

    void unsubscribeClient(ClientHandler clientHandler){
        clients.remove(clientHandler);
    }

    public AuthService getAuthService() {
        return authService;
    }
}