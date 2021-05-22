package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.LeaderCard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

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
    private Text playerInfo;

    @FXML
    void hide(MouseEvent event) {
        marblesContainer.setVisible(false);
    }

    public void load(Game game) {
        gameState = game;
        playerInfo.setText(gui.getUsername() + " - " + game.getPlayerByUsername(gui.getUsername()).getVP() + " points");
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
            AnchorPane tabContainer = new AnchorPane();
            HBox leadersContainer = new HBox();
            leadersContainer.setSpacing(50);
            leadersContainer.setPrefWidth(287);
            leadersContainer.setPrefHeight(188);
            leadersContainer.setAlignment(Pos.CENTER);

            for (LeaderCard leaderCard : p.getLeaderCards()) {
                Image leaderImage = null;
                if (p.getUsername().equals(gui.getUsername())) {
                    leaderImage = new Image(getClass().getClassLoader().getResourceAsStream(
                            "images/leaders/leader_" + leaderCard.getEffectScope().toLowerCase() + "_" + leaderCard.getEffectObject().name().toLowerCase() + ".png"));
                } else {
                    leaderImage = new Image(getClass().getClassLoader().getResourceAsStream(
                            "images/leaders/leader_back.png"));
                }
                ImageView leaderImageView = new ImageView(leaderImage);
                leaderImageView.setPreserveRatio(true);
                leaderImageView.setFitWidth(200);
                leaderImageView.setFitHeight(160);
                leadersContainer.getChildren().add(leaderImageView);
            }
            leadersContainer.setLayoutX(17);
            leadersContainer.setLayoutY(239);
            tabContainer.getChildren().add(leadersContainer);
            playerTab.setContent(tabContainer);
            tabPane.getTabs().add(playerTab);

            if (p.getUsername().equals(gui.getUsername())) {
                tabPane.getSelectionModel().select(playerTab);
            }
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
