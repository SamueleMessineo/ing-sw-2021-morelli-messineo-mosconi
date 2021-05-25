package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.shared.Resource;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;

public class ConvertMarblesController implements SceneController {
    private GUI gui;
    @FXML
    private HBox buttonsContainer;
    @FXML
    private VBox container;

    public void load(int amount, List<Resource> options) {
        Platform.runLater(() -> {
            container.getChildren().clear();
            Text infoText = new Text("You have obtained " + amount + " white marbles");
            infoText.setFont(Font.font("System", FontWeight.BLACK, 18));
            infoText.setFill(Color.WHITE);
            Text subtitle = new Text("choose how to convert them into resources");
            subtitle.setFill(Color.WHITE);

            container.getChildren().addAll(infoText, subtitle, buttonsContainer);
        });
    }

    @FXML
    void convert(ActionEvent event) {

    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
