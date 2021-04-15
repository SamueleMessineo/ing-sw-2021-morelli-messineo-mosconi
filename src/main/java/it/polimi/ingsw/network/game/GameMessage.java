package it.polimi.ingsw.network.game;

import it.polimi.ingsw.network.GameMessageHandler;
import it.polimi.ingsw.network.Message;

public abstract class GameMessage extends Message {
    void accept(GameMessageHandler handler) {}
}
