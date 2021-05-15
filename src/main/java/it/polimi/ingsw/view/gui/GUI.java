package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.MarbleStructure;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.view.UI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class GUI extends Application implements UI {
    private Stage stage;
    private final Map<String, Scene> sceneMap = new HashMap<>();

    @Override
    public void start(Stage stage) throws Exception {
        loadScenes();
        this.stage = stage;
        this.stage.setTitle("Masters of Renaissance");
        setScene("online-offline");
    }

    @Override
    public void run() {
        launch();
    }

    public void setScene(String sceneName) {
        stage.setScene(sceneMap.get(sceneName));
        stage.show();
    }

    @Override
    public void displayRoomDetails(ArrayList<String> players, int playersNum, int RoomId) {

    }

    @Override
    public void setup() {
        launch();
    }

    private void loadScenes() {
        for (String sceneName : Arrays.asList("online-offline", "connect")) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource("scenes/" + sceneName +".fxml"));
            try {
                sceneMap.put(sceneName, loader.load());
                SceneController controller = loader.getController();
                controller.setGUI(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void displayError(String body) {

    }

    @Override
    public void displayString(String body) {

    }

    @Override
    public void selectInitialResources(List<Resource> resources, int amount) {

    }

    @Override
    public void selectLeaderCards(ArrayList<LeaderCard> leaderCards) {

    }

    @Override
    public void displayGameState() {

    }

    @Override
    public void displayPossibleMoves(List<String> moves) {

    }

    @Override
    public void setGameState(Game game) {

    }

    @Override
    public void selectMarbles(MarbleStructure marbleStructure) {

    }

    @Override
    public void dropResources(Map<Resource, Integer> resources) {

    }

    @Override
    public void discardLeaderCard(ArrayList<LeaderCard> cards) {

    }

    @Override
    public void switchShelves(ArrayList<String> shelves) {

    }

    @Override
    public void activateProduction(List<ProductionPower> productionPowers) {

    }

    @Override
    public void buyDevelopmentCard(List<DevelopmentCard> developmentCards) {

    }

    @Override
    public void selectStackToPlaceCard(List<Integer> stacks) {

    }

    @Override
    public void playLeader(List<LeaderCard> leaderCards) {

    }

    @Override
    public void gameOver(String winner, Map<String, Integer> standing){

    }

    @Override
    public void askUsername() {

    }

    @Override
    public String getUsername() {
        return null;
    }
}
