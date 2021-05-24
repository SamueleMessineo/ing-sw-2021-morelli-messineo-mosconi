package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerCardStack;
import it.polimi.ingsw.model.player.Shelf;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.game.SelectMoveResponseMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class GameBoardController implements SceneController {
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
    private Button endTurnButton;

    @FXML
    void hide(MouseEvent event) {
        marblesContainer.setVisible(false);
    }

    public void load(Game game) {
        Platform.runLater(() -> {
            gameState = game;
            playerInfo.setText(gui.getUsername() + " - " + gameState.getPlayerByUsername(gui.getUsername()).getVP() + " points");
            FXMLLoader cardsLoader = new FXMLLoader(
                    getClass().getClassLoader().getResource("scenes/cards-grid.fxml"));
            FXMLLoader marblesLoader = new FXMLLoader(
                    getClass().getClassLoader().getResource("scenes/marbles-grid.fxml"));
            cardsContainer.getChildren().clear();
            marblesContainer.getChildren().clear();
            GridPane cardsGrid;
            VBox marblesGrid;
            try {
                cardsGrid = cardsLoader.load();
                marblesGrid = marblesLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            ((CardsGridController) cardsLoader.getController()).setCards(gameState.getMarket().getCardsGrid());
            cardsContainer.getChildren().add(cardsGrid);
            // populate player tabs
            tabPane.getTabs().clear();
            for (Player p : gameState.getPlayers()) {
                Tab playerTab = new Tab();
                playerTab.setText(p.getUsername() + ": " + p.getVP() + " points");
                AnchorPane tabContainer = new AnchorPane();
                // display leader cards
                HBox leadersContainer = new HBox();
                leadersContainer.setSpacing(50);
                leadersContainer.setPrefWidth(287);
                leadersContainer.setPrefHeight(188);
                leadersContainer.setAlignment(Pos.CENTER);
                for (LeaderCard leaderCard : p.getLeaderCards()) {
                    Image leaderImage = null;
                    if (p.getUsername().equals(gui.getUsername())) {
                        leaderImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                                "images/leaders/leader_" + leaderCard.getEffectScope().toLowerCase() + "_" + leaderCard.getEffectObject().name().toLowerCase() + ".png")));
                    } else {
                        leaderImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                                "images/leaders/leader_back.png")));
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

                leadersContainer.setCursor(Cursor.HAND);
                leadersContainer.setOnMouseClicked(this::viewLeaders);

                // display development card stacks
                HBox cardStacksContainer = new HBox();
                cardStacksContainer.setPrefSize(450, 250);
                cardStacksContainer.setAlignment(Pos.CENTER);
                cardStacksContainer.setLayoutX(365);
                cardStacksContainer.setLayoutY(226);
                for (PlayerCardStack playerCardStack : p.getPlayerBoard().getCardStacks()) {
                    AnchorPane singleCardStackContainer = new AnchorPane();
                    singleCardStackContainer.setPrefSize(150, 210);
                    for (int i = 0; i < playerCardStack.size(); i++) {
                        DevelopmentCard card = playerCardStack.get(i);
                        Image cardImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                            "images/development/development_" + card.getCardType().name().toLowerCase() +
                                    "_" + card.getScore() + ".png"
                        )));
                        ImageView cardImageView = new ImageView(cardImage);
                        cardImageView.setPreserveRatio(true);
                        cardImageView.setFitHeight(160);
                        cardImageView.setFitWidth(200);
                        cardImageView.setLayoutX(
                                75-(cardImageView.getLayoutBounds().getWidth()/2)
                        );
                        cardImageView.setLayoutY(200-160-(25*i));
                        singleCardStackContainer.getChildren().add(cardImageView);
                    }
                    cardStacksContainer.getChildren().add(singleCardStackContainer);
                }
                tabContainer.getChildren().add(cardStacksContainer);

                // display the warehouse
                VBox warehouseContainer = new VBox();
                for (String shelfName : p.getPlayerBoard().getWarehouse().getShelfNames()) {
                    Shelf shelf = p.getPlayerBoard().getWarehouse().getShelf(shelfName);
                    if (Arrays.asList("bottom", "middle", "top").contains(shelfName)) {
                        HBox shelfResourcesContainer = new HBox();
                        shelfResourcesContainer.setPrefSize(129, 46);
                        shelfResourcesContainer.setAlignment(Pos.CENTER);
                        shelfResourcesContainer.setPadding(new Insets(0, 10, 0, 10));
                        shelfResourcesContainer.setSpacing(10);
                        for (int i = 0; i < shelf.getResourceNumber(); i++) {
                            Image resourceImage=new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                                    "images/punchboard/" + shelf.getResourceType().name().toLowerCase() + ".png")));
                            ImageView resourceImageView = new ImageView(resourceImage);
                            resourceImageView.setPreserveRatio(true);
                            resourceImageView.setFitWidth(40);
                            resourceImageView.setFitHeight(40);
                            shelfResourcesContainer.getChildren().add(resourceImageView);
                        }
                        warehouseContainer.getChildren().add(shelfResourcesContainer);
                    }
                }
                warehouseContainer.setPrefSize(165, 185);
                warehouseContainer.setLayoutX(9);
                warehouseContainer.setLayoutY(501);
                warehouseContainer.setPadding(new Insets(0,0,8,0));
                warehouseContainer.setSpacing(10);
                warehouseContainer.setAlignment(Pos.BOTTOM_CENTER);
                warehouseContainer.setOnMouseClicked(this::viewWarehouse);
                tabContainer.getChildren().add(warehouseContainer);

                // display faith track
                VBox faithTrackPositionBox = new VBox();
                faithTrackPositionBox.setPrefSize(40,40);
                faithTrackPositionBox.setAlignment(Pos.CENTER);
                List<Integer> coords = getFaithTrackCoordinatesFromPosition(p.getFaithTrack().getPosition());
                if (coords != null) {
                    faithTrackPositionBox.setLayoutX(coords.get(0));
                    faithTrackPositionBox.setLayoutY(coords.get(1));
                }
                Image posImage = new Image(Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream("images/board/cross.png")));
                ImageView posImageView = new ImageView(posImage);
                posImageView.setPreserveRatio(true);
                posImageView.setFitWidth(40);
                posImageView.setFitHeight(40);
                faithTrackPositionBox.getChildren().add(posImageView);

                tabContainer.getChildren().add(faithTrackPositionBox);

                playerTab.setContent(tabContainer);
                tabPane.getTabs().add(playerTab);
                // select the tab corresponding to the player itself
                if (p.getUsername().equals(gui.getUsername())) {
                    tabPane.getSelectionModel().select(playerTab);
                }
            }
            // display the marbles
            ((MarblesGridController) marblesLoader.getController()).setMarbles(
                    gameState.getMarket().getMarbleStructure().getMarbles(),
                    gameState.getMarket().getMarbleStructure().getExtraMarble());
            marblesContainer.getChildren().add(marblesGrid);
        });
    }

    private List<Integer> getFaithTrackCoordinatesFromPosition(int pos) {
        switch (pos) {
            case 0:
                return Arrays.asList(12, 103);
            case 1:
                return Arrays.asList(54, 103);
            case 2:
                return Arrays.asList(96, 103);
            case 3:
                return Arrays.asList(96, 61);
            case 4:
                return Arrays.asList(96, 19);
            case 5:
                return Arrays.asList(139, 19);
            case 6:
                return Arrays.asList(181, 19);
            case 7:
                return Arrays.asList(223, 19);
            case 8:
                return Arrays.asList(265, 19);
            case 9:
                return Arrays.asList(308, 19);
            case 10:
                return Arrays.asList(308, 61);
            case 11:
                return Arrays.asList(308, 103);
            case 12:
                return Arrays.asList(350, 103);
            case 13:
                return Arrays.asList(392, 103);
            case 14:
                return Arrays.asList(434, 103);
            case 15:
                return Arrays.asList(476, 103);
            case 16:
                return Arrays.asList(518, 103);
            case 17:
                return Arrays.asList(518, 61);
            case 18:
                return Arrays.asList(518, 19);
            case 19:
                return Arrays.asList(561, 19);
            case 20:
                return Arrays.asList(603, 19);
            case 21:
                return Arrays.asList(645, 19);
            case 22:
                return Arrays.asList(687, 19);
            case 23:
                return Arrays.asList(729, 19);
            case 24:
                return Arrays.asList(771, 19);
            default:
                return null;
        }
    }

    public void displayPossibleMoves(List<String> moves) {
        Platform.runLater(() -> {
            String possibleMoveStyle =
                    "-fx-border-style: solid inside;" +
                    "-fx-border-width: 3;" +
                    "-fx-border-radius: 15;" +
                    "-fx-border-color: red;";
            marblesContainer.setStyle("");
            cardsContainer.setStyle("");
            endTurnButton.setDisable(true);
            if (moves.contains("GET_MARBLES")) {
                marblesContainer.setStyle(possibleMoveStyle);
            }
            if (moves.contains("BUY_CARD")) {
                cardsContainer.setStyle(possibleMoveStyle);
            }
            if (moves.contains("END_TURN")) {
                endTurnButton.setDisable(false);
            }
        });
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

    @FXML
    void viewLeaders(MouseEvent event) {
        System.out.println("view leaders");
        ((LeaderCardsController)gui.getSceneController("leader-cards")).load(gameState.getPlayerByUsername(gui.getUsername()).getLeaderCards(), "SHOW");
        try{
            gui.setScene("leader-cards");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void viewWarehouse(MouseEvent event) {
        System.out.println("view warehouse");
    }

    @FXML
    void endTurn(ActionEvent event) {
        gui.getClient().sendMessage(new SelectMoveResponseMessage("END_TURN"));
        displayPossibleMoves(new ArrayList<>());
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
