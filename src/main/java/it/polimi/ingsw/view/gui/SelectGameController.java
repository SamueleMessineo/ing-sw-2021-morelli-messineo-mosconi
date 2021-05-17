package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.setup.CreateRoomMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SelectGameController implements SceneController {
    private GUI gui;

    @FXML
    private Button createGameButton;

    @FXML
    private Button joinRandomButton;

    @FXML
    private Button joinIDButton;

    @FXML
    void createGame(ActionEvent event) {
        System.out.println("create game");
        gui.getClient().sendMessage(new CreateRoomMessage(true, 2, "alberto"));
    }

    @FXML
    void joinByID(ActionEvent event) {

    }

    @FXML
    void joinRandom(ActionEvent event) {

    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}