package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.DropLeaderCardsRequestMessage;
import it.polimi.ingsw.network.client.PossibleMovesMessage;
import it.polimi.ingsw.network.client.StringMessage;
import it.polimi.ingsw.network.client.GameStateMessage;
import it.polimi.ingsw.network.setup.Room;
import it.polimi.ingsw.server.ClientConnection;

import java.util.ArrayList;
import java.util.Random;

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
        selectStartingPlayer();
    }

    private void selectStartingPlayer() {
        Random r = new Random();
        room.getGame().setCurrentPlayer(r.nextInt(room.getNumberOfPlayers()));
    }

    private void leaderCardsSelectionStep(){
        for (ClientConnection player:
                room.getConnections()) {
            player.sendMessage(new DropLeaderCardsRequestMessage(getPlayerFromConnection(player).getLeaderCards()));
        }
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayerFromConnection(ClientConnection clientConnection){
        return room.getGame().getPlayers().get(room.getConnections().indexOf(clientConnection));
    }

    public void dropInitialLeaderCards(int selection1, int selection2, ClientConnection clientConnection){
        getPlayerFromConnection(clientConnection).dropInitialLeaderCards(selection1, selection2);
        startPlayingIfReady(clientConnection);
    }

    private void startPlayingIfReady(ClientConnection clientConnection) {
        for (Player p : room.getGame().getPlayers()) {
            System.out.println(p.getLeaderCards().size());
            if (p.getLeaderCards().size() != 2) return;
        }
        room.sendAll(new GameStateMessage(room.getGame()));
        room.getConnections().get(room.getGame().getPlayers().indexOf(
                room.getGame().getCurrentPlayer())).sendMessage(new PossibleMovesMessage(new ArrayList<>(){{
                    add("DROP_LEADER");
                    add("GET_MARBLES");
        }}));
    }
}
