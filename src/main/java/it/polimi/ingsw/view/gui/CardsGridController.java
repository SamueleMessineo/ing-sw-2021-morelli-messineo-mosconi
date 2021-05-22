package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.market.MarketCardStack;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.util.List;

public class CardsGridController implements SceneController {
    private GUI gui;
    @FXML
    private GridPane grid;

    public void setCards(List<MarketCardStack> cardStacks) {
        try {
            grid.getChildren().clear();
            GridPane.setValignment(grid, VPos.CENTER);
            GridPane.setHalignment(grid, HPos.CENTER);
            for (MarketCardStack stack : cardStacks) { ;
                DevelopmentCard topCard = stack.peek();
                Text cardText = new Text(topCard.getCardType().name() + " " + topCard.getScore());
                Image cardImage = new Image(new FileInputStream(
                        "src/main/resources/images/development/development_" +
                                topCard.getCardType().name().toLowerCase() + "_"+ topCard.getScore() +".png"));
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
