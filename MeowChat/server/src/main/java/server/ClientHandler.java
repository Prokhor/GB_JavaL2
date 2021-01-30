package server;

import commands.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String nickname;
    private String login;

    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {

                    socket.setSoTimeout(5000);

                    // цикл аутентификации
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith(Command.AUTH)) {
                            String[] token = msg.split("\\s");
                            String checkNick = server.getAuthService().getNicknameByLoginAndPassword(token[1], token[2]);
                            login = token[1];
                            if (checkNick != null) {
                                if (!server.isLoginIsAuthenticated(login)) {
                                    nickname = checkNick;
                                    sendMsg(String.format("%s %s", Command.AUTH_SUCCESS, nickname));
                                    server.subscribeClient(this);
                                    System.out.printf(Command.CLIENT_CONNECTED, socket.getRemoteSocketAddress(), nickname);
                                    socket.setSoTimeout(0);
                                    break;
                                } else {
                                    sendMsg(String.format(Command.LOGIN_IS_BUSY, login));
                                }
                            } else {
                                sendMsg(Command.AUTH_FAILED);
                            }
                        }
                        if (msg.trim().equals(Command.QUIT)) {
                            sendMsg(Command.QUIT);
                            throw new RuntimeException(String.format(Command.DISCONNECT, nickname == null ? "Anonymous" : nickname));
                        }

                        if (msg.startsWith(Command.REGISTER_CLIENT)) {
                            String[] token = msg.split("\\s");
                            if (token.length < 4) {
                                continue;
                            }
                            if (server.getAuthService().registration(token[1], token[2], token[3])) {
                                sendMsg(Command.REGISTER_SUCCESS);
                            } else {
                                sendMsg(Command.REGISTER_FAILED);
                            }
                        }
                    }

                    // цикл работы
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith("/")){
                            if (msg.trim().equals(Command.QUIT)) {
                                sendMsg(Command.QUIT);
                                System.out.printf(Command.DISCONNECT, nickname);
                                break;
                            }
                            if (msg.startsWith(Command.PERSONAL_MSG)){
                                if (msg.split("\\s", 3).length < 3){
                                    continue;
                                }
                                server.broadcastMsg(this, msg.split("\\s", 3)[2], msg.split("\\s", 3)[1]);
                            }
                        }  else {
                            server.broadcastMsg(this, msg);
                        }
                    }
                } catch (SocketTimeoutException e) {
                    sendMsg(Command.QUIT);
                } catch (RuntimeException e){
                    System.out.println(e.getMessage());
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

    public String getLogin() {
        return login;
    }
}
