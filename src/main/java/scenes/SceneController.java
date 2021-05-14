package scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SceneController {

    @FXML
    private Button offlineButton;

    @FXML
    private Button onlineButton;

    @FXML
    void playOffline(ActionEvent event) {
        System.out.println("offline");
    }

    @FXML
    void playOnline(ActionEvent event) {
        System.out.println("online");
    }

}
