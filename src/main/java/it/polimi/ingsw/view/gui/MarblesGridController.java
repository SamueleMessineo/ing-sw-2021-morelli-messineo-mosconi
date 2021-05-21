package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.market.Marble;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.util.List;
import java.util.Locale;

public class MarblesGridController implements SceneController{
    private GUI gui;

    @FXML
    private GridPane grid;

    public void setMarbles(List<Marble> marbles){
        try{
            grid.getChildren().clear();
            GridPane.setValignment(grid, VPos.CENTER);
            GridPane.setHalignment(grid, HPos.CENTER);
            for(int i=0; i<marbles.size(); i++){
                Text marbleText=new Text(marbles.get(i).name());
                Image marbleImage=new Image(new FileInputStream(
                        "src/main/resources/images/marbles/" +
                                marbles.get(i).name().toLowerCase()+".png"));
                ImageView marbleImageView=new ImageView(marbleImage);
                marbleImageView.setPreserveRatio(true);
                marbleImageView.setFitWidth(0);
                marbleImageView.setFitHeight(0);
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
