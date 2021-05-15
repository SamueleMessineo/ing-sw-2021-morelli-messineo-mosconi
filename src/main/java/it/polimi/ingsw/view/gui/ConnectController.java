package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ConnectController implements SceneController {
    private GUI gui;

    @FXML
    private TextField ipInput;

    @FXML
    private TextField portInput;

    @FXML
    private Text errorMessage;

    @FXML
    void connect(ActionEvent event) {
        errorMessage.setText("trying to connect to " + ipInput.getText() + " on port " + portInput.getText());
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
