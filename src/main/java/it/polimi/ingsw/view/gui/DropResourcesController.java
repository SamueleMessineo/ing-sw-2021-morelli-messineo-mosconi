package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.game.DropResourcesResponseMessage;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The controller for the scene asking the user which resources to discard
 * after making a move on the marbles grid.
 */
public class DropResourcesController implements SceneController{
    private GUI gui;
    private Map<Resource, Integer> originalResources;
    private Map<Resource, Integer> resourcesToKeep;
    private final Map<Resource, Text> resourceAmountTexts = new HashMap<>();
    private final Map<Resource, Map<String, Button>> resourceButtonsMap = new HashMap<>();
    @FXML
    private HBox resourcesList;

    /**
     * Display the obtained resources.
     * @param resources the obtained resources.
     */
    public void displayResources(Map<Resource, Integer> resources) {
        Platform.runLater(() -> {
            resourcesToKeep = new HashMap<>(resources);
            originalResources = new HashMap<>(resources);
            resourcesList.getChildren().clear();
            for (Resource resource : resources.keySet()) {
                resourceButtonsMap.put(resource, new HashMap<>());
                VBox resourceContainer = new VBox();
                resourceContainer.setAlignment(Pos.BOTTOM_CENTER);
                resourceContainer.setSpacing(5);
                ImageView imageView = ResourceManager.getImageView(resource);
                imageView.setScaleX(0.5);
                imageView.setScaleY(0.5);
                resourceContainer.getChildren().add(imageView);
                HBox buttonContainer = new HBox();
                buttonContainer.setAlignment(Pos.CENTER);
                buttonContainer.setSpacing(10);
                Button minusButton = new Button("-");
                minusButton.setOnAction(actionEvent -> updateCount(resource, false));
                resourceButtonsMap.get(resource).put("minus", minusButton);
                Button plusButton = new Button("+");
                plusButton.setDisable(true);
                plusButton.setOnAction(actionEvent -> updateCount(resource, true));
                resourceButtonsMap.get(resource).put("plus", plusButton);
                Text selectedAmountText = new Text(Integer.toString(resourcesToKeep.get(resource)));
                resourceAmountTexts.put(resource, selectedAmountText);
                buttonContainer.getChildren().addAll(Arrays.asList(minusButton, selectedAmountText, plusButton));
                resourceContainer.getChildren().add(buttonContainer);
                resourcesList.getChildren().add(resourceContainer);
            }
        });
    }

    /**
     * Increment or decrement the given resource's amount.
     * @param resource the resource whose amount to update.
     * @param increment true if the count should be incremented by 1, false otherwise.
     */
    void updateCount(Resource resource, boolean increment) {
        Platform.runLater(() -> {
            GameUtils.incrementValueInResourceMap(resourcesToKeep, resource, increment ? 1 : -1);
            resourceButtonsMap.get(resource).get(increment ? "minus" : "plus").setDisable(false);
            if (!increment && resourcesToKeep.get(resource) < 1) {
                resourceButtonsMap.get(resource).get("minus").setDisable(true);
            }
            if (increment && resourcesToKeep.get(resource) >= originalResources.get(resource)) {
                resourceButtonsMap.get(resource).get("plus").setDisable(true);
            }
            resourceAmountTexts.get(resource).setText(resourcesToKeep.get(resource).toString());
        });
    }

    /**
     * Confirm the choice of resources to be kept.
     * @param event the javafx event.
     */
    @FXML
    void confirmDropSelection(ActionEvent event) {
        ResourceManager.playClickSound();
        Map<Resource, Integer> resourcesToDrop = new HashMap<>();
        for (Resource resource : originalResources.keySet()) {
            resourcesToDrop.put(resource, originalResources.get(resource) - resourcesToKeep.get(resource));
            if (resourcesToDrop.get(resource) < 0) {
                return;
            }
        }
        gui.getClient().sendMessage(new DropResourcesResponseMessage(resourcesToDrop));
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
