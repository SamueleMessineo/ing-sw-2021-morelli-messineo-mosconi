package it.polimi.ingsw.contoller;

import it.polimi.ingsw.controller.ClassicGameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.utils.GameUtils;

public class ClassicGameControllerTest {
    Game game1 = GameUtils.readGame(0001);
    ClassicGameController gameController = new ClassicGameController(game1);

}
