package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class GameViewController implements SceneController {
    private GUI gui;
    private Game gameState;
    @FXML
    private AnchorPane marblesContainer;
    @FXML
    private AnchorPane cardsContainer;
    @FXML
    private TabPane tabPane;

    @FXML
    void hide(MouseEvent event) {
        marblesContainer.setVisible(false);
    }

    public void load(Game game) {
        gameState = game;
        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("scenes/cards-grid.fxml"));
        GridPane cardsGrid;
        GridPane cardsGrid2;
        try {
            cardsGrid = loader.load();
            ((CardsGridController) loader.getController()).setCards(game.getMarket().getCardsGrid());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            // add cards grid
            cardsGrid.setScaleX(0.6);
            cardsGrid.setScaleY(0.6);
            cardsContainer.getChildren().add(cardsGrid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // populate player tabs
        tabPane.getTabs().clear();
        for (Player p : game.getPlayers()) {
            Tab playerTab = new Tab();
            playerTab.setText(p.getUsername() + ": " + p.getVP() + " points");
            tabPane.getTabs().add(playerTab);
        }
    }

    @FXML
    void viewCards(MouseEvent event) {
        ((CardsMarketController)gui.getSceneController("cards-market")).load(gameState.getMarket().getCardsGrid());
        gui.setScene("cards-market");
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
