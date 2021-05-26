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
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;

public class GameBoardController implements SceneController {
    private GUI gui;
    private Game gameState;
    @FXML
    private HBox leadersContainer;
    private AnchorPane cardStacksContainer;
    private AnchorPane basicProductionContainer;
    private HBox warehouseContainer;
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
            playerInfo.setText(gui.getUsername() + ": " + gameState.getPlayerByUsername(gui.getUsername()).getVP() + " points");
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
                leadersContainer.setAlignment(Pos.CENTER_LEFT);
                leadersContainer.setPadding(new Insets(0,0,0,9));
                List<LeaderCard> allLeaders = new ArrayList<>();
                allLeaders.addAll(p.getLeaderCards());
                allLeaders.addAll(p.getPlayedLeaderCards());
                for (LeaderCard leaderCard : allLeaders) {
                    ImageView leaderImageView = null;
                    if (p.getUsername().equals(gui.getUsername())) {
                        leaderImageView = GameUtils.getImageView(leaderCard);
                    } else {
                        if (p.getPlayedLeaderCards().contains(leaderCard)) {
                            leaderImageView = GameUtils.getImageView(leaderCard);
                        } else {
                            Image leaderImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                                    "images/leaders/leader_back.png")));
                            leaderImageView = new ImageView(leaderImage);
                            leaderImageView.setPreserveRatio(true);
                        }

                    }
                    leaderImageView.setFitWidth(200);
                    leaderImageView.setFitHeight(160);
                    if(!p.getPlayedLeaderCards().contains(leaderCard)){
                        GameUtils.setDarkImageView(leaderImageView, 0.5);
                    }

                    leadersContainer.getChildren().add(leaderImageView);
                }
                leadersContainer.setLayoutX(17);
                leadersContainer.setLayoutY(239);
                tabContainer.getChildren().add(leadersContainer);

                // display development card stacks
                AnchorPane cardStacksContainer = new AnchorPane();
                cardStacksContainer.setLayoutX(365);
                cardStacksContainer.setLayoutY(210);
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
                HBox warehouseContainer = new HBox();
                VBox normalShelvesContainer = new VBox();
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
                        normalShelvesContainer.getChildren().add(shelfResourcesContainer);
                    }
                }
                normalShelvesContainer.setPrefSize(165, 177);
                normalShelvesContainer.setSpacing(10);
                normalShelvesContainer.setAlignment(Pos.BOTTOM_CENTER);
                warehouseContainer.getChildren().add(normalShelvesContainer);
                VBox extraShelvesContainer = new VBox();
                for (LeaderCard playedLeaderCard : p.getPlayedLeaderCards()) {
                    if (playedLeaderCard.getEffectScope().equals("Storage")) {
                        Image extraShelfImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                                "images/board/extra_" + playedLeaderCard.getEffectObject().name().toLowerCase() + ".png")));
                        ImageView extraShelfImageView = new ImageView(extraShelfImage);
                        extraShelfImageView.setPreserveRatio(true);
                        extraShelfImageView.setFitWidth(135);
                        extraShelvesContainer.getChildren().add(extraShelfImageView);
                    }
                }
                extraShelvesContainer.setPrefSize(147, 177);
                extraShelvesContainer.setSpacing(25);
                extraShelvesContainer.setAlignment(Pos.CENTER);
                warehouseContainer.getChildren().add(extraShelvesContainer);
                warehouseContainer.setLayoutX(9);
                warehouseContainer.setLayoutY(501);
                warehouseContainer.setSpacing(36);
                warehouseContainer.setAlignment(Pos.CENTER);
                tabContainer.getChildren().add(warehouseContainer);

                // display the strongbox
                GridPane strongboxContainer = new GridPane();
                strongboxContainer.setPrefSize(225, 188);
                strongboxContainer.setLayoutX(389);
                strongboxContainer.setLayoutY(498);
                Map<Resource, Integer> strongboxResources = p.getPlayerBoard().getStrongbox().getResources();
                for (Map.Entry<Resource, Integer> entry : strongboxResources.entrySet()) {
                    int i = Arrays.asList(Resource.COIN, Resource.SHIELD, Resource.SERVANT, Resource.STONE)
                            .indexOf(entry.getKey());
                    ImageView resourceImage = GameUtils.getImageView(entry.getKey());
                    HBox resourceContainer = new HBox();
                    resourceContainer.setAlignment(Pos.CENTER);
                    resourceContainer.setPrefSize(112.5, 94);
                    resourceImage.setFitWidth(40);
                    resourceImage.setFitHeight(40);
                    Text amountText = new Text(entry.getValue() + "x");
                    amountText.setFont(Font.font("System", FontWeight.BLACK, 18));
                    amountText.setStrokeWidth(1);
                    amountText.setStroke(Color.BLACK);
                    amountText.setFill(Color.WHITE);
                    amountText.setEffect(new DropShadow());
                    resourceContainer.getChildren().addAll(amountText, resourceImage);
                    strongboxContainer.add(resourceContainer, i/2, i%2);
                }
                tabContainer.getChildren().add(strongboxContainer);

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
                    tileBox.setPrefSize(65, 65);
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
                // display basic production container
                basicProductionContainer = new AnchorPane();
                basicProductionContainer.setPrefSize(200, 185);
                basicProductionContainer.setLayoutX(627);
                basicProductionContainer.setLayoutY(500);
                tabContainer.getChildren().add(basicProductionContainer);

                playerTab.setContent(tabContainer);
                tabPane.getTabs().add(playerTab);

                this.cardsContainer.setCursor(Cursor.HAND);
                this.marblesContainer.setCursor(Cursor.HAND);
                // select the tab corresponding to the player itself and apply things
                if (p.getUsername().equals(gui.getUsername())) {
                    tabPane.getSelectionModel().select(playerTab);
                    this.leadersContainer = leadersContainer;
                    this.leadersContainer.setCursor(Cursor.HAND);
                    this.leadersContainer.setOnMouseClicked(this::viewLeaders);
                    this.cardStacksContainer = cardStacksContainer;
                    this.cardStacksContainer.setOnMouseClicked(this::viewCardsAndProductions);
                    this.cardStacksContainer.setCursor(Cursor.HAND);
                    this.warehouseContainer = warehouseContainer;
                    this.warehouseContainer.setOnMouseClicked(this::viewWarehouse);
                    this.warehouseContainer.setCursor(Cursor.HAND);
                    this.basicProductionContainer.setCursor(Cursor.HAND);
                    this.basicProductionContainer.setOnMouseClicked(this::viewCardsAndProductions);
                }
            }
            if(gameState.getLorenzoIlMagnifico()!=null){
                Tab playerTab = new Tab();
                playerTab.setText("Lorenzo il Magnifico" + ": " + game.getLorenzoIlMagnifico().getVP() + " points");
                AnchorPane tabContainer = new AnchorPane();

                Image lorenzoPaint = new Image(Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream("images/lorenzoIlMagnificoPaint.png")));
                ImageView lorenzoView = new ImageView(lorenzoPaint);
                lorenzoView.setFitHeight(552);
                lorenzoView.setFitWidth(854);
                lorenzoView.setY(168);
                tabContainer.getChildren().add(lorenzoView);


                // display faith track position
                VBox faithTrackPositionBox = new VBox();
                faithTrackPositionBox.setPrefSize(40,40);
                faithTrackPositionBox.setAlignment(Pos.CENTER);
                List<Integer> positionCoordinates = GameUtils.getFaithTrackPositionCoordinates(game.getLorenzoIlMagnifico().getFaithTrack().getPosition());
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


                playerTab.setContent(tabContainer);
                tabPane.getTabs().add(playerTab);
            }
            // display the marbles
            ((MarblesGridController) marblesLoader.getController()).setMarbles(
                    gameState.getMarket().getMarbleStructure().getMarbles(),
                    gameState.getMarket().getMarbleStructure().getExtraMarble());
            marblesContainer.getChildren().add(marblesGrid);
        });
    }

    public void displayPossibleMoves(List<String> moves) {
        GameUtils.debug("displaying" + moves.toString());
        Platform.runLater(() -> {
            String possibleMoveStyle =
                    "-fx-border-style: solid inside;" +
                    "-fx-border-width: 3;" +
                    "-fx-border-radius: 15;" +
                    "-fx-border-color: red;" +
                    "-fx-cursor: hand;";
            marblesContainer.setStyle("");
            cardsContainer.setStyle("");
            leadersContainer.setStyle("");
            cardStacksContainer.setStyle("");
            warehouseContainer.setStyle("");
            basicProductionContainer.setStyle("");
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
                if (gameState.getPlayerByUsername(gui.getUsername()).canActivateBasicProduction()) {
                    basicProductionContainer.setStyle(possibleMoveStyle);
                }
                if (gameState.getPlayerByUsername(gui.getUsername()).canActivateProduction()) {
                    cardStacksContainer.setStyle(possibleMoveStyle);
                }
            }
            if (moves.contains("SWITCH_SHELVES")) {
                warehouseContainer.setStyle(possibleMoveStyle);
            }
            if (moves.contains("END_TURN")) {
                endTurnButton.setDisable(false);
            }
        });
        GameUtils.debug("ended");
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
        List<LeaderCard> allLeaders = new ArrayList<>();
        allLeaders.addAll(gameState.getPlayerByUsername(gui.getUsername()).getLeaderCards());
        allLeaders.addAll(gameState.getPlayerByUsername(gui.getUsername()).getPlayedLeaderCards());
        ((LeaderCardsController)gui.getSceneController("leader-cards")).load(allLeaders, "SHOW");
        gui.setScene("leader-cards");
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
        if(gameState.getPlayers().size()!=1)displayPossibleMoves(new ArrayList<>());
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
