package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.network.setup.CreateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPrivateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPublicRoomMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Arrays;

public class SetupGameController implements SceneController{
    private GUI gui;
    @FXML
    private VBox form;
    private final Text usernameLabel = new Text("Enter your username"){{getStyleClass().add("light-text");}};
    private final TextField usernameInput = new TextField();
    private final Text roomIDLabel = new Text("Enter the room ID"){{getStyleClass().add("light-text");}};
    private final TextField roomIDInput = new TextField();
    private final Text playerNumberLabel = new Text("Enter the number of players"){{getStyleClass().add("light-text");}};
    private final TextField playerNumberInput = new TextField();
    private final CheckBox privateCheckbox = new CheckBox(){{setSelected(true);setText("private");setStyle("-fx-text-fill: white;");}};
    private final Text errorMessage = new Text("Invalid format"){{getStyleClass().add("light-text");}};
    private String view = "CREATE";

    @FXML
    void start(ActionEvent event) {
        Client client = gui.getClient();
        String username = usernameInput.getText();
        form.getChildren().remove(errorMessage);
        switch (view) {
            case "CREATE":
                try {
                    int playerNumber = Integer.parseInt(playerNumberInput.getText());
                    boolean privateGame = privateCheckbox.isSelected();
                    client.sendMessage(new CreateRoomMessage(privateGame, playerNumber, username));
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
    }

    @FXML
    void createGame(ActionEvent event) {
        view = "CREATE";
        form.getChildren().clear();
        form.getChildren().addAll(Arrays.asList(
                usernameLabel, usernameInput, playerNumberLabel, playerNumberInput, privateCheckbox));
    }

    @FXML
    void joinPrivate(ActionEvent event) {
        view = "PRIVATE";
        form.getChildren().clear();
        form.getChildren().addAll(Arrays.asList(
                usernameLabel, usernameInput, roomIDLabel, roomIDInput));
    }

    @FXML
    void joinPublic(ActionEvent event) {
        view = "PUBLIC";
        form.getChildren().clear();
        form.getChildren().addAll(Arrays.asList(
                usernameLabel, usernameInput, playerNumberLabel, playerNumberInput));
    }
    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
