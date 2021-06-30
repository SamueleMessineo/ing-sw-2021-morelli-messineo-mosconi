package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.player.Shelf;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.network.game.SelectMoveResponseMessage;
import it.polimi.ingsw.network.game.SwitchShelvesResponseMessage;
import it.polimi.ingsw.utils.ResourceManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class WarehouseController implements SceneController{
    private GUI gui;
    private Map<String, HBox> shelfContainerMap;
    private List<String> selected;
    private Button confirmButton;
    @FXML
    private VBox container;

    @FXML
    private HBox buttonsContainer;

    public void load(Warehouse warehouse) {
        Platform.runLater(() -> {
            // display the warehouse
            container.getChildren().clear();
            shelfContainerMap = new HashMap<>();
            HBox warehouseContainer = new HBox();
            VBox normalShelvesContainer = new VBox();
            VBox extraShelvesContainer = new VBox();
            for (String shelfName : warehouse.getShelfNames()) {
                Shelf shelf = warehouse.getShelf(shelfName);
                if (Arrays.asList("bottom", "middle", "top").contains(shelfName)) {
                    HBox shelfResourcesContainer = new HBox();
                    shelfResourcesContainer.setPrefSize(129, 46);
                    shelfResourcesContainer.setAlignment(Pos.CENTER);
                    shelfResourcesContainer.setPadding(new Insets(0, 10, 0, 10));
                    shelfResourcesContainer.setSpacing(10);
                    for (int i = 0; i < shelf.getResourceNumber(); i++) {
                        ImageView resourceImageView = ResourceManager.getImageView(shelf.getResourceType());
                        resourceImageView.setFitWidth(40);
                        resourceImageView.setFitHeight(40);
                        shelfResourcesContainer.getChildren().add(resourceImageView);
                    }
                    shelfContainerMap.put(shelfName, shelfResourcesContainer);
                    normalShelvesContainer.getChildren().add(shelfResourcesContainer);
                } else {
                    AnchorPane extraShelfOuterContainer = new AnchorPane();
                    Image extraShelfImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                            "images/board/extra_" + shelf.getResourceType().name().toLowerCase() + ".png")));
                    ImageView extraShelfImageView = new ImageView(extraShelfImage);
                    extraShelfImageView.setPreserveRatio(true);
                    extraShelfImageView.setFitWidth(135);
                    extraShelfOuterContainer.setPrefSize(135, 60);
                    extraShelfOuterContainer.getChildren().add(extraShelfImageView);
                    HBox extraShelfResourcesContainer = new HBox();
                    extraShelfResourcesContainer.setPrefSize(
                            extraShelfOuterContainer.getPrefWidth(), extraShelfOuterContainer.getPrefHeight());
                    extraShelfResourcesContainer.setLayoutX(0);
                    extraShelfResourcesContainer.setLayoutY(0);
                    extraShelfResourcesContainer.setPadding(new Insets(0, 0, 0, 15));
                    extraShelfResourcesContainer.setSpacing(25);
                    extraShelfResourcesContainer.setAlignment(Pos.CENTER_LEFT);
                    for (int i = 0; i < shelf.getResourceNumber(); i++) {
                        ImageView resourceImageView = ResourceManager.getImageView(shelf.getResourceType());
                        resourceImageView.setFitWidth(40);
                        resourceImageView.setFitHeight(40);
                        extraShelfResourcesContainer.getChildren().add(resourceImageView);
                    }
                    shelfContainerMap.put(shelfName, extraShelfResourcesContainer);
                    extraShelfOuterContainer.getChildren().add(extraShelfResourcesContainer);
                    extraShelvesContainer.getChildren().add(extraShelfOuterContainer);
                }
            }
            normalShelvesContainer.setPrefSize(165, 177);
            normalShelvesContainer.setSpacing(10);
            normalShelvesContainer.setAlignment(Pos.BOTTOM_CENTER);
            warehouseContainer.getChildren().add(normalShelvesContainer);

            extraShelvesContainer.setPrefSize(147, 177);
            extraShelvesContainer.setSpacing(25);
            extraShelvesContainer.setAlignment(Pos.CENTER);
            warehouseContainer.getChildren().add(extraShelvesContainer);
            warehouseContainer.setLayoutX(9);
            warehouseContainer.setLayoutY(501);
            warehouseContainer.setSpacing(36);
            warehouseContainer.setAlignment(Pos.CENTER);
            container.getChildren().addAll(warehouseContainer, buttonsContainer);
        });
    }

    @FXML
    void requestSwitch(ActionEvent event) {
        ResourceManager.playClickSound();
        gui.getClient().sendMessage(new SelectMoveResponseMessage("SWITCH_SHELVES"));
    }

    void allowSwitch(List<String> shelves) {
        Platform.runLater(() -> {
            selected = new ArrayList<>();
            for (String shelfName : shelves) {
                shelfContainerMap.get(shelfName).setOnMouseClicked(event -> select(shelfName));
                shelfContainerMap.get(shelfName).setCursor(Cursor.HAND);
            }
            container.getChildren().remove(buttonsContainer);
            confirmButton = new Button("Confirm");
            confirmButton.getStyleClass().add("flat-button");
            confirmButton.setOnAction(event -> confirm());
            confirmButton.setDisable(true);
            HBox newButtonContainer = new HBox();
            newButtonContainer.setAlignment(Pos.CENTER);
            newButtonContainer.getChildren().add(confirmButton);
            container.getChildren().add(newButtonContainer);
        });
    }

    void select(String shelfName) {
        Platform.runLater(() -> {
            ResourceManager.playClickSound();
            if (selected.contains(shelfName)) {
                selected.remove(shelfName);
                shelfContainerMap.get(shelfName).setStyle("");
                confirmButton.setDisable(true);
            } else {
                if (selected.size() > 1) {
                    shelfContainerMap.get(selected.get(0)).setStyle("");
                    selected.remove(0);
                }
                selected.add(shelfName);
                shelfContainerMap.get(shelfName).setStyle("-fx-border-color: red;" +
                        "-fx-border-width: 2; -fx-border-style: solid inside;");
                if (selected.size() == 2) confirmButton.setDisable(false);
            }
        });
    }

    @FXML
    void confirm() {
        ResourceManager.playClickSound();
        if (selected.size() == 2)
            gui.getClient().sendMessage(new SwitchShelvesResponseMessage(selected.get(0), selected.get(1)));
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void cancel(ActionEvent actionEvent) {
        ResourceManager.playClickSound();
        gui.setScene("game-board");
    }
}
