package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.market.Marble;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.util.List;
import java.util.Locale;

public class MarblesGridController implements SceneController{
    private GUI gui;

    @FXML
    private GridPane grid;
    @FXML
    private HBox extraContainer;

    public void setMarbles(List<Marble> marbles, Marble extra){
        try{
            grid.getChildren().clear();
            Image extraImage = new Image(new FileInputStream(
                    "src/main/resources/images/marbles/" + extra.name().toLowerCase() + ".png"));
            ImageView extraImageView = new ImageView(extraImage);
            extraImageView.setFitHeight(40);
            extraImageView.setFitWidth(40);
            extraContainer.getChildren().add(extraImageView);
            for(int i=0; i<marbles.size(); i++){
                Image marbleImage=new Image(new FileInputStream(
                        "src/main/resources/images/marbles/" +
                                marbles.get(i).name().toLowerCase()+".png"));
                ImageView marbleImageView=new ImageView(marbleImage);
                marbleImageView.setPreserveRatio(true);
                marbleImageView.setFitWidth(40);
                marbleImageView.setFitHeight(40);
                GridPane.setValignment(marbleImageView, VPos.CENTER);
                GridPane.setHalignment(marbleImageView, HPos.CENTER);
                int marbleX = (i/4);
                int marbleY = (i%4);
                grid.add(marbleImageView, marbleY, marbleX);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
