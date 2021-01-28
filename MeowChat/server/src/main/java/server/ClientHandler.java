package server;

import commands.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String nickname;

    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    // цикл аутентификации
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith(Command.AUTH)) {
                            String[] token = msg.split("\\s");
                            String checkNick = server.getAuthService().getNicknameByLoginAndPassword(token[1], token[2]);
                            if (checkNick != null){
                                nickname = checkNick;
                                sendMsg(String.format("%s %s", Command.AUTH_SUCCESS, nickname));
                                server.subscribeClient(this);
                                System.out.printf("Client [%s] connected as %s\n", socket.getRemoteSocketAddress(), nickname);
                                break;
                            } else {
                                sendMsg("Authenticate failed");
                            }
                        }

                        if (msg.trim().equals(Command.QUIT)) {
                            sendMsg(Command.QUIT);
                            System.out.printf("%s disconnected\n", nickname);
                            break;
                        }
                    }

                    // цикл работы
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.trim().equals(Command.QUIT)) {
                            sendMsg(Command.QUIT);
                            System.out.printf("%s disconnected\n", nickname);
                            break;
                        }
                        if (msg.startsWith(Command.PERSONAL_MSG)){
                            server.broadcastMsg(this, msg.split("\\s", 3)[2], msg.split("\\s", 3)[1]);
                        } else {
                            server.broadcastMsg(this, msg);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    server.unsubscribeClient(this);
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }
}
