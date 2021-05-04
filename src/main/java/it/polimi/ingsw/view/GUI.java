package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.MarbleStructure;
import it.polimi.ingsw.model.player.Shelf;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.Resource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GUI extends Application implements UI {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/connect.fxml"));
        stage.setTitle("Masters of Renaissance");
        //Label label = new Label("Welcome to the game!");
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

    @Override
    public void displayRoomDetails(ArrayList<String> players, int playersNum, int RoomId) {

    }

    @Override
    public void setup() {
        launch();
    }

    @Override
    public void displayError(String body) {

    }

    @Override
    public void displayString(String body) {

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
    public void displayMarbles(MarbleStructure marbleStructure) {

    }

    @Override
    public void dropResources(List<Resource> resources) {

    }

    @Override
    public void discardLeaderCard(ArrayList<LeaderCard> cards) {

    }

    @Override
    public void switchShelves(ArrayList<Shelf> shelves) {

    }
}
