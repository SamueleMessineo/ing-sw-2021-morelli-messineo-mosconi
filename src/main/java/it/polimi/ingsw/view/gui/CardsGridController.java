package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.market.MarketCardStack;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Objects;

public class CardsGridController implements SceneController {
    private GUI gui;
    @FXML
    private GridPane grid;

    public void setCards(List<MarketCardStack> cardStacks) {
        try {
            grid.getChildren().clear();
            for (MarketCardStack stack : cardStacks) { ;
                DevelopmentCard topCard = stack.peek();
                String cardName = "development_" + topCard.getCardType().name().toLowerCase() + "_"+ topCard.getScore();
                Image cardImage = new Image(Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream("images/development/" + cardName + ".png")));
                ImageView cardImageView = new ImageView(cardImage);
                cardImageView.setPreserveRatio(true);
                cardImageView.setFitWidth(200);
                cardImageView.setFitHeight(95);
                GridPane.setValignment(cardImageView, VPos.CENTER);
                GridPane.setHalignment(cardImageView, HPos.CENTER);
                int cardX = (cardStacks.indexOf(stack))/4;
                int cardY = (cardStacks.indexOf(stack) % 4);
                grid.add(cardImageView, cardY, cardX);
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
