package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.market.MarketCardStack;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.utils.GameUtils;
import it.polimi.ingsw.utils.ResourceManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;

/**
 * Controller for the grid of development cards.
 */
public class CardsGridController implements SceneController {
    private GUI gui;
    @FXML
    private GridPane grid;

    /**
     * Fills the grid with the development cards.
     * @param cardStacks all the card stacks.
     */
    public void setCards(List<MarketCardStack> cardStacks) {
        Platform.runLater(() -> {
            grid.getChildren().clear();
            for (MarketCardStack stack : cardStacks) {
                if (!stack.isEmpty()) {
                    DevelopmentCard topCard = stack.peek();
                    ImageView cardImageView = ResourceManager.getImageView(topCard);
                    cardImageView.setFitWidth(200);
                    cardImageView.setFitHeight(95);
                    GridPane.setValignment(cardImageView, VPos.CENTER);
                    GridPane.setHalignment(cardImageView, HPos.CENTER);
                    int cardX = (cardStacks.indexOf(stack)) / 4;
                    int cardY = (cardStacks.indexOf(stack) % 4);
                    grid.add(cardImageView, cardY, cardX);
                }
            }
        });
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
