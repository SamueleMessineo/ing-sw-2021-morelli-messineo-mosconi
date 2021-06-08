package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.game.ActivateProductionResponseMessage;
import it.polimi.ingsw.utils.GameUtils;
import it.polimi.ingsw.utils.ResourceManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.*;

public class ActivateProductionController implements SceneController {
    private GUI gui;
    private Player player;
    private ProductionPower basicProduction = null;
    private List<Integer> selectedCardPowers;
    private Button confirmBasicProductionButton;
    private List<AnchorPane> selectedCardPowersContainers;
    private List<ProductionPower> cardPowers;
    private AnchorPane basicProductionContainer;
    private List<Resource> extraOutputs;
    private List<Integer> selectedExtras;
    private int numberOfExtraProductions;
    @FXML
    private VBox vbox;
    @FXML
    private HBox productionsContainer;
    @FXML
    private HBox buttonsContainer;

    public void load(List<ProductionPower> powers) {
        Platform.runLater(() -> {
            player = gui.getGame().getPlayerByUsername(gui.getUsername());
            vbox.getChildren().clear();
            productionsContainer.getChildren().clear();
            // check default production
            if (player.canActivateBasicProduction()) {
                basicProductionContainer = new AnchorPane();
                Image basicProductionImage = new Image(Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream("images/board/basic_production_scroll.png")));
                ImageView basicProductionImageView = new ImageView(basicProductionImage);
                basicProductionImageView.setPreserveRatio(true);
                basicProductionImageView.setFitWidth(150);
                basicProductionContainer.getChildren().add(basicProductionImageView);
                basicProductionContainer.setOnMouseClicked(this::handleBasicProduction);
                basicProductionContainer.setCursor(Cursor.HAND);
                productionsContainer.getChildren().add(basicProductionContainer);
            }
            // display cards' powers
            cardPowers = powers;
            selectedCardPowers = new ArrayList<>();
            selectedCardPowersContainers = new ArrayList<>();
            extraOutputs = new ArrayList<>();
            selectedExtras = new ArrayList<>();
            for (ProductionPower cardPower : powers) {
                if (cardPower.getOutput().containsKey(Resource.ANY)) numberOfExtraProductions++;
                AnchorPane powerPane = GameUtils.buildProductionPowerBook(cardPower);
                powerPane.setOnMouseClicked(event -> selectCardPower(powers.indexOf(cardPower)));
                selectedCardPowersContainers.add(powerPane);
                productionsContainer.getChildren().add(powerPane);
            }

            vbox.getChildren().addAll(productionsContainer, buttonsContainer);
        });
    }

    void handleBasicProduction(MouseEvent event) {
        Platform.runLater(() -> {
            ResourceManager.playClickSound();
            VBox popupContainer = new VBox();
            popupContainer.setPadding(new Insets(20));
            popupContainer.setSpacing(10);
            popupContainer.setAlignment(Pos.CENTER_LEFT);
            basicProduction = new ProductionPower(GameUtils.emptyResourceMap(), GameUtils.emptyResourceMap());
            Map<Resource, Integer> playerResources = player.getPlayerBoard().getResources();

            for (Resource resource : new HashMap<>(playerResources).keySet()) {
                if (playerResources.get(resource) == 0) playerResources.remove(resource);
            }
            for (int i = 0; i < 2; i++) {
                HBox resourcePickerContainer = new HBox();
                resourcePickerContainer.setSpacing(5);
                for (Map.Entry<Resource, Integer> entry : playerResources.entrySet()) {
                    ImageView resourceImageView = GameUtils.getImageView(entry.getKey());
                    resourceImageView.setFitWidth(40);
                    resourceImageView.setFitHeight(40);
                    resourceImageView.setCursor(Cursor.HAND);
                    resourceImageView.setOnMouseClicked(event1 -> resourceClick(entry.getKey(), basicProduction.getInput(), resourcePickerContainer, resourceImageView));
                    resourcePickerContainer.getChildren().add(resourceImageView);
                }
                popupContainer.getChildren().addAll(new Text("Select the " + (i == 0 ? "first" : "second") + " input resource"), resourcePickerContainer);
            }
            HBox outputPickerContainer = new HBox();
            for (Resource resource : Arrays.asList(Resource.COIN, Resource.SERVANT, Resource.SHIELD, Resource.STONE)) {
                ImageView resourceImageView = GameUtils.getImageView(resource);
                resourceImageView.setFitWidth(40);
                resourceImageView.setFitHeight(40);
                resourceImageView.setCursor(Cursor.HAND);
                resourceImageView.setOnMouseClicked(event1 -> resourceClick(resource, basicProduction.getOutput(), outputPickerContainer, resourceImageView));
                outputPickerContainer.getChildren().add(resourceImageView);
            }
            popupContainer.getChildren().addAll(new Text("Select the output resource"), outputPickerContainer);
            confirmBasicProductionButton = new Button("Confirm");
            confirmBasicProductionButton.setOnAction(this::confirmBasic);
            confirmBasicProductionButton.setDisable(true);
            popupContainer.getChildren().add(confirmBasicProductionButton);
            gui.displayPopup(popupContainer);
        });
    }

    void resourceClick(Resource resource, Map<Resource, Integer> map, HBox pickerContainer, ImageView imageViewClicked) {
        Platform.runLater(() -> {
            GameUtils.incrementValueInResourceMap(map, resource, 1);
            for (Node imageView : pickerContainer.getChildren()) {
                if(!imageView.equals(imageViewClicked)) {
                    GameUtils.setDarkImageView((ImageView) imageView, 1);
                }
                imageView.setDisable(true);
            }
            int count = 0;
            for (int amount : basicProduction.getInput().values()) count += amount;
            if (count != 2) return;
            count = 0;
            for (int amount : basicProduction.getOutput().values()) count += amount;
            if (count != 1) return;
            confirmBasicProductionButton.setDisable(false);
        });
    }

    @FXML
    void confirmBasic(ActionEvent event) {
        Platform.runLater(() -> {
            if (player.getPlayerBoard().canPayResources(basicProduction.getInput())) {
                player.getPlayerBoard().payResourceCost(basicProduction.getInput());
                basicProductionContainer.setStyle("-fx-border-color: transparent transparent red transparent;" +
                        "-fx-border-width: 2; -fx-border-style: solid inside;");
                basicProductionContainer.setDisable(true);
                basicProductionContainer.setCursor(Cursor.DEFAULT);
            } else {
                gui.displayError("You don't own the selected resources.");
            }
            gui.getPopupStage().close();
        });
    }

    @FXML
    void selectCardPower(int index) {
        Platform.runLater(() -> {
            ResourceManager.playClickSound();
            if (!selectedCardPowers.contains(index)
                    && player.getPlayerBoard().canPayResources(cardPowers.get(index).getInput())) {

                if (!cardPowers.get(index).getOutput().containsKey(Resource.ANY))
                    confirmSingleProduction(index);
                else {
                    VBox popupContainer = new VBox();
                    popupContainer.setPadding(new Insets(20));
                    popupContainer.setSpacing(10);
                    popupContainer.setAlignment(Pos.CENTER_LEFT);
                    HBox resourcePickerContainer = new HBox();
                    resourcePickerContainer.setSpacing(5);
                    for (Resource resource : Arrays.asList(Resource.COIN, Resource.STONE, Resource.SHIELD, Resource.SERVANT)) {
                        ImageView resourceImageView = GameUtils.getImageView(resource);
                        resourceImageView.setFitWidth(40);
                        resourceImageView.setFitHeight(40);
                        resourceImageView.setCursor(Cursor.HAND);
                        resourceImageView.setOnMouseClicked(event1 -> selectExtraOutput(index, resource));
                        resourcePickerContainer.getChildren().add(resourceImageView);
                    }
                    popupContainer.getChildren().addAll(new Text("Select the output resource"), resourcePickerContainer);
                    gui.displayPopup(popupContainer);
                }
            } else {
                gui.displayError("You can't activate this production power.");
            }
        });
    }

    private void selectExtraOutput(int index, Resource resource) {
        Platform.runLater(() -> {
            if (numberOfExtraProductions == 2) selectedExtras.add(index - (cardPowers.size()-1));
            else selectedExtras.add(0);
            extraOutputs.add(resource);
            gui.getPopupStage().close();
            confirmSingleProduction(index);
        });
    }

    private void confirmSingleProduction(int index) {
        Platform.runLater(() -> {
            if (index < cardPowers.size() - numberOfExtraProductions)
                selectedCardPowers.add(index);
            player.getPlayerBoard().payResourceCost(cardPowers.get(index).getInput());
            selectedCardPowersContainers.get(index)
                    .setStyle("-fx-border-color: transparent transparent red transparent;" +
                            "-fx-border-width: 2; -fx-border-style: solid inside;");
            selectedCardPowersContainers.get(index).setDisable(true);
        });

    }

    @FXML
    void confirm(ActionEvent event) {
        ResourceManager.playClickSound();
        gui.getClient().sendMessage(new ActivateProductionResponseMessage(
                selectedCardPowers, basicProduction, selectedExtras, extraOutputs));
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
