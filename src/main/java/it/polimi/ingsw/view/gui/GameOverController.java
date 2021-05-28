package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.utils.ResourceManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Map;

public class GameOverController implements SceneController{
    private GUI gui;
    @FXML
    private Text winner;
    @FXML
    private VBox standingContainer;

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

    @FXML
    void playAgain(ActionEvent event) {
//        gui.setScene("online-offline");
        ResourceManager.playClickSound();
        try {
            gui.start(gui.getStage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
