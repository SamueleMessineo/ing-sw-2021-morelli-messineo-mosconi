package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.network.setup.CreateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPrivateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPublicRoomMessage;
import it.polimi.ingsw.utils.ResourceManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Arrays;

/**
 * The controller for the game creation or joining scene.
 */
public class SetupGameController implements SceneController{
    private GUI gui;
    @FXML
    private VBox form;
    @FXML
    private Button startButton;
    private final Text usernameLabel = new Text("Enter your username"){{getStyleClass().add("light-text");}};
    private final TextField usernameInput = new TextField();
    private final Text roomIDLabel = new Text("Enter the room ID"){{getStyleClass().add("light-text");}};
    private final TextField roomIDInput = new TextField();
    private final Text playerNumberLabel = new Text("Enter the number of players"){{getStyleClass().add("light-text");}};
    private final TextField playerNumberInput = new TextField();
    private final CheckBox privateCheckbox = new CheckBox(){{setSelected(true);setText("private");setStyle("-fx-text-fill: white;");}};
    private final Text errorMessage = new Text("Invalid format"){{getStyleClass().add("light-text");}};
    private String view = "CREATE";

    /**
     * Starts the game based on the current view (CREATE, PUBLIC, PRIVATE).
     * @param event the javafx event.
     */
    @FXML
    void start(ActionEvent event) {
        Platform.runLater(() -> {
            ResourceManager.playClickSound();
            Client client = gui.getClient();
            String username = usernameInput.getText();
            gui.setUsername(username);
            form.getChildren().remove(errorMessage);
            switch (view) {
                case "CREATE":
                    try {
                        int playerNumber = Integer.parseInt(playerNumberInput.getText());
                        boolean privateGame = privateCheckbox.isSelected();
                        if(playerNumber==1 && gui.getUsername().trim().equalsIgnoreCase("lorenzoilmagnifico")){
                            gui.displayError("There is only one Lorenzo il Magnifico");
                            gui.setScene("setup-game");
                        } else client.sendMessage(new CreateRoomMessage(privateGame, playerNumber, username));
                    } catch (NumberFormatException e) {
                        errorMessage.setText("The number of players is invalid.");
                        form.getChildren().add(errorMessage);
                    }
                    break;
                case "PUBLIC":
                    try {
                        int playerNumber = Integer.parseInt(playerNumberInput.getText());
                        client.sendMessage(new JoinPublicRoomMessage(playerNumber, username));
                    } catch (NumberFormatException e) {
                        errorMessage.setText("The number of players is invalid");
                        form.getChildren().add(errorMessage);
                    }
                    break;
                case "PRIVATE":
                    try {
                        int roomID = Integer.parseInt(roomIDInput.getText());
                        if (roomID < 1000 || roomID > 9999) throw new NumberFormatException();
                        client.sendMessage(new JoinPrivateRoomMessage(roomID, username));
                    } catch (NumberFormatException e) {
                        errorMessage.setText("The room ID is invalid, enter a number between 1000 and 9999");
                        form.getChildren().add(errorMessage);
                    }
                    break;
            }
        });
    }

    /**
     * Show the form with the inputs required to create a game.
     * @param event the javafx event.
     */
    @FXML
    void createGame(ActionEvent event) {
        Platform.runLater(() -> {
            ResourceManager.playClickSound();
            startButton.setVisible(true);
            view = "CREATE";
            form.getChildren().clear();
            Text text = new Text("Create a room");
            text.getStyleClass().add("light-text");
            text.setFont(Font.font("System", FontWeight.BLACK, 18));
            form.getChildren().addAll(Arrays.asList(text,
                    usernameLabel, usernameInput, playerNumberLabel, playerNumberInput, privateCheckbox));
        });
    }

    /**
     * Show the form with the inputs required to join a private room.
     * @param event the javafx event.
     */
    @FXML
    void joinPrivate(ActionEvent event) {
        Platform.runLater(() -> {
            ResourceManager.playClickSound();
            startButton.setVisible(true);
            view = "PRIVATE";
            form.getChildren().clear();
            Text text = new Text("Join a private room");
            text.getStyleClass().add("light-text");
            text.setFont(Font.font("System", FontWeight.BLACK, 18));
            form.getChildren().addAll(Arrays.asList(
                    text, usernameLabel, usernameInput, roomIDLabel, roomIDInput));
        });
    }

    /**
     * Show the form with the inputs required to join a public game.
     * @param event the javafx event.
     */
    @FXML
    void joinPublic(ActionEvent event) {
        Platform.runLater(() -> {
            ResourceManager.playClickSound();
            startButton.setVisible(true);
            view = "PUBLIC";
            form.getChildren().clear();
            Text text = new Text("Join a public room");
            text.getStyleClass().add("light-text");
            text.setFont(Font.font("System", FontWeight.BLACK, 18));
            form.getChildren().addAll(Arrays.asList(text,
                    usernameLabel, usernameInput, playerNumberLabel, playerNumberInput));
        });
    }
    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
