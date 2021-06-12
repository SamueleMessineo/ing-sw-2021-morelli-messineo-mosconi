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

public class LeaderCardsController implements SceneController{
    private GUI gui;
    @FXML
    private VBox vbox;
    @FXML
    private HBox buttonContainer;

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

    @FXML
    void cancel(ActionEvent event) {
        ResourceManager.playClickSound();
        gui.setScene("game-board");
    }

    @FXML
    void dropLeader(ActionEvent event){
        ResourceManager.playClickSound();
        System.out.println("dropLeader");
        gui.getClient().sendMessage(new SelectMoveResponseMessage("DROP_LEADER"));
    }

    @FXML
    void playLeader(ActionEvent event){
        ResourceManager.playClickSound();
        System.out.println("playLeader");
        gui.getClient().sendMessage(new SelectMoveResponseMessage("PLAY_LEADER"));
    }

    private void sendLeaderToDrop(int index){
        ResourceManager.playClickSound();
        System.out.println("sendLeaderToDrop");
        gui.getClient().sendMessage(new DropLeaderCardResponseMessage(index));
    }

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
