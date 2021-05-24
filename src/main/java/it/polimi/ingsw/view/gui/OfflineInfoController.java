package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.utils.GameUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class OfflineInfoController implements SceneController{
    private GUI gui;

    @FXML
    public TextField username;

    public String askUsername() {

        GameUtils.debug("asking");
        String username;
        username = this.username.getText();
        return username;

    }


    @Override
    public void setGUI(GUI gui)  {
        this.gui = gui;
    }



    public void confirm(ActionEvent actionEvent) {
        gui.initializeClient(false);
        gui.getClient().run();
    }
}
