package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.utils.ResourceManager;
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
        ResourceManager.playClickSound();
        gui.askUsername();
    }

    @FXML
    void playOnline(ActionEvent event) {
        System.out.println("play online");
        ResourceManager.playClickSound();
        gui.initializeClient(true);
        gui.setScene("connect");
    }

    @FXML
    void toggleSounds(ActionEvent event) {
        ResourceManager.playClickSound();
        ResourceManager.toggleSound();
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
