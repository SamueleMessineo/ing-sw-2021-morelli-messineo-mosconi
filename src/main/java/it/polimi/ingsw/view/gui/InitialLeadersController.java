package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.game.DropInitialLeaderCardsResponseMessage;
import it.polimi.ingsw.network.game.SelectInitialResourceResponseMessage;
import it.polimi.ingsw.utils.GameUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Objects;

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

    private List<LeaderCard> cards;

    private int card1 = -1;
    private int card2 = -1;

    @FXML
    void confirm(ActionEvent event) {
        if(card1!=card2 && card1!=-1 && card1<=3 && card2<=3){
            System.out.println("sending " +card1 + " " + card2 );
            gui.getClient().sendMessage(new DropInitialLeaderCardsResponseMessage(card1, card2));
        }
    }

    public void displayGrid(List<LeaderCard> cards){
        Platform.runLater(()->{
            GameUtils.debug(cards.toString());
            this.cards = cards;
            for (LeaderCard card : cards) {
                Image image  = null;
//                    System.out.println(cards.indexOf(card));
//                    System.out.println("src/main/resources/images/leaders/leader_"+card.getEffectScope().toLowerCase()+"_"+card.getEffectObject().name().toLowerCase()+".png");
                image = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                    "images/leaders/leader_"+card.getEffectScope().toLowerCase() +
                           "_" + card.getEffectObject().name().toLowerCase() + ".png"
                )));
                ImageView imageView = new ImageView(image);
                imageView.setScaleX(0.5);
                imageView.setScaleY(0.5);
                switch (cards.indexOf(card)){
                    case 0:
                        upLeftImg.setImage(image);
                        break;
                    case 1:
                        upRightImg.setImage(image);
                        break;
                    case 2:
                        bottomLeftImg.setImage(image);
                        break;
                    case 3:
                        bottomRightImg.setImage(image);
                }
            }
        });
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void selectUpLeft(MouseEvent mouseEvent) {
        if(card1==-1||card2==-1){
            if(card1==-1)card1 = 0;
            else card2 = 0;
            upLeftImg.toBack();
            upLeftCross.setImage(new Image(Objects.requireNonNull(getClass().getClassLoader()
                    .getResourceAsStream("images/red-cross.png"))));
            upLeftCross.toFront();
        } else {
            if(card1==0)card1=-1;
            if(card2==0)card2=-1;
            upLeftCross.setImage(null);
            upLeftImg.toFront();
        }
    }

    public void selectUpRight(MouseEvent mouseEvent) {
        if(card1==-1||card2==-1){
            if(card1==-1)card1 = 1;
            else card2 = 1;
            upRightImg.toBack();
            upRightCross.setImage(new Image(Objects.requireNonNull(getClass().getClassLoader()
                    .getResourceAsStream("images/red-cross.png"))));
            upRightCross.toFront();
        } else {
            if(card1==1)card1=-1;
            if(card2==1)card2=-1;
            upRightCross.setImage(null);
            upRightImg.toFront();
        }
    }

    public void selectBottomLeft(MouseEvent mouseEvent) {
        if(card1==-1||card2==-1){
            if(card1==-1)card1 = 2;
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
    }

    public void selectBottomRight(MouseEvent mouseEvent) {
        if(card1==-1||card2==-1){
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
    }
}