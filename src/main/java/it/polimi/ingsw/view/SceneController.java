package it.polimi.ingsw.view;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class SceneController {
    private Scene scene;

    public SceneController(Scene scene) {
        this.scene = scene;
    }

    public void setRoot(Parent pane) {
        scene.setRoot(pane);
    }
}
