package client;

import commands.Command;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private HBox authPanel;
    @FXML
    private HBox msgPanel;
    @FXML
    private TextField tfLogin;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private ListView<String> lvClients;
    @FXML
    private TextArea taMessages;
    @FXML
    private TextField tfMessage;

    @FXML
    private void sendMessage(ActionEvent actionEvent) {
        try {
            if (tfMessage.getText().trim().length() > 0) {
                out.writeUTF(tfMessage.getText());
                tfMessage.clear();
                tfMessage.requestFocus();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void tryToAuth(ActionEvent actionEvent) {
        checkSocket();
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

    @FXML
    public void lvClientsClicked(MouseEvent mouseEvent) {
        String receiver = lvClients.getSelectionModel().getSelectedItem();
        tfMessage.setText(String.format("%s %s ", Command.PERSONAL_MSG, receiver));
    }

    @FXML
    public void registrationClient(ActionEvent actionEvent) {
        if (regStage == null){
            createRegWindow();
        }
        regStage.show();
    }

    private final String SERVER_ADDRESS = "localhost";
    private final int PORT = 8880;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean authenticated;
    private String nickname;
    private Stage stage;
    private Stage regStage;
    private RegistrationController registrationController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            stage = (Stage) taMessages.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                if (socket != null && !socket.isClosed()){
                    try {
                        out.writeUTF(Command.QUIT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
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
                                taMessages.appendText(String.format(Command.SUCCESS_CONNECT, nickname));
                                break;
                            }

                            if (msg.trim().equals(Command.QUIT)) {
                                throw new RuntimeException(Command.KICKED_BY_SERVER);
                            }

                            if (msg.equals(Command.REGISTER_SUCCESS)){
                                registrationController.registerSuccess();
                            }
                            if (msg.equals(Command.REGISTER_FAILED)){
                                registrationController.registerFailed();
                            }
                        } else {
                            taMessages.appendText(msg + "\n");
                        }
                    }

                    // цикл работы
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith("/")){
                            if (msg.trim().equals(Command.QUIT)) {
                                break;
                            }
                            if (msg.startsWith(Command.CLIENT_LIST)) {
                                String[] tokens = msg.split("\\s");
                                Platform.runLater(() -> {
                                    lvClients.getItems().clear();
                                    for (int i = 1; i < tokens.length; i++) {
                                        lvClients.getItems().add(tokens[i]);
                                    }
                                });
                            }
                        } else {
                            taMessages.appendText(msg + "\n");
                        }
                    }
                } catch (RuntimeException e){
                    System.out.println(e.getMessage());
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
        lvClients.setVisible(authenticated);
        lvClients.setManaged(authenticated);
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

    private void createRegWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/registration.fxml"));
            Parent root = fxmlLoader.load();
            regStage = new Stage();
            regStage.setTitle("MeowChat registration");
            regStage.setScene(new Scene(root, 300, 250));
            registrationController = fxmlLoader.getController();
            registrationController.setController(this);
            regStage.initModality(Modality.APPLICATION_MODAL);
            regStage.setResizable(false);
            regStage.initStyle(StageStyle.UNIFIED);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void tryToRegistration(String login, String password, String nickname){
        checkSocket();
        String msg = String.format("%s %s %s %s", Command.REGISTER_CLIENT, login, password, nickname);
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkSocket(){
        if (socket == null || socket.isClosed()) {
            connect();
        }
    }
}