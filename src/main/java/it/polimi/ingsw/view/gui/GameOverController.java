package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.utils.ResourceManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Map;

/**
 * The controller for the game over scene.
 */
public class GameOverController implements SceneController{
    private GUI gui;
    @FXML
    private Text winner;
    @FXML
    private VBox standingContainer;

    /**
     * Displays the winner and the standings.
     * @param winner the username of the winner.
     * @param standing the standings.
     */
    public void showWinnerAndStanding(String winner, Map<String, Integer> standing){
        Platform.runLater(()->{
            this.winner.setText("The winner is:\n" + winner);
            this.standingContainer.getChildren().clear();
            for (Map.Entry<String, Integer> entry : standing.entrySet()) {
                Text t = new Text(entry.getKey() + ": " + entry.getValue() + " points");
                t.setFill(Color.BLACK);
                standingContainer.getChildren().add(t);
            }
        });
    }

    /**
     * Play another game.
     * @param event the javafx event.
     */
    @FXML
    void playAgain(ActionEvent event) {
        ResourceManager.playClickSound();
        try {
            gui.start(gui.getStage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Quits the application.
     * @param event the javafx event.
     */
    @FXML
    void quit(ActionEvent event) {
        ResourceManager.playClickSound();
        System.exit(0);
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
