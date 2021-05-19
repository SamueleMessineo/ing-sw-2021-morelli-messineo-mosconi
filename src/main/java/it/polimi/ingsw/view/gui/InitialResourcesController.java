package it.polimi.ingsw.view.gui;

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
import java.util.*;

public class InitialResourcesController implements SceneController {
    private GUI gui;
    @FXML
    private Text amountText;
    @FXML
    private HBox resourcesList;
    private final Map<Resource, Integer> selectedResources = GameUtils.emptyResourceMap();
    private final Map<Resource, Text> selectedAmounts = new HashMap<>();

    void updateCount(Resource resource, boolean increment) {
        System.out.println("update count " + resource + " increment? " + increment);
        GameUtils.incrementValueInResourceMap(selectedResources, resource, increment ? 1 : -1);
        selectedAmounts.get(resource).setText(selectedResources.get(resource).toString());
    }

    @FXML
    void confirm(ActionEvent event) {

    }

    public void displayList(List<Resource> resources, int amount) {
        Platform.runLater(() -> {
            amountText.setText("Select " + amount + " resources and confirm");
            resourcesList.getChildren().clear();
            for (Resource resource : resources) {
                VBox resourceContainer = new VBox();
                resourceContainer.setAlignment(Pos.CENTER);
                resourceContainer.setSpacing(5);
                System.out.println("create resource vbox");
                System.out.println(resource.name().toLowerCase());
                Image image  = null;
                try {
                    image = new Image(new FileInputStream("src/main/resources/images/punchboard/" + resource.name().toLowerCase() + ".png"));
                    System.out.println("loaded image");
                    ImageView imageView = new ImageView(image);
                    imageView.setScaleX(0.5);
                    imageView.setScaleY(0.5);
                    resourceContainer.getChildren().add(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                HBox buttonContainer = new HBox();
                buttonContainer.setAlignment(Pos.CENTER);
                buttonContainer.setSpacing(10);
                Button minusButton = new Button("-");
                minusButton.setOnAction(actionEvent -> updateCount(resource, false));
//                minusButton.setDisable(true);
                Button plusButton = new Button("+");
                plusButton.setOnAction(actionEvent -> updateCount(resource, true));
                Text selectedAmountText = new Text(selectedResources.get(resource).toString());
                selectedAmounts.put(resource, selectedAmountText);
                buttonContainer.getChildren().addAll(Arrays.asList(minusButton, selectedAmountText, plusButton));
                resourceContainer.getChildren().add(buttonContainer);
                resourcesList.getChildren().add(resourceContainer);
            }
        });
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
