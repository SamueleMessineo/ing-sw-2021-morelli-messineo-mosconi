package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.utils.GameUtils;
import it.polimi.ingsw.utils.ResourceManager;
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
        ResourceManager.playClickSound();
        Platform.runLater(() -> {
            username = usernameInput.getText();
            if(!username.trim().equalsIgnoreCase("lorenzoilmagnifico")){
                gui.setUsername(username);
                GameUtils.debug(username);
                if(username!= null)
                    gui.initializeClient(false);
            }
           else {
               gui.displayError("There is only one Lorenzo il Magnifico");
               gui.setScene("offline-info");
            }
        });
    }

    public String getUsername() {
        return username;
    }
}
