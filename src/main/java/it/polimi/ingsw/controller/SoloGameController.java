package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.client.SelectLeaderCardsMessage;
import it.polimi.ingsw.network.client.StringMessage;
import it.polimi.ingsw.network.setup.Room;
import it.polimi.ingsw.server.ClientConnection;

public class SoloGameController extends GameController {
    private final Room room;
    private final Game game;

    public SoloGameController(Room room) {
        this.room = room;
        this.game = room.getGame();
    }

    public void startGame(){
        ClientConnection player = room.getConnections().get(0);
        leaderCardsSelectionStep();
    }

    public void leaderCardsSelectionStep(){
        ClientConnection player = room.getConnections().get(0);
        player.sendMessage(new StringMessage("Single game started"));
        player.sendMessage(new SelectLeaderCardsMessage(getPlayerFromConnection(player).getLeaderCards()));
    }
}
