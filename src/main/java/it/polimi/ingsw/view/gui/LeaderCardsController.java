package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.network.game.DropLeaderCardResponseMessage;
import it.polimi.ingsw.network.game.PlayLeaderResponseMessage;
import it.polimi.ingsw.network.game.SelectMoveResponseMessage;
import it.polimi.ingsw.utils.GameUtils;
import it.polimi.ingsw.utils.ResourceManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * The controller for the scene displaying the player's leader cards
 * and allowing the choices of playing or discarding one.
 */
public class LeaderCardsController implements SceneController{
    private GUI gui;
    @FXML
    private VBox vbox;
    @FXML
    private HBox buttonContainer;

    /**
     * Display the player's leader cards.
     * @param leaderCards the player's leader cards.
     * @param type a string with the value of 'SHOW' if a row of buttons should be displayed letting the
     *             player select whether to play or drop a card, 'DROP' if clicking on a card should drop it,
     *             'PLAY' if clicking on a card should play it.
     */
    public void load(List<LeaderCard> leaderCards, String type) {
        GameUtils.debug(leaderCards.toString());
        Platform.runLater(() -> {
            System.out.println(type);
            vbox.getChildren().clear();
            if(type.equals("SHOW"))
                vbox.getChildren().add(buttonContainer);
            HBox leadersContainer = new HBox();
            leadersContainer.setSpacing(50);
            leadersContainer.setPrefWidth(287);
            leadersContainer.setPrefHeight(188);
            leadersContainer.setAlignment(Pos.CENTER);
            for (LeaderCard leaderCard : leaderCards) {
                ImageView leaderImageView = ResourceManager.getImageView(leaderCard);
                leaderImageView.setFitWidth(300);
                leaderImageView.setFitHeight(240);
                if (type.equals("DROP")) {
                    leaderImageView.setCursor(Cursor.HAND);
                    leaderImageView.setOnMouseClicked(MouseEvent -> sendLeaderToDrop(leaderCards.indexOf(leaderCard)));
                }
                if (type.equals("PLAY")) {
                    leaderImageView.setCursor(Cursor.HAND);
                    leaderImageView.setOnMouseClicked(MouseEvent -> sendLeaderToPlay(leaderCards.indexOf(leaderCard)));
                }
                leadersContainer.getChildren().add(leaderImageView);
            }
            vbox.getChildren().add(0, leadersContainer);
        });
    }

    /**
     * Go back to the main scene of the game board.
     * @param event the javafx event.
     */
    @FXML
    void cancel(ActionEvent event) {
        ResourceManager.playClickSound();
        gui.setScene("game-board");
    }

    /**
     * Allow the user to choose a card to drop.
     * @param event the javafx event.
     */
    @FXML
    void dropLeader(ActionEvent event){
        ResourceManager.playClickSound();
        System.out.println("dropLeader");
        gui.getClient().sendMessage(new SelectMoveResponseMessage("DROP_LEADER"));
    }

    /**
     * Allow the user to choose a card to play.
     * @param event the javafx event.
     */
    @FXML
    void playLeader(ActionEvent event){
        ResourceManager.playClickSound();
        System.out.println("playLeader");
        gui.getClient().sendMessage(new SelectMoveResponseMessage("PLAY_LEADER"));
    }

    /**
     * Drop the selected card.
     * @param index the index of the leader card to drop.
     */
    private void sendLeaderToDrop(int index){
        ResourceManager.playClickSound();
        System.out.println("sendLeaderToDrop");
        gui.getClient().sendMessage(new DropLeaderCardResponseMessage(index));
    }

    /**
     * Play the selected leader card.
     * @param index the index of the leader card to drop.
     */
    private void sendLeaderToPlay(int index){
        ResourceManager.playClickSound();
        System.out.println("sendLeaderToDrop");
        gui.getClient().sendMessage(new PlayLeaderResponseMessage(index));
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
