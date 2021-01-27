package client;

import commands.Command;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public HBox authPanel;
    @FXML
    public HBox msgPanel;
    @FXML
    public TextField tfLogin;
    @FXML
    public PasswordField pfPassword;
    @FXML
    private TextArea taMessages;
    @FXML
    private TextField tfMessage;

    @FXML
    public void sendMessage(ActionEvent actionEvent) {
        try {
            if (tfMessage.getText().trim().length() > 0) {
                out.writeUTF(tfMessage.getText() + "\n\n");
                tfMessage.clear();
                tfMessage.requestFocus();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void tryToAuth(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()) {
            connect();
        }
        if (tfLogin.getText().trim().length() > 0 && pfPassword.getText().trim().length() > 0) {
            String msg = String.format("%s %s %s", Command.AUTH, tfLogin.getText().trim(), pfPassword.getText().trim());
            try {
                out.writeUTF(msg);
                pfPassword.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final String SERVER_ADDRESS = "localhost";
    private final int PORT = 8880;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean authenticated;
    private String nickname;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            stage = (Stage) taMessages.getScene().getWindow();
        });
        setAuthenticated(false);
    }

    private void connect() {
        try {
            socket = new Socket(SERVER_ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    // цикл аутентификации
                    while (true) {
                        String msg = in.readUTF();

                        if (msg.startsWith("/")) {
                            if (msg.startsWith(Command.AUTH_SUCCESS)) {
                                nickname = msg.split("\\s")[1];
                                setAuthenticated(true);
                                taMessages.appendText(String.format("Success connect to chat as %s\n", nickname));
                                break;
                            }

                            if (msg.trim().equals(Command.QUIT)) {
                                //System.out.println("Client disconnected");
                                throw new RuntimeException("Kicked by server");
                            }
                        } else {
                            taMessages.appendText(msg + "\n");
                        }
                    }

                    // цикл работы
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.trim().equals(Command.QUIT)) {
                            //System.out.println("Client disconnected");
                            break;
                        }
                        taMessages.appendText(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    setAuthenticated(false);
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

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        msgPanel.setVisible(authenticated);
        msgPanel.setManaged(authenticated);
        authPanel.setVisible(!authenticated);
        authPanel.setManaged(!authenticated);
        if (!authenticated) {
            nickname = "";
        }
        setTitle(nickname);
        taMessages.clear();
    }

    private void setTitle(String nickname) {
        Platform.runLater(() -> {
            stage.setTitle(nickname.equals("") ? "MeowChat" : String.format("MeowChat [%s]", nickname));
        });
    }
}