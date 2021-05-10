package it.polimi.ingsw.network.game;

import it.polimi.ingsw.client.LocalMessageHandler;
import it.polimi.ingsw.network.GameMessageHandler;
import it.polimi.ingsw.network.Message;

public abstract class GameMessage implements Message {
    public void accept(GameMessageHandler handler) {}
    public void accept(LocalMessageHandler handler) {}

    public String getType() {return "GAME";}
}
