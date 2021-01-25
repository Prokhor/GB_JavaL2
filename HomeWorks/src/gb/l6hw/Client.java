package gb.l6hw;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String SERVER_IP = "localhost";
    private static final int PORT = 8880;

    private static Socket socket;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Scanner clientScan;

    public static void main(String[] args) {
        try {
            socket = new Socket(SERVER_IP, PORT);
            System.out.println("Connected to server " + socket.getRemoteSocketAddress());
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            clientScan = new Scanner(System.in);

            Thread clientReaderThread = new Thread(() -> {
                while (true) {
                    try {
                        out.writeUTF(clientScan.nextLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            clientReaderThread.setDaemon(true);
            clientReaderThread.start();

            while (true) {
                String msgFromServer = in.readUTF();
                if (msgFromServer.equals("/q")) {
                    System.out.println("Server stopped");
                    out.writeUTF("/q");
                    break;
                }
                System.out.println("Server: " + msgFromServer);
            }
        } catch (IOException e) {
            System.out.println("Server is not available now");
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