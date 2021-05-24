package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.LocalClient;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.MarbleStructure;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.view.UI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

public class GUI extends Application implements UI {
    private Stage stage;
    private final Map<String, Scene> sceneMap = new HashMap<>();
    private final Map<String, SceneController> controllerMap = new HashMap<>();
    private Client client;
    private Game gameState;
    private String username;
    private MediaPlayer mediaPlayer;

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void start(Stage stage) throws Exception {
        loadScenes();
        this.stage = stage;
        this.stage.setTitle("Masters of Renaissance");
        this.stage.setResizable(false);
//        setMusic();
        setScene("online-offline");

        Image logo = new Image(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream("images/punchboard/calamaio.png")));
        this.stage.getIcons().add(logo);

    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }

    public void initializeClient(boolean online) {
        if (online) {
            this.client = new Client(this);
        } else {
            this.client = new LocalClient(this);
        }
    }

    public Client getClient() {
        return client;
    }

    @Override
    public void run() {
        launch();
    }

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

    private void loadScenes() {
        for (String sceneName : Arrays.asList(
                "online-offline", "connect", "setup-game", "room-details", "initial-resources",
                "initial-leaders", "game-board", "cards-market", "marbles-market", "drop-resources",
                "leader-cards")) {
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

    private void setMusic() {
        Media backgroundMusic = new Media(Objects.requireNonNull(getClass().getClassLoader().getResource(
                "music/bg.mp3"
        )).toExternalForm());
        mediaPlayer = new MediaPlayer(backgroundMusic);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.25);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        });
    }

    @Override
    public void displayRoomDetails(ArrayList<String> players, int playersNum, int RoomId) {
        ((RoomDetailsController)controllerMap.get("room-details")).displayDetails(players, playersNum, RoomId);
        setScene("room-details");
    }

    @Override
    public void displayError(String body) {
        Platform.runLater(() -> {
            System.out.println(body);
            Stage dialog = new Stage();
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(50);
            Text text = new Text("An error occurred!");
            text.setFont(Font.font("System", FontWeight.BLACK, 18));
            vBox.getChildren().add(text);
            vBox.getChildren().add(new Text(body));
            Scene scene = new Scene(vBox, 400, 300);
            dialog.setScene(scene);
            dialog.initOwner(stage);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        });
    }

    @Override
    public void displayString(String body) {

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
        ((GameBoardController)controllerMap.get("game-board")).displayPossibleMoves(moves);
        setScene("game-board");
    }

    @Override
    public void setGameState(Game game) {
        gameState = game;
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

    }

    @Override
    public void discardLeaderCard(ArrayList<LeaderCard> cards) {
        System.out.println("discardLeader");
        ((LeaderCardsController) controllerMap.get("leader-cards")).load(cards,"DROP");
        setScene("leader-cards");
    }

    @Override
    public void switchShelves(ArrayList<String> shelves) {

    }

    @Override
    public void activateProduction(List<ProductionPower> productionPowers) {

    }

    @Override
    public void buyDevelopmentCard(List<DevelopmentCard> developmentCards) {
        ((CardsMarketController)controllerMap.get("cards-market")).allowBuy(developmentCards);

    }

    @Override
    public void selectStackToPlaceCard(List<Integer> stacks) {

    }

    @Override
    public void playLeader(List<LeaderCard> leaderCards) {
        System.out.println("playLeader");
        ((LeaderCardsController) controllerMap.get("leader-cards")).load(leaderCards,"PLAY");
        setScene("leader-cards");
    }

    @Override
    public void gameOver(String winner, Map<String, Integer> standing){

    }

    @Override
    public void askUsername() {

    }

    @Override
    public String getUsername() {
        return username;
    }

    public Game getGame(){
        return gameState;
    }
}
