package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Shelf;
import it.polimi.ingsw.model.shared.*;
import it.polimi.ingsw.network.game.SelectMoveResponseMessage;
import it.polimi.ingsw.utils.GameUtils;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;

public class GameBoardController implements SceneController {
    private GUI gui;
    private Game gameState;
    private HBox leadersContainer;
    private AnchorPane cardStacksContainer;
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
                leadersContainer = new HBox();
                leadersContainer.setSpacing(50);
                leadersContainer.setPrefWidth(287);
                leadersContainer.setPrefHeight(188);
                leadersContainer.setAlignment(Pos.CENTER_LEFT);
                leadersContainer.setPadding(new Insets(0,0,0,9));
                for (LeaderCard leaderCard : p.getLeaderCards()) {
                    ImageView leaderImageView = null;
                    if (p.getUsername().equals(gui.getUsername())) {
                        leaderImageView = GameUtils.getImageView(leaderCard);
                    } else {
                        Image leaderImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                                "images/leaders/leader_back.png")));
                        leaderImageView = new ImageView(leaderImage);
                        leaderImageView.setPreserveRatio(true);
                    }
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
                cardStacksContainer = new AnchorPane();
                cardStacksContainer.setLayoutX(365);
                cardStacksContainer.setLayoutY(226);
                cardStacksContainer.setOnMouseClicked(this::viewCardsAndProductions);
                FXMLLoader playerCardStacksLoader = new FXMLLoader(
                        getClass().getClassLoader().getResource("scenes/player-card-stacks.fxml"));
                try {
                    cardStacksContainer.getChildren().add(playerCardStacksLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ((PlayerCardStacksController)playerCardStacksLoader.getController()).load(p.getPlayerBoard().getCardStacks());
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
                            ImageView resourceImageView = GameUtils.getImageView(shelf.getResourceType());
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

                // display faith track position
                VBox faithTrackPositionBox = new VBox();
                faithTrackPositionBox.setPrefSize(40,40);
                faithTrackPositionBox.setAlignment(Pos.CENTER);
                List<Integer> positionCoordinates = GameUtils.getFaithTrackPositionCoordinates(p.getFaithTrack().getPosition());
                if (positionCoordinates != null) {
                    faithTrackPositionBox.setLayoutX(positionCoordinates.get(0));
                    faithTrackPositionBox.setLayoutY(positionCoordinates.get(1));
                }
                Image posImage = new Image(Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream("images/faith/cross.png")));
                ImageView posImageView = new ImageView(posImage);
                posImageView.setPreserveRatio(true);
                posImageView.setFitWidth(40);
                posImageView.setFitHeight(40);
                faithTrackPositionBox.getChildren().add(posImageView);
                tabContainer.getChildren().add(faithTrackPositionBox);

                // display pope's tiles
                for (int i = 0; i < 3; i++) {
                    PopesFavorTile tile = p.getFaithTrack().getPopesFavorTiles().get(i);
                    VBox tileBox = new VBox();
                    tileBox.setPrefSize(65,65);
                    tileBox.setAlignment(Pos.CENTER);
                    if (tile.getState() != PopesFavorTileState.DISCARDED) {
                        Image tileImage = new Image(Objects.requireNonNull(getClass().getClassLoader()
                                .getResourceAsStream("images/faith/tile_" + tile.getState().name() + "_" + i + ".png")));
                        ImageView tileImageView = new ImageView(tileImage);
                        tileImageView.setPreserveRatio(true);
                        tileImageView.setFitWidth(65);
                        tileImageView.setFitHeight(65);
                        tileBox.getChildren().add(tileImageView);
                    }
                    List<Integer> tileCoordinates = GameUtils.getPopeTileCoordinates(i);
                    if (tileCoordinates != null) {
                        tileBox.setLayoutX(tileCoordinates.get(0));
                        tileBox.setLayoutY(tileCoordinates.get(1));
                    }
                    tabContainer.getChildren().add(tileBox);
                }


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

    public void displayPossibleMoves(List<String> moves) {
        Platform.runLater(() -> {
            String defaultStyle="-fx-cursor: hand;";
            String possibleMoveStyle =
                    "-fx-border-style: solid inside;" +
                    "-fx-border-width: 3;" +
                    "-fx-border-radius: 15;" +
                    "-fx-border-color: red;" +
                    "-fx-cursor: hand;";
            marblesContainer.setStyle(defaultStyle);
            cardsContainer.setStyle(defaultStyle);
            leadersContainer.setStyle(defaultStyle);
            cardStacksContainer.setStyle(defaultStyle);
            endTurnButton.setDisable(true);
            if (moves.contains("GET_MARBLES")) {
                marblesContainer.setStyle(possibleMoveStyle);
            }
            if (moves.contains("BUY_CARD")) {
                cardsContainer.setStyle(possibleMoveStyle);
            }
            if (moves.contains("DROP_LEADER") || moves.contains("PLAY_LEADER")) {
                leadersContainer.setStyle(possibleMoveStyle);
            }
            if (moves.contains("ACTIVATE_PRODUCTION")) {
                cardStacksContainer.setStyle(possibleMoveStyle);
            }
            if (moves.contains("END_TURN")) {
                endTurnButton.setDisable(false);
            }
        });
    }

    @FXML
    void viewCardMarket(MouseEvent event) {
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
    void viewCardsAndProductions(MouseEvent event) {
        ((CardsProductionController)gui.getSceneController("cards-production")).display(
                gameState.getPlayerByUsername(gui.getUsername()).getPlayerBoard().getCardStacks()
        );
        gui.setScene("cards-production");
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
