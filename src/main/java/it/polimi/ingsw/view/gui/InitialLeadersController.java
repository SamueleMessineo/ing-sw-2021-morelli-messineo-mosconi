package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.network.game.DropInitialLeaderCardsResponseMessage;
import it.polimi.ingsw.utils.GameUtils;
import it.polimi.ingsw.utils.ResourceManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Objects;

/**
 * The controller for the scene that lets the player choose
 * their initial leader cards.
 */
public class InitialLeadersController implements SceneController{
    private GUI gui;
    @FXML
    private VBox cardGrid;
    @FXML
    private HBox topLine;
    @FXML
    private HBox bottomLine;
    @FXML
    private ImageView upLeftImg;
    @FXML
    private ImageView upRightImg;
    @FXML
    private ImageView bottomLeftImg;
    @FXML
    private ImageView bottomRightImg;
    @FXML
    private ImageView upLeftCross;
    @FXML
    private ImageView upRightCross;
    @FXML
    private ImageView bottomLeftCross;
    @FXML
    private ImageView bottomRightCross;
    @FXML
    private Button confirmButton;

    private List<LeaderCard> cards;

    private int card1 = -1;
    private int card2 = -1;

    /**
     * Confirms the selected cards.
     * @param event the javafx event.
     */
    @FXML
    void confirm(ActionEvent event) {
        ResourceManager.playClickSound();
        if(card1!=card2 && card1!=-1 && card1<=3 && card2<=3){
            System.out.println("sending " +card1 + " " + card2 );
            gui.getClient().sendMessage(new DropInitialLeaderCardsResponseMessage(card1, card2));

            confirmButton.setDisable(true);
            confirmButton.setText("Waiting for other players to select their cards");
            confirmButton.setPrefWidth(380);

        }
    }

    /**
     * Displays the four cards to choose from.
     * @param cards the four random leader cards.
     */
    public void displayGrid(List<LeaderCard> cards){
        Platform.runLater(()->{
            GameUtils.debug(cards.toString());
            this.cards = cards;
            for (LeaderCard card : cards) {
                ImageView imageView = ResourceManager.getImageView(card);
                imageView.setScaleX(0.5);
                imageView.setScaleY(0.5);
                switch (cards.indexOf(card)) {
                    case 0:
                        upLeftImg.setImage(imageView.getImage());
                        break;
                    case 1:
                        upRightImg.setImage(imageView.getImage());
                        break;
                    case 2:
                        bottomLeftImg.setImage(imageView.getImage());
                        break;
                    case 3:
                        bottomRightImg.setImage(imageView.getImage());
                        break;
                }
            }
        });
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    /**
     * Selects the top left card.
     * @param mouseEvent the javafx event.
     */
    public void selectUpLeft(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            ResourceManager.playClickSound();
            if ((card1 == -1 && card2 != 0) || (card2 == -1 && card1 != 0)) {
                if (card1 == -1) card1 = 0;
                else card2 = 0;
                upLeftImg.toBack();
                upLeftCross.setImage(new Image(Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream("images/red-cross.png"))));
                upLeftCross.toFront();
            } else {
                if (card1 == 0) card1 = -1;
                if (card2 == 0) card2 = -1;
                upLeftCross.setImage(null);
                upLeftCross.toBack();
                upLeftImg.toFront();
            }
        });
    }

    /**
     * Selects the top right card.
     * @param mouseEvent the javafx event.
     */
    public void selectUpRight(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            ResourceManager.playClickSound();
            if ((card1 == -1 && card2 != 1) || (card2 == -1 && card1 != 1)) {
                if (card1 == -1) card1 = 1;
                else card2 = 1;
                upRightImg.toBack();
                upRightCross.setImage(new Image(Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream("images/red-cross.png"))));
                upRightCross.toFront();
            } else {
                if (card1 == 1) card1 = -1;
                if (card2 == 1) card2 = -1;
                upRightCross.setImage(null);
                upRightImg.toFront();
            }
        });
    }

    /**
     * Selects the bottom left card.
     * @param mouseEvent the javafx event.
     */
    public void selectBottomLeft(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            ResourceManager.playClickSound();
            if ((card1 == -1 && card2 != 2) || (card2 == -1 && card1 != 2)) {
                if (card1 == -1) card1 = 2;
                else card2 = 2;
                bottomLeftImg.toBack();
                bottomLeftCross.setImage(new Image(Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream("images/red-cross.png"))));
                bottomLeftCross.toFront();
            } else {
                if (card1 == 2) card1 = -1;
                if (card2 == 2) card2 = -1;
                bottomLeftCross.setImage(null);
                bottomLeftImg.toFront();
            }
        });
    }

    /**
     * Selects the bottom right card.
     * @param mouseEvent the javafx event.
     */
    public void selectBottomRight(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            ResourceManager.playClickSound();
            if((card1==-1 && card2!=3)||(card2==-1 && card1 != 3)){
                if(card1==-1)card1 = 3;
                else card2 = 3;
                bottomRightImg.toBack();
                bottomRightCross.setImage(new Image(Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream("images/red-cross.png"))));
                bottomRightCross.toFront();
            }else {
                if(card1==3)card1=-1;
                if(card2==3)card2=-1;
                bottomRightCross.setImage(null);
                bottomRightImg.toFront();
            }
        });
    }


}