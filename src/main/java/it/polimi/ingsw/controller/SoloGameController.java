package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.client.DropLeaderCardsRequestMessage;
import it.polimi.ingsw.network.client.StringMessage;
import it.polimi.ingsw.network.setup.Room;
import it.polimi.ingsw.server.ClientConnection;

import java.util.List;

public class SoloGameController implements GameController {
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
        player.sendMessage(new DropLeaderCardsRequestMessage(room.getPlayerFromConnection(player).getLeaderCards()));
    }

    @Override
    public void dropInitialLeaderCards(int selection1, int selection2, String player) {

    }

    @Override
    public List<Resource> getMarbles(String rowOrColumn, int index) {
        return null;
    }

    @Override
    public void dropLeader(int card) {

    }

    @Override
    public void switchShelves(String shelf1, String shelf2) {

    }
}
