package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.utils.GameUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.util.List;

public class InitialLeadersController implements SceneController{
    private GUI gui;

    @FXML
    private VBox cardGrid;
    @FXML
    private HBox topLine;
    @FXML
    private HBox bottomLine;
    @FXML
    void confirm(ActionEvent event) {

    }
    @FXML
    private ImageView upLeftImg;
    @FXML
    private ImageView upRightImg;
    @FXML
    private ImageView bottomLeftImg;
    @FXML
    private ImageView bottomRightImg;


    public void ciao(){
        System.out.println("ciao");
    }

    public void displayGrid(List<LeaderCard> cards){
        Platform.runLater(()->{
            //topLine.getChildren().clear();
            //bottomLine.getChildren().clear();
            //cardGrid.getChildren().add(topLine);
            //cardGrid.getChildren().add(bottomLine);
            for (LeaderCard card:
                 cards) {
                Image image  = null;
                try {
                    System.out.println(cards.indexOf(card));
                    System.out.println("src/main/resources/images/leaders/leader_"+card.getEffectScope().toLowerCase()+"_"+card.getEffectObject().name().toLowerCase()+".png");
                    try {
                        image = new Image(new FileInputStream("src/main/resources/images/leaders/leader_"+card.getEffectScope().toLowerCase()+"_"+card.getEffectObject().name().toLowerCase()+".png"));
                    } catch (Exception e){
                        e.printStackTrace();
                    }


                    System.out.println("loaded image");
                    ImageView imageView = new ImageView(image);
                    imageView.setScaleX(0.5);
                    imageView.setScaleY(0.5);
                    /*
                    if(cards.indexOf(card)<2) topLine.getChildren().add(imageView);
                    else bottomLine.getChildren().add(imageView);

                     */
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
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
