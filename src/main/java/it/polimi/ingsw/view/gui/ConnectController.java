package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ConnectController implements SceneController {
    private GUI gui;

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    @FXML
    private TextField ipInput;

    @FXML
    private TextField portInput;

    @FXML
    private Text errorMessage;

    @FXML
    void connect(ActionEvent event) {
        System.out.println("try connection");
        try {
            gui.getClient().connect(ipInput.getText(), Integer.parseInt(portInput.getText()));
            System.out.println("connection successful");
            gui.setScene("select-game");
        } catch (Exception e) {
            System.out.println("CONNECTION FAILED");
            errorMessage.setText("CONNECTION FAILED, try again");
        }
    }
}
