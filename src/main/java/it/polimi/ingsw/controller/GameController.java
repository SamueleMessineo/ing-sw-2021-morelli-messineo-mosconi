package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.network.client.SelectLeaderCardsMessage;
import it.polimi.ingsw.network.client.StringMessage;
import it.polimi.ingsw.network.setup.Room;
import it.polimi.ingsw.server.ClientConnection;

import java.util.ArrayList;

public class GameController {
    private final Room room;
    private final Game game;

    public GameController(Room room) {
        this.room = room;
        this.game = room.getGame();
    }

    public void startGame(){
        room.sendAll(new StringMessage("Game started"));
        leaderCardsSelectionStep();

    }

    public void startSingleGame(){
        ClientConnection player = room.getConnections().get(0);
        player.sendMessage(new StringMessage("Single game started"));
        player.sendMessage(new SelectLeaderCardsMessage(getPlayerFromConnection(player).getLeaderCards()));

    }

    public void leaderCardsSelectionStep(){
        for (ClientConnection player:
                room.getConnections()) {
            player.sendMessage(new SelectLeaderCardsMessage(getPlayerFromConnection(player).getLeaderCards()));
        }
    }

    public void dropInitialLeaderCards(int selection1, int selection2, ClientConnection clientConnection){

        ArrayList <LeaderCard> leaderCards = getPlayerFromConnection(clientConnection).getLeaderCards();
        LeaderCard card1 = leaderCards.get(selection1);
        LeaderCard card2 = leaderCards.get(selection2);
        leaderCards.remove(card1);
        leaderCards.remove(card2);

    }

    public Game getGame() {
        return game;
    }

    public Player getPlayerFromConnection(ClientConnection clientConnection){
        return room.getGame().getPlayers().get(room.getConnections().indexOf(clientConnection));
    }
}
