package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.game.SelectInitialResourceResponseMessage;
import it.polimi.ingsw.utils.GameUtils;
import it.polimi.ingsw.utils.ResourceManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.*;

public class InitialResourcesController implements SceneController {
    private GUI gui;
    @FXML
    private Text amountText;
    @FXML
    private HBox resourcesList;
    @FXML
    private Button confirmButton;
    private int toSelect;
    private final Map<Resource, Integer> selectedResources = GameUtils.emptyResourceMap();
    private final Map<Resource, Text> selectedAmounts = new HashMap<>();
    private int totalResourcesSelected = 0;
    private final Map<Resource, Map<String, Button>> resourceButtonsMap = new HashMap<>();

    void updateCount(Resource resource, boolean increment) {
        Platform.runLater(() -> {
            GameUtils.incrementValueInResourceMap(selectedResources, resource, increment ? 1 : -1);
            totalResourcesSelected += increment ? 1 : -1;
            if (increment){
                resourceButtonsMap.get(resource).get("minus").setDisable(false);
                if (totalResourcesSelected == toSelect) {
                    confirmButton.setDisable(false);
                    for (Resource r : resourceButtonsMap.keySet()) {
                        resourceButtonsMap.get(r).get("plus").setDisable(true);
                    }
                }
            } else {
                confirmButton.setDisable(true);
                for (Resource r : resourceButtonsMap.keySet()) {
                    resourceButtonsMap.get(r).get("plus").setDisable(false);
                }
                if (selectedResources.get(resource) < 1) {
                    resourceButtonsMap.get(resource).get("minus").setDisable(true);
                }
            }
            selectedAmounts.get(resource).setText(selectedResources.get(resource).toString());
        });
    }

    @FXML
    void confirm(ActionEvent event) {
        ResourceManager.playClickSound();
        if (totalResourcesSelected == toSelect) {
            List<Resource> selected = new ArrayList<>();
            for (Map.Entry<Resource, Integer> entry : selectedResources.entrySet()) {
                if (entry.getValue() > 0) {
                    for (int i = 0; i < entry.getValue(); i++) {
                        selected.add(entry.getKey());
                    }
                }
            }
            gui.getClient().sendMessage(new SelectInitialResourceResponseMessage(selected));
        }
    }

    public void displayList(List<Resource> resources, int amount) {
        Platform.runLater(() -> {
            toSelect = amount;
            amountText.setText("Select " + amount + " resources and confirm");
            resourcesList.getChildren().clear();
            for (Resource resource : resources) {
                resourceButtonsMap.put(resource, new HashMap<>());
                VBox resourceContainer = new VBox();
                resourceContainer.setAlignment(Pos.CENTER);
                resourceContainer.setSpacing(5);
                System.out.println("create resource vbox");
                ImageView imageView = GameUtils.getImageView(resource);
                imageView.setScaleX(0.5);
                imageView.setScaleY(0.5);
                resourceContainer.getChildren().add(imageView);
                HBox buttonContainer = new HBox();
                buttonContainer.setAlignment(Pos.CENTER);
                buttonContainer.setSpacing(10);
                Button minusButton = new Button("-");
                minusButton.setOnAction(actionEvent -> updateCount(resource, false));
                minusButton.setDisable(true);
                resourceButtonsMap.get(resource).put("minus", minusButton);
                Button plusButton = new Button("+");
                plusButton.setOnAction(actionEvent -> updateCount(resource, true));
                resourceButtonsMap.get(resource).put("plus", plusButton);
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
