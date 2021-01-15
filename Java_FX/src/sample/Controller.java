package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Controller {

    @FXML
    private Button btnSend;

    @FXML
    private TextField tfMessage;

    @FXML
    private TextArea taMessages;

    @FXML
    public void btnSendClick(ActionEvent actionEvent) {
        setTextToTextArea();
    }

    @FXML
    public void tfMessageOnKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            setTextToTextArea();
        }
    }

    private void setTextToTextArea() {
        if (!tfMessage.getText().equals("")) {
            taMessages.appendText(String.format("%s\n\n", tfMessage.getText()));
        }
        tfMessage.setText("");
        tfMessage.requestFocus();
    }

    @FXML
    public void miOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void btnOnMouseEntered(MouseEvent mouseEvent) {
        btnSend.setCursor(Cursor.HAND);
    }
}