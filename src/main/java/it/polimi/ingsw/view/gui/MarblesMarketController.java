package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.game.SelectMarblesResponseMessage;
import it.polimi.ingsw.network.game.SelectMoveResponseMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MarblesMarketController implements SceneController{
    private GUI gui;
    @FXML
    private VBox vbox;
    @FXML
    private HBox buttonContainer;

    public void load(List<Marble> marbles, Marble extra) {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource("scenes/marbles-grid.fxml"));
            VBox marblesGrid;
            if (vbox.getChildren().size() == 2)
                vbox.getChildren().remove(0);
            try {
                marblesGrid = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            ((MarblesGridController) loader.getController()).setMarbles(marbles, extra);
            marblesGrid.setScaleX(1.8);
            marblesGrid.setScaleY(1.8);
            vbox.setAlignment(Pos.BOTTOM_CENTER);
            vbox.setSpacing(140);
            vbox.setPadding(new Insets(0, 0, 30, 0));
            vbox.getChildren().add(0, marblesGrid);
        });
    }

    public void allowSelect() {
        Platform.runLater(() -> {
            VBox marblesGridContainer = (VBox) vbox.getChildren().get(0);
            GridPane marblesGrid = (GridPane) marblesGridContainer.getChildren().get(0);
            marblesGrid.setPrefSize(360, 260);
            marblesGrid.addColumn(4);
            marblesGrid.addRow(3);
            Image leftArrowImage = new Image(Objects.requireNonNull(getClass().getClassLoader()
                            .getResourceAsStream("images/marbles/marbles_arrow_left.png")));
            Image upArrowImage = new Image(Objects.requireNonNull(getClass().getClassLoader()
                            .getResourceAsStream("images/marbles/marbles_arrow_up.png")));
            for (int i = 0; i < 3; i++) {
                ImageView upArrowImageView = viewFromImage(upArrowImage);
                int finalI = i;
                upArrowImageView.setOnMouseClicked(mouseEvent -> selectRowOrColumn("COLUMN", finalI+1));
                marblesGrid.add(upArrowImageView, i, 3);

                ImageView leftArrowImageView = viewFromImage(leftArrowImage);
                leftArrowImageView.setOnMouseClicked(mouseEvent -> selectRowOrColumn("ROW", finalI+1));
                marblesGrid.add(leftArrowImageView, 4, i);
            }
            ImageView upArrowImageView = viewFromImage(upArrowImage);
            upArrowImageView.setOnMouseClicked(mouseEvent -> selectRowOrColumn("COLUMN", 4));
            marblesGrid.add(upArrowImageView, 3, 3);
            vbox.getChildren().remove(buttonContainer);
            vbox.setAlignment(Pos.CENTER);
        });
    }

    private ImageView viewFromImage(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
        imageView.setCursor(Cursor.HAND);
        GridPane.setValignment(imageView, VPos.CENTER);
        GridPane.setHalignment(imageView, HPos.CENTER);
        return imageView;
    }

    private void selectRowOrColumn(String rowOrColumn, int index) {
        System.out.println("selected " + rowOrColumn + " number " + index);
        gui.getClient().sendMessage(new SelectMarblesResponseMessage(rowOrColumn, index-1));
        vbox.getChildren().remove(0);
        vbox.getChildren().add(buttonContainer);
    }

    @FXML
    void cancel(ActionEvent event) {
        gui.setScene("game-board");
    }

    @FXML
    void getMarbles(ActionEvent event) {
        gui.getClient().sendMessage(new SelectMoveResponseMessage("GET_MARBLES"));
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}