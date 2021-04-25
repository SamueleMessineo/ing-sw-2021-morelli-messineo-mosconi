package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.network.client.SelectLeaderCardsMessage;
import it.polimi.ingsw.network.client.StringMessage;
import it.polimi.ingsw.network.setup.Room;
import it.polimi.ingsw.server.ClientConnection;

import java.util.ArrayList;

public class ClassicGameController extends GameController{
    private final Room room;
    private final Game game;

    public ClassicGameController(Room room) {

        this.room = room;
        this.game = room.getGame();
    }


    public void startGame(){
        room.sendAll(new StringMessage("Game started"));
        leaderCardsSelectionStep();

    }



    public void leaderCardsSelectionStep(){
        for (ClientConnection player:
                room.getConnections()) {
            player.sendMessage(new SelectLeaderCardsMessage(getPlayerFromConnection(player).getLeaderCards()));
        }
    }



    public Game getGame() {
        return game;
    }

    public Player getPlayerFromConnection(ClientConnection clientConnection){
        return room.getGame().getPlayers().get(room.getConnections().indexOf(clientConnection));
    }
}
