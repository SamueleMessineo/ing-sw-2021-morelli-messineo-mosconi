package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.game.ActivateProductionResponseMessage;
import it.polimi.ingsw.utils.GameUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private ProductionPower basicProduction;
    private List<Integer> selectedCardPowers;
    @FXML
    private VBox vbox;
    @FXML
    private HBox productionsContainer;
    @FXML
    private HBox buttonsContainer;

    public void load(List<ProductionPower> powers) {
        player = gui.getGame().getPlayerByUsername(gui.getUsername());
        Platform.runLater(() -> {
            vbox.getChildren().clear();
            productionsContainer.getChildren().clear();
            // check default production
            if (player.canActivateBasicProduction()) {
                Image basicProductionImage = new Image(Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream("images/board/basic_production_scroll.png")));
                ImageView basicProductionImageView = new ImageView(basicProductionImage);
                basicProductionImageView.setPreserveRatio(true);
                basicProductionImageView.setFitWidth(150);
                basicProductionImageView.setOnMouseClicked(this::handleBasicProduction);
                productionsContainer.getChildren().add(basicProductionImageView);
            }
            // display cards' powers
            selectedCardPowers = new ArrayList<>();
            for (ProductionPower cardPower : powers) {
                AnchorPane powerPane = new AnchorPane();
                powerPane.setPrefSize(212,150);
                Image bookImage = new Image(Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream("images/board/production_book.png")));
                ImageView bookImageView = new ImageView(bookImage);
                bookImageView.setPreserveRatio(true);
                bookImageView.setFitHeight(150);
                bookImageView.setFitWidth(212);
                bookImageView.setLayoutX(0);
                bookImageView.setLayoutY(0);
                powerPane.getChildren().add(bookImageView);
                HBox singleCardPowerContainer = new HBox();
                singleCardPowerContainer.setPrefSize(212,150);
                singleCardPowerContainer.setAlignment(Pos.CENTER);
                singleCardPowerContainer.setSpacing(60);
                VBox inputContainer = new VBox();
                inputContainer.setAlignment(Pos.CENTER);
                for (Resource inputResource : cardPower.getInput().keySet()) {
                    ImageView resourceImageView = GameUtils.getImageView(inputResource);
                    resourceImageView.setFitWidth(35);
                    resourceImageView.setFitHeight(35);
                    inputContainer.getChildren().add(resourceImageView);
                }
                singleCardPowerContainer.getChildren().add(inputContainer);
                VBox outputContainer = new VBox();
                outputContainer.setAlignment(Pos.CENTER);
                for (Resource outputResource : cardPower.getOutput().keySet()) {
                    ImageView resourceImageView = GameUtils.getImageView(outputResource);
                    resourceImageView.setFitWidth(35);
                    resourceImageView.setFitHeight(35);
                    outputContainer.getChildren().add(resourceImageView);
                }
                singleCardPowerContainer.getChildren().add(outputContainer);
                singleCardPowerContainer.setOnMouseClicked(event -> selectCardPower(powers.indexOf(cardPower)));
                singleCardPowerContainer.setLayoutX(0);
                singleCardPowerContainer.setLayoutY(0);
                powerPane.getChildren().add(singleCardPowerContainer);
                productionsContainer.getChildren().add(powerPane);
            }

            vbox.getChildren().addAll(productionsContainer, buttonsContainer);
        });
    }

    void handleBasicProduction(MouseEvent event) {
        Platform.runLater(() -> {
            VBox popupContainer = new VBox();
            basicProduction = new ProductionPower(GameUtils.emptyResourceMap(), GameUtils.emptyResourceMap());
            Map<Resource, Integer> playerResources = player.getPlayerBoard().getResources();

            for (Resource resource : new HashMap<>(playerResources).keySet()) {
                if (playerResources.get(resource) == 0) playerResources.remove(resource);
            }
            for (int i = 0; i < 2; i++) {
                HBox resourcePickerContainer = new HBox();
                for (Map.Entry<Resource, Integer> entry : playerResources.entrySet()) {
                    ImageView resourceImageView = GameUtils.getImageView(entry.getKey());
                    resourceImageView.setFitWidth(40);
                    resourceImageView.setFitHeight(40);
                    resourceImageView.setCursor(Cursor.HAND);
                    resourceImageView.setOnMouseClicked(event1 -> resourceClick(entry.getKey(), basicProduction.getInput(), resourcePickerContainer));
                    resourcePickerContainer.getChildren().add(resourceImageView);
                }
                popupContainer.getChildren().addAll(new Text("select input"),resourcePickerContainer);
            }
            HBox outputPickerContainer = new HBox();
            for (Resource resource : Arrays.asList(Resource.COIN, Resource.SERVANT, Resource.SHIELD, Resource.STONE)) {
                ImageView resourceImageView = GameUtils.getImageView(resource);
                resourceImageView.setFitWidth(40);
                resourceImageView.setFitHeight(40);
                resourceImageView.setCursor(Cursor.HAND);
                resourceImageView.setOnMouseClicked(event1 -> resourceClick(resource, basicProduction.getOutput(), outputPickerContainer));
                outputPickerContainer.getChildren().add(resourceImageView);
            }
            popupContainer.getChildren().addAll(new Text("select output"), outputPickerContainer);
            Button button = new Button("Confirm");
            button.getStyleClass().add("flat-button");
            button.setOnAction(this::confirmBasic);
            popupContainer.getChildren().add(button);
            gui.displayPopup(popupContainer);
        });
    }

    void resourceClick(Resource resource, Map<Resource, Integer> map, HBox pickerContainer) {
        Platform.runLater(() -> {
            GameUtils.incrementValueInResourceMap(map, resource, 1);
            for (Node imageView : pickerContainer.getChildren()) {
                imageView.setDisable(true);
            }
        });
    }

    @FXML
    void confirmBasic(ActionEvent event) {
        if (player.getPlayerBoard().canPayResources(basicProduction.getInput())) {
            player.getPlayerBoard().payResourceCost(basicProduction.getInput());
        } else {
            gui.displayError("You don't own the selected resources.");
        }
        gui.getPopupStage().close();
    }

    @FXML
    void selectCardPower(int index) {
        selectedCardPowers.add(index);
    }

    @FXML
    void confirm(ActionEvent event) {
        gui.getClient().sendMessage(new ActivateProductionResponseMessage(
                selectedCardPowers, basicProduction, null, null));
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
