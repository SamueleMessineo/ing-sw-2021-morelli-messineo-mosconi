package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.player.Shelf;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.utils.GameUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class WarehouseController implements SceneController{
    private GUI gui;
    private Map<String, HBox> shelfContainerMap;
    @FXML
    private VBox container;

    @FXML
    private HBox buttonsContainer;

    public void load(Warehouse warehouse){
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
                    ImageView resourceImageView = GameUtils.getImageView(shelf.getResourceType());
                    resourceImageView.setFitWidth(40);
                    resourceImageView.setFitHeight(40);
                    shelfResourcesContainer.getChildren().add(resourceImageView);
                }
                shelfContainerMap.put(shelfName, shelfResourcesContainer);
                normalShelvesContainer.getChildren().add(shelfResourcesContainer);
            }else {
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
                    ImageView resourceImageView = GameUtils.getImageView(shelf.getResourceType());
                    resourceImageView.setFitWidth(40);
                    resourceImageView.setFitHeight(40);
                    extraShelfResourcesContainer.getChildren().add(resourceImageView);
                }
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
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void cancel(ActionEvent actionEvent) {
        gui.setScene("game-board");
    }
}
