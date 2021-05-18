package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.setup.CreateRoomMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CreateGameController implements SceneController {
    private GUI gui;
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField playerNumberInput;
    @FXML
    private Button createButton;
    private final Text errorMessage = new Text() {{getStyleClass().add("light-text");}};
    @FXML
    private VBox form;
    @FXML
    private CheckBox privateInput;

    @FXML
    void create(ActionEvent event) {
        String username;
        int playerNumber;
        boolean privateGame;
        form.getChildren().remove(errorMessage);
        try {
            username = usernameInput.getText();
            playerNumber = Integer.parseInt(playerNumberInput.getText());
            privateGame = privateInput.isSelected();
            gui.getClient().sendMessage(new CreateRoomMessage(privateGame, playerNumber, username));
        } catch (NumberFormatException e) {
            errorMessage.setText("Invalid input");
            form.getChildren().add(errorMessage);
        }
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
