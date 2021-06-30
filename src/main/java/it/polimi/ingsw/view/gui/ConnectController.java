package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.utils.ResourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * Controller for the scene that lets the user enter the server details and connect.
 */
public class ConnectController implements SceneController {
    private GUI gui;
    @FXML
    private TextField ipInput;
    @FXML
    private TextField portInput;
    @FXML
    private Text errorMessage;

    /**
     * Tries to connect to the server.
     * @param event the javafx event.
     */
    @FXML
    void connect(ActionEvent event) {
        ResourceManager.playClickSound();
        try {
            String ip;
            int port;
            if (ipInput.getText().equals("") && portInput.getText().equals("")) {
                ip = "localhost";
                port = 31415;
            } else {
                ip = ipInput.getText();
                port = Integer.parseInt(portInput.getText());
            }
            System.out.println("try connection to " + ip + " on port " + port);
            gui.getClient().connect(ip, port);
            System.out.println("connection successful");
            gui.setScene("setup-game");
        } catch (Exception e) {
            System.out.println("CONNECTION FAILED");
            errorMessage.setText("CONNECTION FAILED, try again");
        }
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
