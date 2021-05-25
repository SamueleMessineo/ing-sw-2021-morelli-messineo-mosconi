package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.LocalClient;
import it.polimi.ingsw.utils.GameUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.concurrent.TimeUnit;

public class OnlineOfflineController implements SceneController {

    private GUI gui;

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

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
}
