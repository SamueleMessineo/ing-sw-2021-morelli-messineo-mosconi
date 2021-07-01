package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.utils.ResourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * The controller for the scene that lets the user choose between playing online and offline.
 */
public class OnlineOfflineController implements SceneController {
    private GUI gui;
    @FXML
    private Button offlineButton;
    @FXML
    private Button onlineButton;

    /**
     * Start an offline game.
     * @param event the javafx event.
     */
    @FXML
    void playOffline(ActionEvent event) {
        System.out.println("play offline");
        ResourceManager.playClickSound();
        gui.askUsername();
    }

    /**
     * Start an online game.
     * @param event the javafx event.
     */
    @FXML
    void playOnline(ActionEvent event) {
        System.out.println("play online");
        ResourceManager.playClickSound();
        gui.initializeClient(true);
        gui.setScene("connect");
    }

    /**
     * Toggles on or off the sounds.
     * @param event the javafx event.
     */
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
