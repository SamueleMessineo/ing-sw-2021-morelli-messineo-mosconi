package it.polimi.ingsw.view.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

/**
 * The controller for the room details scene.
 */
public class RoomDetailsController implements SceneController {
    private GUI gui;
    @FXML
    private Text roomID;
    @FXML
    private Text playerNumber;
    @FXML
    private VBox playerList;

    /**
     * Sets and displays the room details.
     * @param players the usernames of the players connected to the room.
     * @param np the total number of required players.
     * @param rID the room id.
     */
    public void displayDetails(List<String> players, int np, int rID) {
        Platform.runLater(() -> {
            roomID.setText("Rood ID: " + rID);
            playerNumber.setText("Players: " + players.size() + "/" + np);
            playerList.getChildren().clear();
            for (String username : players) {
                Text usernameText = new Text(username);
                usernameText.getStyleClass().add("light-text");
                playerList.getChildren().add(usernameText);
            }
        });
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
