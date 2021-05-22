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
    private VBox marblesContainer;
    @FXML
    private VBox cardsContainer;
    @FXML
    private TabPane tabPane;

    @FXML
    void hide(MouseEvent event) {
        marblesContainer.setVisible(false);
    }

    public void load(Game game) {
        gameState = game;
        FXMLLoader cardsLoader = new FXMLLoader(
                getClass().getClassLoader().getResource("scenes/cards-grid.fxml"));
        FXMLLoader marblesLoader = new FXMLLoader(
                getClass().getClassLoader().getResource("scenes/marbles-grid.fxml"));
        GridPane cardsGrid;
        VBox marblesGrid;
        try {
            cardsGrid = cardsLoader.load();
            marblesGrid = marblesLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        ((CardsGridController) cardsLoader.getController()).setCards(game.getMarket().getCardsGrid());
        cardsContainer.getChildren().add(cardsGrid);
        // populate player tabs
        tabPane.getTabs().clear();
        for (Player p : game.getPlayers()) {
            Tab playerTab = new Tab();
            playerTab.setText(p.getUsername() + ": " + p.getVP() + " points");
            tabPane.getTabs().add(playerTab);
        }

        ((MarblesGridController) marblesLoader.getController()).setMarbles(
                game.getMarket().getMarbleStructure().getMarbles(),
                game.getMarket().getMarbleStructure().getExtraMarble());
        marblesContainer.getChildren().add(marblesGrid);
    }

    @FXML
    void viewCards(MouseEvent event) {
        ((CardsMarketController)gui.getSceneController("cards-market")).load(gameState.getMarket().getCardsGrid());
        gui.setScene("cards-market");
    }

    @FXML
    void viewMarbles(MouseEvent event) {
        ((MarblesMarketController)gui.getSceneController("marbles-market")).load(
                gameState.getMarket().getMarbleStructure().getMarbles(),
                gameState.getMarket().getMarbleStructure().getExtraMarble());
        gui.setScene("marbles-market");
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
