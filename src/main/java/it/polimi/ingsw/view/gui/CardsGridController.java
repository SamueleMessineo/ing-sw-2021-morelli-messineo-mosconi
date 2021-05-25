package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.market.MarketCardStack;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.utils.GameUtils;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;

public class CardsGridController implements SceneController {
    private GUI gui;
    @FXML
    private GridPane grid;

    public void setCards(List<MarketCardStack> cardStacks) {
        try {
            grid.getChildren().clear();
            for (MarketCardStack stack : cardStacks) {
                if (!stack.isEmpty()) {
                    DevelopmentCard topCard = stack.peek();
                    ImageView cardImageView = GameUtils.getImageView(topCard);
                    cardImageView.setFitWidth(200);
                    cardImageView.setFitHeight(95);
                    GridPane.setValignment(cardImageView, VPos.CENTER);
                    GridPane.setHalignment(cardImageView, HPos.CENTER);
                    int cardX = (cardStacks.indexOf(stack))/4;
                    int cardY = (cardStacks.indexOf(stack) % 4);
                    grid.add(cardImageView, cardY, cardX);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
