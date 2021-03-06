package server;

import commands.Command;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int PORT = 8880;
    private ServerSocket server;
    private Socket socket;
    private List<ClientHandler> clients;
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private ExecutorService service;

    public Server() {
        clients = new CopyOnWriteArrayList<>();
        //authService = new SimpleAuthService();
        authService = new DBWorkService();
        service = Executors.newCachedThreadPool();

        try {
            server = new ServerSocket(PORT);
            System.out.println(Command.SERVER_STARTED);
            while (true) {
                socket = server.accept();
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ((DBWorkService) authService).disconnect();
            service.shutdown();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    public void broadcastMsg(ClientHandler sender, String msg){
        String message = String.format("%s [%s]: %s", dateFormat.format(new Date(System.currentTimeMillis())), sender.getNickname(), msg);
        for (ClientHandler client : clients) {
            client.sendMsg(message);
        }
        if (authService instanceof LogService) {
            ((LogService) authService).logUserMessage(((LogService) authService).getUserIDByNickname(sender.getNickname()), msg, dateFormat.format(new Date(System.currentTimeMillis())));
        }
    }

    public void broadcastMsg(ClientHandler sender, String msg, String recipientNickname){
        String message = String.format("%s [%s] private to [%s]: %s", dateFormat.format(new Date(System.currentTimeMillis())), sender.getNickname(), recipientNickname, msg);
        for (ClientHandler client : clients) {
            if (client.getNickname().equals(recipientNickname)){
                client.sendMsg(message);
                if (!client.equals(sender)){
                    sender.sendMsg(message);
                }
                if (authService instanceof LogService) {
                    ((LogService) authService).logUserMessage(
                            ((LogService) authService).getUserIDByNickname(sender.getNickname()),
                            ((LogService) authService).getUserIDByNickname(recipientNickname),
                            msg, dateFormat.format(new Date(System.currentTimeMillis())));
                }
                return;
            }
        }
        sender.sendMsg(Command.NOT_DELIVERED);
    }

    void subscribeClient(ClientHandler clientHandler){
        clients.add(clientHandler);
        broadcastClientList();
        for (ClientHandler client : clients) {
            if (!client.equals(clientHandler)){
                client.sendMsg(String.format(Command.CONNECTED_TO_CHAT, clientHandler.getNickname()));
            }
        }
    }

    void unsubscribeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientList();
        for (ClientHandler client : clients) {
            if (client.getNickname() != null){
                client.sendMsg(String.format(Command.DISCONNECTED_FROM_CHAT, clientHandler.getNickname()));
            }
        }
    }

    public boolean isLoginIsAuthenticated(String login){
        for (ClientHandler client : clients) {
            if (client.getLogin().equalsIgnoreCase(login)){
                return true;
            }
        }
        return false;
    }

    public void broadcastClientList(){
        StringBuilder sb = new StringBuilder(Command.CLIENT_LIST);
        for (ClientHandler client : clients) {
            sb.append(" ").append(client.getNickname());
        }
        String msg = sb.toString();
        for (ClientHandler client : clients) {
            client.sendMsg(msg);
        }
    }

    public ExecutorService getService() {
        return service;
    }
}