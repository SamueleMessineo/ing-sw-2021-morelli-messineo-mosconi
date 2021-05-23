package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.market.MarketCardStack;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class CardsMarketController implements SceneController {
    private GUI gui;
    @FXML
    private VBox vbox;

    public void load(List<MarketCardStack> cardStacks) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("scenes/cards-grid.fxml"));
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
        vbox.getChildren().add(0, cardsGrid);
    }

    @FXML
    void cancel(ActionEvent event) {
        vbox.getChildren().remove(0);
        gui.setScene("game-board");
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
