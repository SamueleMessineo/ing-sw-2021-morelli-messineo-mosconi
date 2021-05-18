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
}
