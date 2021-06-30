package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.market.MarketCardStack;
import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.network.game.BuyDevelopmentCardResponseMessage;
import it.polimi.ingsw.network.game.SelectMoveResponseMessage;
import it.polimi.ingsw.utils.GameUtils;
import it.polimi.ingsw.utils.ResourceManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Controller for the scene showing the development cards market.
 */
public class CardsMarketController implements SceneController {
    private GUI gui;
    @FXML
    private VBox vbox;
    @FXML
    private HBox buttonsContainer;

    /**
     * Loads and displays the cards grid.
     * @param cardStacks the
     */
    public void load(List<MarketCardStack> cardStacks) {
        Platform.runLater(() -> {
            vbox.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("scenes/cards-grid.fxml"));
            GridPane cardsGrid;
            try {
                cardsGrid = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            ((CardsGridController) loader.getController()).setCards(cardStacks);
            cardsGrid.setScaleX(1.9);
            cardsGrid.setScaleY(1.9);
            vbox.setAlignment(Pos.BOTTOM_CENTER);
            vbox.setSpacing(160);
            vbox.setPadding(new Insets(0, 0, 30, 0));
            vbox.getChildren().addAll(cardsGrid, buttonsContainer);
        });
    }

    /**
     * Goes back to the main game board without buying any card.
     * @param event the javafx event.
     */
    @FXML
    void cancel(ActionEvent event) {
        Platform.runLater(() -> {
            ResourceManager.playClickSound();
            vbox.getChildren().remove(0);
            gui.setScene("game-board");
        });
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    /**
     * Allows the user to choose one development card to buy.
     * @param developmentCards the development cards that can be bought.
     */
    public void allowBuy(List<DevelopmentCard> developmentCards) {
        Platform.runLater(() -> {
            vbox.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource("scenes/cards-grid.fxml"));
            GridPane cardsGrid;
            try {
                cardsGrid = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            cardsGrid.setScaleX(1.9);
            cardsGrid.setScaleY(1.9);
            vbox.setAlignment(Pos.CENTER);
            vbox.setSpacing(160);
            vbox.setPadding(new Insets(0, 0, 30, 0));
            vbox.getChildren().add(cardsGrid);
            for (DevelopmentCard card : developmentCards) {
                ImageView cardImageView = ResourceManager.getImageView(card);
                cardImageView.setFitWidth(200);
                cardImageView.setFitHeight(95);
                cardImageView.setCursor(Cursor.HAND);
                cardImageView.setOnMouseClicked(MouseEvent -> sendCardToBuy(developmentCards.indexOf(card)));
                GridPane.setValignment(cardImageView, VPos.CENTER);
                GridPane.setHalignment(cardImageView, HPos.CENTER);
                int cardX = 3 - card.getLevel();
                int cardY = Arrays.asList(CardType.GREEN, CardType.BLUE, CardType.YELLOW, CardType.PURPLE)
                        .indexOf(card.getCardType());
                cardsGrid.add(cardImageView, cardY, cardX);
            }
        });
    }

    /**
     * Buys the selected development card.
     * @param cardIndex the index of the development card to buy.
     */
    public void sendCardToBuy(int cardIndex) {
        ResourceManager.playClickSound();
        gui.getClient().sendMessage(new BuyDevelopmentCardResponseMessage(cardIndex));
    }

    /**
     * Confirms that the user wants to buy a card.
     * @param actionEvent the javafx event.
     */
    public void buyCard(ActionEvent actionEvent) {
        ResourceManager.playClickSound();
        gui.getClient().sendMessage(new SelectMoveResponseMessage("BUY_CARD"));
    }
}
