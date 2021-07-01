package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Shelf;
import it.polimi.ingsw.model.shared.*;
import it.polimi.ingsw.network.game.GetResourcesCheatMessage;
import it.polimi.ingsw.network.game.SelectMoveResponseMessage;
import it.polimi.ingsw.utils.GameUtils;
import it.polimi.ingsw.utils.ResourceManager;
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

/**
 * Controller for the main scene of the application.
 */
public class GameBoardController implements SceneController {
    private GUI gui;
    private Game gameState;
    private AnchorPane cardStacksContainer;
    private AnchorPane basicProductionContainer;
    private HBox warehouseContainer;
    @FXML
    private HBox leadersContainer;
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
    private Text whosPlayingText;
    @FXML
    private Tab settingsTab;
    @FXML
    private Text roomIdText;

    @FXML
    void hide(MouseEvent event) {
        marblesContainer.setVisible(false);
    }

    /**
     * Takes the full game state and displays all the info, for all the connected players.
     * @param game the game state.
     */
    public void load(Game game) {
        Platform.runLater(() -> {
            gameState = game;
            playerInfo.setText(gui.getUsername() + ": " + gameState.getPlayerByUsername(gui.getUsername()).getVP() + " points");
            if (gui.getUsername().equals(gameState.getCurrentPlayer().getUsername())) {
                whosPlayingText.setText("It's your turn to play!");
            } else {
                whosPlayingText.setText(gameState.getCurrentPlayer().getUsername() + " is playing. Wait for your turn!");
            }
            roomIdText.setText("Room ID: " + gui.getRoomID());
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
            // display the cards grid
            ((CardsGridController) cardsLoader.getController()).setCards(gameState.getMarket().getCardsGrid());
            cardsContainer.getChildren().add(cardsGrid);
            // display the marbles
            ((MarblesGridController) marblesLoader.getController()).setMarbles(
                    gameState.getMarket().getMarbleStructure().getMarbles(),
                    gameState.getMarket().getMarbleStructure().getExtraMarble());
            marblesContainer.getChildren().add(marblesGrid);
            // populate player tabs
            tabPane.getTabs().clear();
            for (Player p : gameState.getActivePlayers()) {
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
                        leaderImageView = ResourceManager.getImageView(leaderCard);
                    } else {
                        if (p.getPlayedLeaderCards().contains(leaderCard)) {
                            leaderImageView = ResourceManager.getImageView(leaderCard);
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
                        ResourceManager.setDarkImageView(leaderImageView, 0.5);
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
                VBox extraShelvesContainer = new VBox();
                for (String shelfName : p.getPlayerBoard().getWarehouse().getShelfNames()) {
                    Shelf shelf = p.getPlayerBoard().getWarehouse().getShelf(shelfName);
                    if (Arrays.asList("bottom", "middle", "top").contains(shelfName)) {
                        HBox shelfResourcesContainer = new HBox();
                        shelfResourcesContainer.setPrefSize(129, 46);
                        shelfResourcesContainer.setAlignment(Pos.CENTER);
                        shelfResourcesContainer.setPadding(new Insets(0, 10, 0, 10));
                        shelfResourcesContainer.setSpacing(10);
                        for (int i = 0; i < shelf.getResourceNumber(); i++) {
                            ImageView resourceImageView = ResourceManager.getImageView(shelf.getResourceType());
                            resourceImageView.setFitWidth(40);
                            resourceImageView.setFitHeight(40);
                            shelfResourcesContainer.getChildren().add(resourceImageView);
                        }
                        normalShelvesContainer.getChildren().add(shelfResourcesContainer);
                    }else {
                        AnchorPane extraShelfOuterContainer = new AnchorPane();
                        Image extraShelfImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                                "images/board/extra_" + shelf.getResourceType().name().toLowerCase() + ".png")));
                        ImageView extraShelfImageView = new ImageView(extraShelfImage);
                        extraShelfImageView.setPreserveRatio(true);
                        extraShelfImageView.setFitWidth(135);
                        extraShelfOuterContainer.setPrefSize(135, 60);
                        extraShelfOuterContainer.getChildren().add(extraShelfImageView);
                        HBox extraShelfResourcesContainer = new HBox();
                        extraShelfResourcesContainer.setPrefSize(
                                extraShelfOuterContainer.getPrefWidth(), extraShelfOuterContainer.getPrefHeight());
                        extraShelfResourcesContainer.setLayoutX(0);
                        extraShelfResourcesContainer.setLayoutY(0);
                        extraShelfResourcesContainer.setPadding(new Insets(0, 0, 0, 15));
                        extraShelfResourcesContainer.setSpacing(25);
                        extraShelfResourcesContainer.setAlignment(Pos.CENTER_LEFT);
                        for (int i = 0; i < shelf.getResourceNumber(); i++) {
                            ImageView resourceImageView = ResourceManager.getImageView(shelf.getResourceType());
                            resourceImageView.setFitWidth(40);
                            resourceImageView.setFitHeight(40);
                            extraShelfResourcesContainer.getChildren().add(resourceImageView);
                        }
                        extraShelfOuterContainer.getChildren().add(extraShelfResourcesContainer);
                        extraShelvesContainer.getChildren().add(extraShelfOuterContainer);
                    }
                }

                normalShelvesContainer.setPrefSize(165, 177);
                normalShelvesContainer.setSpacing(10);
                normalShelvesContainer.setAlignment(Pos.BOTTOM_CENTER);
                warehouseContainer.getChildren().add(normalShelvesContainer);

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
                    ImageView resourceImage = ResourceManager.getImageView(entry.getKey());
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

                if (p.getUsername().equals(game.getPlayers().get(game.getInkwellPlayer()).getUsername()))
                playerTab.setText("(inkwell) " + p.getUsername() + ": " + p.getVP() + " points");

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

                // display pope's tiles
                for (int i = 0; i < 3; i++) {
                    PopesFavorTile tile = game.getLorenzoIlMagnifico().getFaithTrack().getPopesFavorTiles().get(i);
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

                playerTab.setContent(tabContainer);
                tabPane.getTabs().add(playerTab);
            }
            tabPane.getTabs().add(settingsTab);
        });
    }

    /**
     * Makes the areas of the board that if clicked allow the player
     * to make a legal move glow with a yellow color.
     * @param moves the allowed moves.
     */
    public void displayPossibleMoves(List<String> moves) {
        Platform.runLater(() -> {
            DropShadow borderGlow = ResourceManager.getGlowEffect();
            endTurnButton.setDisable(true);
            marblesContainer.setEffect(null);
            cardsContainer.setEffect(null);
            leadersContainer.setEffect(null);
            cardStacksContainer.setEffect(null);
            warehouseContainer.setEffect(null);
            basicProductionContainer.setEffect(null);

            if (gui.isPlaying()) {
                if (moves.contains("GET_MARBLES"))
                    marblesContainer.setEffect(borderGlow);
                if (moves.contains("BUY_CARD"))
                    cardsContainer.setEffect(borderGlow);
                if (moves.contains("DROP_LEADER") || moves.contains("PLAY_LEADER"))
                    leadersContainer.setEffect(borderGlow);
                if (moves.contains("ACTIVATE_PRODUCTION")) {
                    if (gameState.getPlayerByUsername(gui.getUsername()).canActivateBasicProduction())
                        basicProductionContainer.setEffect(borderGlow);
                    if (gameState.getPlayerByUsername(gui.getUsername()).canActivateProduction())
                        cardStacksContainer.setEffect(borderGlow);
                }
                if (moves.contains("SWITCH_SHELVES"))
                    warehouseContainer.setEffect(borderGlow);
                if (moves.contains("END_TURN"))
                    endTurnButton.setDisable(false);
            }
        });
    }

    /**
     * Zooms in on the development cards market.
     * @param event the javafx event.
     */
    @FXML
    void viewCardMarket(MouseEvent event) {
        ResourceManager.playClickSound();
        ((CardsMarketController)gui.getSceneController("cards-market")).load(gameState.getMarket().getCardsGrid());
        gui.setScene("cards-market");
    }

    /**
     * Zooms in on the marbles grid.
     * @param event the javafx event.
     */
    @FXML
    void viewMarbles(MouseEvent event) {
        ResourceManager.playClickSound();
        ((MarblesMarketController)gui.getSceneController("marbles-market")).load(
                gameState.getMarket().getMarbleStructure().getMarbles(),
                gameState.getMarket().getMarbleStructure().getExtraMarble());
        gui.setScene("marbles-market");
    }

    /**
     * Zooms in on the player's leader cards.
     * @param event the javafx event.
     */
    @FXML
    void viewLeaders(MouseEvent event) {
        ResourceManager.playClickSound();
        List<LeaderCard> allLeaders = new ArrayList<>();
        allLeaders.addAll(gameState.getPlayerByUsername(gui.getUsername()).getLeaderCards());
        allLeaders.addAll(gameState.getPlayerByUsername(gui.getUsername()).getPlayedLeaderCards());
        ((LeaderCardsController)gui.getSceneController("leader-cards")).load(allLeaders, "SHOW");
        gui.setScene("leader-cards");
    }

    /**
     * Zooms in on the warehouse.
     * @param event the javafx event.
     */
    @FXML
    void viewWarehouse(MouseEvent event) {
        ResourceManager.playClickSound();
        ((WarehouseController) gui.getSceneController("warehouse")).load(gameState.getPlayerByUsername(gui.getUsername()).getPlayerBoard().getWarehouse());
        gui.setScene("warehouse");
    }

    /**
     * Zooms in on the development card stacks.
     * @param event the javafx event.
     */
    @FXML
    void viewCardsAndProductions(MouseEvent event) {
        ResourceManager.playClickSound();
        ((CardsProductionController)gui.getSceneController("cards-production")).display(
                gameState.getPlayerByUsername(gui.getUsername()).getPlayerBoard().getCardStacks()
        );
        gui.setScene("cards-production");
    }

    /**
     * Ends the turn for the player.
     * @param event the javafx event.
     */
    @FXML
    void endTurn(ActionEvent event) {
        ResourceManager.playClickSound();
        gui.getClient().sendMessage(new SelectMoveResponseMessage("END_TURN"));
        if(gameState.getActivePlayers().size()!=1)displayPossibleMoves(new ArrayList<>());
    }

    /**
     * Toggles the sounds of the application.
     * @param event the javafx event.
     */
    @FXML
    void toggleSound(ActionEvent event) {
        ResourceManager.toggleSound();
    }

    /**
     * Activate cheat message.
     * @param event the javafx event.
     */
    @FXML
    void getExtraResources(ActionEvent event) {
        gui.getClient().sendMessage(new GetResourcesCheatMessage());
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
