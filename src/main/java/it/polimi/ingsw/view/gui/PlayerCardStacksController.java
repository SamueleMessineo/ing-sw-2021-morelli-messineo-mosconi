package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.player.PlayerCardStack;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Objects;

public class PlayerCardStacksController implements SceneController {
    private GUI gui;
    @FXML
    private HBox container;

    public void load(List<PlayerCardStack> stacks) {
        Platform.runLater(() -> {
            container.getChildren().clear();
            for (PlayerCardStack playerCardStack : stacks) {
                AnchorPane singleCardStackContainer = new AnchorPane();
                singleCardStackContainer.setPrefSize(150, 210);
                for (int i = 0; i < playerCardStack.size(); i++) {
                    DevelopmentCard card = playerCardStack.get(i);
                    Image cardImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                            "images/development/development_" + card.getCardType().name().toLowerCase() +
                                    "_" + card.getScore() + ".png"
                    )));
                    ImageView cardImageView = new ImageView(cardImage);
                    cardImageView.setPreserveRatio(true);
                    cardImageView.setFitHeight(160);
                    cardImageView.setFitWidth(200);
                    cardImageView.setLayoutX(
                            75-(cardImageView.getLayoutBounds().getWidth()/2)
                    );
                    cardImageView.setLayoutY(200-160-(25*i));
                    singleCardStackContainer.getChildren().add(cardImageView);
                }
                container.getChildren().add(singleCardStackContainer);
            }
        });
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
