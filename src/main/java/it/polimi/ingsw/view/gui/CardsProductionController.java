package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.player.PlayerCardStack;
import it.polimi.ingsw.network.game.SelectMoveResponseMessage;
import it.polimi.ingsw.utils.ResourceManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the scene showing the player's card stacks.
 */
public class CardsProductionController implements SceneController {
    private GUI gui;
    @FXML
    private HBox buttonsContainer;
    @FXML
    private VBox vbox;

    /**
     * Display the card stacks.
     * @param cardStacks the player's card stacks.
     */
    public void display(List<PlayerCardStack> cardStacks) {
        Platform.runLater(() -> {
            vbox.getChildren().clear();
            HBox cardStacksContainer = new HBox();
            FXMLLoader playerCardStacksLoader = new FXMLLoader(
                    getClass().getClassLoader().getResource("scenes/player-card-stacks.fxml"));
            try {
                cardStacksContainer = playerCardStacksLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cardStacksContainer.setScaleX(1.8);
            cardStacksContainer.setScaleY(1.8);
            vbox.setSpacing(50);
            ((PlayerCardStacksController)playerCardStacksLoader.getController()).load(cardStacks);
            vbox.getChildren().addAll(cardStacksContainer, buttonsContainer);
            buttonsContainer.getChildren().get(1).setDisable(!gui.isPlaying());
        });
    };

    /**
     * Request the activation of some production powers.
     * @param event the javafx event.
     */
    @FXML
    void sendActivateRequest(ActionEvent event) {
        ResourceManager.playClickSound();
        gui.getClient().sendMessage(new SelectMoveResponseMessage("ACTIVATE_PRODUCTION"));
    }

    /**
     * Go back to the main game board scene.
     * @param event the javafx event.
     */
    @FXML
    void cancel(ActionEvent event) {
        ResourceManager.playClickSound();
        gui.setScene("game-board");
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
