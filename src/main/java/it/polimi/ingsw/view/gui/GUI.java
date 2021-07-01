package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.LocalClient;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.MarbleStructure;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.utils.GameUtils;
import it.polimi.ingsw.utils.ResourceManager;
import it.polimi.ingsw.view.UI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 * The main class for the GUI, extends a JavaFX Application
 */
public class GUI extends Application implements UI {
    private Stage stage;
    /**
     * A map storing all the scenes, this is used in the setScene method to quickly change scene
     */
    private Map<String, Scene> sceneMap;
    /**
     * The scene controllers, these have the same key as their corresponding scene in the 'sceneMap'
     */
    private Map<String, SceneController> controllerMap;
    private Client client;
    private Game gameState;
    private String username;
    private Stage popupStage;
    private boolean playing;

    public void setUsername(String username) {
        this.username = username;
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * The method called when the application starts: attributes are reset, scenes are preloaded,
     * the stage is set up, sounds are loaded and the game icon is set.
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        username = null;
        gameState = null;
        client = null;
        sceneMap = new HashMap<>();
        controllerMap = new HashMap<>();
        loadScenes();
        this.stage = stage;
        this.stage.setTitle("Masters of Renaissance");
        this.stage.setResizable(false);
        popupStage = new Stage();
        popupStage.initOwner(stage);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        ResourceManager.initializeSound();
        ResourceManager.playBackgroundMusic();
        setScene("online-offline");

        Image logo = new Image(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream("images/punchboard/calamaio.png")));
        this.stage.getIcons().add(logo);

    }

    /**
     * The method called when the application stops.
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        System.exit(0);
    }

    /**
     * Initialized a Client or LocalClient depending on the user's choice to play online or offline.
     * @param online a boolean indicating whether the client is playing online or offline
     */
    public void initializeClient(boolean online) {
        if (online) {
            this.client = new Client(this);
        } else {
            this.client = new LocalClient(this);
            this.client.run();
        }
    }

    public Client getClient() {
        return client;
    }

    @Override
    public void run() {
        launch();
    }

    /**
     * Changes the scene to the given one
     * @param sceneName the name of the scene to be shown
     */
    public void setScene(String sceneName) {
        System.out.println("display " + sceneName);
        try {
            Platform.runLater(() -> {
                if (stage.getScene() == null || !stage.getScene().equals(sceneMap.get(sceneName))) {
                    System.out.println("scene set");
                } else {
                    System.out.println("scene already set");
                }
                stage.setScene(sceneMap.get(sceneName));
                stage.show();
                System.out.println("stage shown");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SceneController getSceneController(String sceneName) {
        return controllerMap.get(sceneName);
    }

    @Override
    public void setup() {
        System.out.println("setup");
        try {
            launch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Preloads from memory all the scenes and their respective controllers
     * and stores them in maps for easy access
     */
    private void loadScenes() {
        for (String sceneName : Arrays.asList(
                "online-offline", "connect", "setup-game", "room-details", "initial-resources",
                "initial-leaders", "game-board", "cards-market","select-stack", "marbles-market", "drop-resources",
                "offline-info", "leader-cards", "cards-production", "activate-production","game-over", "convert-marbles",
                "warehouse")) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource("scenes/" + sceneName +".fxml"));
            try {
                sceneMap.put(sceneName, new Scene(loader.load()));
                SceneController controller = loader.getController();
                controllerMap.put(sceneName, controller);
                controller.setGUI(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void displayRoomDetails(ArrayList<String> players, int playersNum, int RoomId) {
        ((RoomDetailsController)controllerMap.get("room-details")).displayDetails(players, playersNum, RoomId);
        setScene("room-details");
    }

    /**
     * Display a popup window with the given parent as body
     * @param parent the body of the popup
     */
    public void displayPopup(Parent parent) {
        Platform.runLater(() -> {
            Scene scene = new Scene(parent);
            popupStage.setAlwaysOnTop(true);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        });
    }

    /**
     * Returns the stage for the popup used for error and string messages.
     * @return the popup stage.
     */
    public Stage getPopupStage() {
        return popupStage;
    }

    /**
     * Display an error popup with the given string as body
     * @param body the error message
     */
    @Override
    public void displayError(String body) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(30);
        Text text = new Text("An error occurred!");
        text.setFont(Font.font("System", FontWeight.BLACK, 18));
        vBox.getChildren().add(text);
        vBox.getChildren().add(new Text(body));
        displayPopup(vBox);
    }

    @Override
    public void displayString(String body) {
//        VBox vBox = new VBox();
//        vBox.setAlignment(Pos.CENTER);
//        vBox.setSpacing(50);
//        Text text = new Text("");
//        text.setFont(Font.font("System", FontWeight.BLACK, 18));
//        vBox.getChildren().add(text);
//        vBox.getChildren().add(new Text(body));
//        displayPopup(vBox);
    }

    @Override
    public void selectInitialResources(List<Resource> resources, int amount) {
        ((InitialResourcesController)controllerMap.get("initial-resources")).displayList(resources, amount);
        setScene("initial-resources");
    }

    @Override
    public void selectLeaderCards(ArrayList<LeaderCard> leaderCards) {
        System.out.println("received");
        try {
            ((InitialLeadersController)controllerMap.get("initial-leaders")).displayGrid(leaderCards);
        }catch (Exception e){
            e.printStackTrace();
        }
        setScene("initial-leaders");
    }

    @Override
    public void displayGameState() {
        ((GameBoardController)controllerMap.get("game-board")).load(gameState);
        setScene("game-board");
    }

    @Override
    public void displayPossibleMoves(List<String> moves) {
        GameUtils.debug("ready to display");
        ((GameBoardController)controllerMap.get("game-board")).displayPossibleMoves(moves);
        setScene("game-board");
    }

    @Override
    public void setGameState(Game game) {
        gameState = game;
        playing = game.getCurrentPlayer().getUsername().equals(username);
    }

    @Override
    public void selectMarbles(MarbleStructure marbleStructure) {
        ((MarblesMarketController) controllerMap.get("marbles-market")).allowSelect();
    }

    @Override
    public void dropResources(Map<Resource, Integer> resources) {
        ((DropResourcesController) controllerMap.get("drop-resources")).displayResources(resources);
        setScene("drop-resources");
    }

    @Override
    public void selectResourceForWhiteMarbles(int amount, List<Resource> options) {
        ((ConvertMarblesController) controllerMap.get("convert-marbles")).load(amount, options);
        setScene("convert-marbles");
    }

    @Override
    public void discardLeaderCard(ArrayList<LeaderCard> cards) {
        ((LeaderCardsController) controllerMap.get("leader-cards")).load(cards,"DROP");
        setScene("leader-cards");
    }

    @Override
    public void switchShelves(ArrayList<String> shelves) {
        ((WarehouseController) controllerMap.get("warehouse")).allowSwitch(shelves);
        setScene("warehouse");
    }

    @Override
    public void activateProduction(List<ProductionPower> productionPowers) {
        ((ActivateProductionController)controllerMap.get("activate-production")).load(productionPowers);
        setScene("activate-production");
    }

    @Override
    public void buyDevelopmentCard(List<DevelopmentCard> developmentCards) {
        ((CardsMarketController)controllerMap.get("cards-market")).allowBuy(developmentCards);

    }

    @Override
    public void selectStackToPlaceCard(List<Integer> stacks) {
        ((SelectStackToPlaceCardController) controllerMap.get("select-stack")).showStacks(stacks);
        setScene("select-stack");
    }

    @Override
    public void playLeader(List<LeaderCard> leaderCards) {
        System.out.println("playLeader");
        ((LeaderCardsController) controllerMap.get("leader-cards")).load(leaderCards,"PLAY");
        setScene("leader-cards");
    }

    @Override
    public void gameOver(String winner, Map<String, Integer> standing){
        ((GameOverController) controllerMap.get("game-over")).showWinnerAndStanding(winner, standing);
        setScene("game-over");
    }

    @Override
    public void askUsername() {
        if(username==null){
            setScene("offline-info");
        }

    }

    @Override
    public String getUsername() {
        return username;
    }

    public boolean isPlaying() {
        return playing;
    }

    public Game getGame(){
        return gameState;
    }
}
