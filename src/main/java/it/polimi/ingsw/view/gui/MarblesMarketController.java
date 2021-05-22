package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarketCardStack;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class MarblesMarketController implements SceneController{
        private GUI gui;
        @FXML
        private VBox vbox;

        public void load(List<Marble> marbles, Marble extra) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource("scenes/marbles-grid.fxml"));
            VBox marblesGrid;
            try {
                marblesGrid = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            ((MarblesGridController) loader.getController()).setMarbles(marbles, extra);
            marblesGrid.setScaleX(2);
            marblesGrid.setScaleY(2);
            vbox.getChildren().add(0, marblesGrid);
        }

        @FXML
        void cancel(ActionEvent event) {
            vbox.getChildren().remove(0);
            gui.setScene("game-board");
        }

        @Override
        public void setGUI(GUI gui) {
            this.gui = gui;
        }
    }


