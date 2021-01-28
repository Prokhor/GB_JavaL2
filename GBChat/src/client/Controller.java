package client;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    public TextArea taMessages;
    @FXML
    public TextField tfMessage;
    @FXML
    public Button btnSendMessage;

    @FXML
    public void sendMessage(ActionEvent actionEvent) {
        if (tfMessage.getText().trim().length() > 0) {
            taMessages.appendText(tfMessage.getText() + "\n\n");
            tfMessage.clear();
            tfMessage.requestFocus();
        }
    }
}
