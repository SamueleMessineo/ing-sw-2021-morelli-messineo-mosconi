package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.market.MarketCardStack;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.List;

public class CardsGridController implements SceneController {
    private GUI gui;
    @FXML
    private GridPane grid;

    public void setCards(List<MarketCardStack> cardStacks) {
        try {
            grid.getChildren().clear();
            for (MarketCardStack stack : cardStacks) { ;
                DevelopmentCard topCard = stack.peek();
                Text cardText = new Text(topCard.getCardType().name() + " " + topCard.getScore());
                int cardX = (cardStacks.indexOf(stack))/4;
                int cardY = (cardStacks.indexOf(stack) % 4);
                grid.add(cardText, cardX, cardY);
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
