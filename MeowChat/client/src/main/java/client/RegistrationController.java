package client;

import commands.Command;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrationController {
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnRegister;
    @FXML
    private TextField tfLogin;
    @FXML
    private PasswordField pfPassword1;
    @FXML
    private PasswordField pfPassword2;
    @FXML
    private TextField tfNickname;
    @FXML
    private TextArea taMessages;

    @FXML
    private void tryToRegistration(ActionEvent actionEvent) {
        String login = tfLogin.getText().trim();
        String password1 = pfPassword1.getText().trim();
        String password2 = pfPassword2.getText().trim();
        String nickname = tfNickname.getText().trim();

        if (password1.equals(password2)) {
            controller.tryToRegistration(login, password1, nickname);
        } else {
            taMessages.appendText(Command.PASSWORDS_NOT_MATCH);
        }
    }

    @FXML
    private void checkFields() {
        btnRegister.setDisable(
                tfLogin.getText().trim().length() == 0 ||
                        pfPassword1.getText().trim().length() == 0 ||
                        pfPassword2.getText().trim().length() == 0 ||
                        tfNickname.getText().trim().length() == 0);
    }

    @FXML
    private void closeWindow(ActionEvent actionEvent) {
        stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private Controller controller;
    private Stage stage;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void registerSuccess(){
        taMessages.appendText(Command.REGISTER_CLIENT_SUCCESS);
        tfLogin.clear();
        pfPassword1.clear();
        pfPassword2.clear();
        tfNickname.clear();
        checkFields();
    }

    public void registerFailed(){
        taMessages.appendText(Command.REGISTER_CLIENT_FAILED);
        tfLogin.clear();
        pfPassword1.clear();
        pfPassword2.clear();
        tfNickname.clear();
        checkFields();
    }
}