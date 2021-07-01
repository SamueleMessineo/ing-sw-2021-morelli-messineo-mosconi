package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.game.SelectResourceForWhiteMarbleResponseMessage;
import it.polimi.ingsw.utils.GameUtils;
import it.polimi.ingsw.utils.ResourceManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The controller for the scene asking the user how to convert the white marbles
 * obtained from a move on the grid when he has two active leader cards on the marbles.
 */
public class ConvertMarblesController implements SceneController {
    private GUI gui;
    private List<Resource> selectedConversions;
    private List<Map<Resource, ImageView>> imageViewMaps;
    @FXML
    private HBox buttonsContainer;
    @FXML
    private VBox container;

    /**
     * Display the choices and ask the user to choose.
     * @param amount the number of marbles to convert.
     * @param options the choices of conversion.
     */
    public void load(int amount, List<Resource> options) {
        Platform.runLater(() -> {
            container.getChildren().clear();
            Text infoText = new Text("You have obtained " + amount + " white marbles");
            infoText.setFont(Font.font("System", FontWeight.BLACK, 18));
            infoText.setFill(Color.WHITE);
            infoText.setEffect(new DropShadow());
            Text subtitle = new Text("choose how to convert them into resources");
            subtitle.setFill(Color.WHITE);
            subtitle.setEffect(new DropShadow());
            container.getChildren().addAll(infoText, subtitle);
            selectedConversions = new ArrayList<>();
            imageViewMaps = new ArrayList<>();
            for (int i = 0; i < amount; i++) {
                selectedConversions.add(null);
                Map<Resource, ImageView> singleConversionMap = new HashMap<>();
                HBox row = new HBox();
                row.setAlignment(Pos.CENTER);
                row.setSpacing(20);
                Text resourceNumberText = new Text("Marble " + (i+1));
                resourceNumberText.setFill(Color.WHITE);
                resourceNumberText.setEffect(new DropShadow());
                row.getChildren().add(resourceNumberText);
                for (Resource resourceOption : options) {
                    ImageView resourceImage = ResourceManager.getImageView(resourceOption);
                    resourceImage.setFitHeight(50);
                    resourceImage.setFitWidth(50);
                    ColorAdjust bw = new ColorAdjust();
                    bw.setSaturation(0);
                    resourceImage.setEffect(bw);
                    singleConversionMap.put(resourceOption, resourceImage);
                    int finalI = i;
                    resourceImage.setOnMouseClicked(event -> selectResource(finalI, resourceOption));
                    row.getChildren().add(resourceImage);
                }
                imageViewMaps.add(singleConversionMap);
                container.getChildren().add(row);
            }
            buttonsContainer.getChildren().get(0).setDisable(true);
            container.getChildren().add(buttonsContainer);
        });
    }

    /**
     * Convert the white marble to the selected resource.
     * @param number the index of white marble to be converted.
     * @param option the choice of conversion.
     */
    void selectResource(int number, Resource option) {
        Platform.runLater(() -> {
            ResourceManager.playClickSound();
            Resource lastSelected = selectedConversions.get(number);
            if (lastSelected != null) {
                ColorAdjust bw = new ColorAdjust();
                bw.setSaturation(0);
                imageViewMaps.get(number).get(lastSelected).setEffect(bw);
            }
            selectedConversions.remove(number);
            selectedConversions.add(number, option);
            ColorAdjust color = new ColorAdjust();
            color.setSaturation(1);
            imageViewMaps.get(number).get(option).setEffect(color);

            for (Resource selectedResource : selectedConversions)
                if (selectedResource == null) return;
            buttonsContainer.getChildren().get(0).setDisable(false);
        });
    }

    /**
     * Confirm the conversion of the white marbles.
     * @param event the javafx event.
     */
    @FXML
    void convert(ActionEvent event) {
        ResourceManager.playClickSound();
        Map<Resource, Integer> conversion = new HashMap<>();
        for (Resource resource : selectedConversions) {
            GameUtils.incrementValueInResourceMap(conversion, resource, 1);
        }
        gui.getClient().sendMessage(new SelectResourceForWhiteMarbleResponseMessage(conversion));
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
