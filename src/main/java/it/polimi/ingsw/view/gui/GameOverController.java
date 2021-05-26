package it.polimi.ingsw.view.gui;

import javafx.application.Platform;
import javafx.scene.control.TextField;

import java.util.Map;

public class GameOverController implements SceneController{
    public TextField winner;
    public TextField standing;
    private GUI gui;



    public void showWinnerAndStanding(String winner, Map<String, Integer> standing){
        Platform.runLater(()->{
            this.winner.setText("The winner is: " + winner);

           this.standing.setText(standing.toString());
        });

    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
