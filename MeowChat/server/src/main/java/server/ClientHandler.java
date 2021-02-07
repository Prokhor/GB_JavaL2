package server;

import commands.Command;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientHandler {

    private final int TIMEOUT_TO_KICK = 5000;
    private final String LOG_LOCAL_HISTORY_FILENAME = "history/history_%s.txt";

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

                    socket.setSoTimeout(TIMEOUT_TO_KICK);

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
                        if (msg.startsWith("/")) {
                            if (msg.trim().equals(Command.QUIT)) {
                                sendMsg(Command.QUIT);
                                System.out.printf(Command.DISCONNECT, nickname);
                                break;
                            }
                            if (msg.startsWith(Command.PERSONAL_MSG)) {
                                if (msg.split("\\s", 3).length < 3) {
                                    continue;
                                }
                                server.broadcastMsg(this, msg.split("\\s", 3)[2], msg.split("\\s", 3)[1]);
                            }
                            if (msg.startsWith(Command.CHANGE_MY_NICK)) {
                                String[] tokens = msg.split("\\s", 2);
                                if (tokens.length < 2) {
                                    continue;
                                }
                                if (tokens[1].split("\\s").length > 1) {
                                    sendMsg(String.format(Command.BAD_NICKNAME));
                                    continue;
                                }
                                if (tokens[1].length() > 50) {
                                    sendMsg(String.format(Command.TOO_LONG_NICK));
                                    continue;
                                }
                                if (((DBWorkService) server.getAuthService()).changeNickname(this.login, tokens[1])) {
                                    server.broadcastMsg(this, String.format(Command.CHANGED_NICK, tokens[1]));
                                    nickname = tokens[1];
                                    server.broadcastClientList();
                                    sendMsg(msg);
                                }
                            }
                        } else {
                            server.broadcastMsg(this, msg);
                        }
                    }
                } catch (SocketTimeoutException e) {
                    sendMsg(Command.QUIT);
                } catch (RuntimeException e) {
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
