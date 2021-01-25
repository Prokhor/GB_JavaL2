package gb.l6hw;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    private static ServerSocket server;
    private static Socket socket;
    private static final int PORT = 8880;

    private static DataInputStream in;
    private static DataOutputStream out;
    private static Scanner srvScanner;

    public static void main(String[] args) {
        try {

            server = new ServerSocket(PORT);
            System.out.println("Server started");

            socket = server.accept();
            System.out.println("Client " + socket.getRemoteSocketAddress() + " connected");

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            srvScanner = new Scanner(System.in);

            Thread serverReaderThread = new Thread(() -> {
                try {
                    while (true) {
                        out.writeUTF(srvScanner.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            serverReaderThread.setDaemon(true);
            serverReaderThread.start();

            while (true) {
                String msgFromClient = in.readUTF();
                if (msgFromClient.equals("/q")) {
                    System.out.println("Client disconnected");
                    out.writeUTF("/q");
                    break;
                }
                System.out.println("Client: " + msgFromClient);
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
}