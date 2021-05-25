package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.player.PlayerCardStack;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.network.game.SelectStackToPlaceCardResponseMessage;
import it.polimi.ingsw.utils.GameUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.List;

public class SelectStackToPlaceCardController implements SceneController{
    private GUI gui;
    @FXML
    public HBox stackList;

    public void showStacks(List<Integer> stackIndexes){
        Platform.runLater(() -> {
            stackList.getChildren().clear();
            for (Integer index : stackIndexes) {
                System.out.println(stackIndexes.indexOf(index) + 1 + ":");
                PlayerCardStack playerCardStack = gui.getGame().getCurrentPlayer().getPlayerBoard().getCardStacks().get(index);
                ImageView cardImageView = null;
                if (!playerCardStack.isEmpty()) {
                    DevelopmentCard topCard = playerCardStack.peek();
                    cardImageView = GameUtils.getImageView(topCard);
                }
                cardImageView.setFitWidth(200);
                cardImageView.setFitHeight(95);
                cardImageView.setOnMouseClicked(MouseEvent -> sendSelectedStack(stackIndexes.indexOf(index)));
                stackList.getChildren().add(cardImageView);
            }
        });
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void sendSelectedStack(int index){
        gui.getClient().sendMessage(new SelectStackToPlaceCardResponseMessage(index));
    }
}
