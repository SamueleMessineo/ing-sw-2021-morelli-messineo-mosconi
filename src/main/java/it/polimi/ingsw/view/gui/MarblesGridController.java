package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.market.Marble;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Objects;

/**
 * The controller for the grid of marbles.
 */
public class MarblesGridController implements SceneController{
    private GUI gui;
    @FXML
    private GridPane grid;
    @FXML
    private HBox extraContainer;

    /**
     * Fills and displays the marbles grid.
     * @param marbles the marbles in the grid.
     * @param extra
     */
    public void setMarbles(List<Marble> marbles, Marble extra){
        Platform.runLater(() -> {
            grid.getChildren().clear();
            Image extraImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                    "images/marbles/" + extra.name().toLowerCase() + ".png")));
            int marbleSize = 60;
            ImageView extraImageView = new ImageView(extraImage);
            extraImageView.setFitHeight(marbleSize);
            extraImageView.setFitWidth(marbleSize);
            extraContainer.getChildren().add(extraImageView);
            for (int i = 0; i < marbles.size(); i++) {
                Image marbleImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                        "images/marbles/" + marbles.get(i).name().toLowerCase() + ".png")));
                ImageView marbleImageView = new ImageView(marbleImage);
                marbleImageView.setPreserveRatio(true);
                marbleImageView.setFitWidth(marbleSize);
                marbleImageView.setFitHeight(marbleSize);
                GridPane.setValignment(marbleImageView, VPos.CENTER);
                GridPane.setHalignment(marbleImageView, HPos.CENTER);
                int marbleX = (i / 4);
                int marbleY = (i % 4);
                grid.add(marbleImageView, marbleY, marbleX);
            }
        });
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}