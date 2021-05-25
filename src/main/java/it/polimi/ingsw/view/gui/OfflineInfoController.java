package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.utils.GameUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class OfflineInfoController implements SceneController{
    private GUI gui;

    @FXML
    public TextField usernameInput;
    public String username;


    @Override
    public void setGUI(GUI gui)  {
        this.gui = gui;
    }

    public void confirm(ActionEvent actionEvent) {
        Platform.runLater(() -> {
        username = usernameInput.getText();
        gui.setUsername(username);
        GameUtils.debug(username);
        if(username!= null)
            gui.initializeClient(false);

        });
    }

    public String getUsername() {
        return username;
    }
}
