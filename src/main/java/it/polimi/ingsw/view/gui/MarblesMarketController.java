package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.game.SelectMarblesResponseMessage;
import it.polimi.ingsw.network.game.SelectMoveResponseMessage;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * The controller for the scene showing the marbles grid, and allowing the user to make a move.
 */
public class MarblesMarketController implements SceneController{
    private GUI gui;
    @FXML
    private VBox vbox;
    @FXML
    private HBox buttonContainer;

    /**
     * Display the marbles grid.
     * @param marbles the marbles grid.
     * @param extra the extra marble.
     */
    public void load(List<Marble> marbles, Marble extra) {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource("scenes/marbles-grid.fxml"));
            VBox marblesGrid;
            vbox.getChildren().clear();
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
            vbox.getChildren().addAll(marblesGrid, buttonContainer);
            buttonContainer.getChildren().get(1).setDisable(!gui.isPlaying());
        });
    }

    /**
     * Let the player choose a row or column to shift.
     */
    public void allowSelect() {
        Platform.runLater(() -> {
            VBox marblesGridContainer = (VBox) vbox.getChildren().get(0);
            GridPane marblesGrid = (GridPane) marblesGridContainer.getChildren().get(0);
            marblesGrid.setPrefSize(360, 260);
            marblesGrid.addColumn(4);
            marblesGrid.addRow(3);
            for (int i = 0; i < 3; i++) {
                ImageView upArrowImageView = ResourceManager.getMarblesArrow("up");
                int finalI = i;
                upArrowImageView.setOnMouseClicked(mouseEvent -> selectRowOrColumn("COLUMN", finalI+1));
                marblesGrid.add(upArrowImageView, i, 3);

                ImageView leftArrowImageView = ResourceManager.getMarblesArrow("left");
                leftArrowImageView.setOnMouseClicked(mouseEvent -> selectRowOrColumn("ROW", finalI+1));
                marblesGrid.add(leftArrowImageView, 4, i);
            }
            ImageView upArrowImageView = ResourceManager.getMarblesArrow("up");
            upArrowImageView.setOnMouseClicked(mouseEvent -> selectRowOrColumn("COLUMN", 4));
            marblesGrid.add(upArrowImageView, 3, 3);
            vbox.getChildren().remove(buttonContainer);
            vbox.setAlignment(Pos.CENTER);
        });
    }

    /**
     * Select a row of column of marbles.
     * @param rowOrColumn 'ROW' if the player selected a row, 'COLUMN' otherwise.
     * @param index the index of the selected row or column.
     */
    private void selectRowOrColumn(String rowOrColumn, int index) {
        ResourceManager.playClickSound();
        gui.getClient().sendMessage(new SelectMarblesResponseMessage(rowOrColumn, index-1));
    }

    /**
     * Goes back to the main scene of the game board.
     * @param event the javafx event.
     */
    @FXML
    void cancel(ActionEvent event) {
        ResourceManager.playClickSound();
        gui.setScene("game-board");
    }

    /**
     * Requests to obtain marbles from the grid.
     * @param event the javafx event.
     */
    @FXML
    void getMarbles(ActionEvent event) {
        ResourceManager.playClickSound();
        gui.getClient().sendMessage(new SelectMoveResponseMessage("GET_MARBLES"));
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}