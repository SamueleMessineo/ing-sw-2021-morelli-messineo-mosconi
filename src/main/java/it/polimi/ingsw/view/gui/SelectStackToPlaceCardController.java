package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.player.PlayerCardStack;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.network.game.SelectStackToPlaceCardResponseMessage;
import it.polimi.ingsw.utils.ResourceManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * The controller for the scene that lets the user choose
 * which stack to place a newly bought development card on.
 */
public class SelectStackToPlaceCardController implements SceneController{
    private GUI gui;
    @FXML
    public HBox stackList;

    /**
     * Displays the possible stacks.
     * @param stackIndexes the indexes of the stacks the card could be placed on.
     */
    public void showStacks(List<Integer> stackIndexes){
        Platform.runLater(() -> {
            stackList.getChildren().clear();
            for (Integer index : stackIndexes) {
                System.out.println(stackIndexes.indexOf(index) + 1 + ":");
                PlayerCardStack playerCardStack = gui.getGame().getCurrentPlayer().getPlayerBoard().getCardStacks().get(index);
                ImageView cardImageView = null;
                if (!playerCardStack.isEmpty()) {
                    DevelopmentCard topCard = playerCardStack.peek();
                    cardImageView = ResourceManager.getImageView(topCard);
                    cardImageView.setPreserveRatio(true);
                    cardImageView.setCursor(Cursor.HAND);
                    cardImageView.setFitWidth(400);
                    cardImageView.setFitHeight(195);
                    cardImageView.setOnMouseClicked(MouseEvent -> sendSelectedStack(index));
                }

                stackList.getChildren().add(cardImageView);
            }
        });
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    /**
     * Confirms the selection of a stack.
     * @param index the index of the selected stack.
     */
    public void sendSelectedStack(int index){
        ResourceManager.playClickSound();
        gui.getClient().sendMessage(new SelectStackToPlaceCardResponseMessage(index));
    }
}
