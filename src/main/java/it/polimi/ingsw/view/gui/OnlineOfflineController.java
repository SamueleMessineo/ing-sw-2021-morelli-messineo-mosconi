package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class OnlineOfflineController implements SceneController {
    private GUI gui;
    @FXML
    private Button offlineButton;
    @FXML
    private Button onlineButton;

    @FXML
    void playOffline(ActionEvent event) {
        System.out.println("play offline");
        gui.askUsername();
    }

    @FXML
    void playOnline(ActionEvent event) {
        System.out.println("play online");
        gui.initializeClient(true);
        gui.setScene("connect");
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
