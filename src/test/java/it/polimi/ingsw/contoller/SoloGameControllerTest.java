package it.polimi.ingsw.contoller;

import it.polimi.ingsw.controller.SoloGameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.utils.GameUtils;

public class SoloGameControllerTest {
    Game game1 = GameUtils.readGame(0000);
    SoloGameController controller = new SoloGameController(game1);
}
