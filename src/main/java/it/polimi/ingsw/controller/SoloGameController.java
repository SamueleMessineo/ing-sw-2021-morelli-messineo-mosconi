package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.client.DropInitialLeaderCardsRequestMessage;
import it.polimi.ingsw.network.client.StringMessage;
import it.polimi.ingsw.server.Room;
import it.polimi.ingsw.server.ClientConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        player.sendMessage(new DropInitialLeaderCardsRequestMessage(room.getPlayerFromConnection(player).getLeaderCards()));
    }

    @Override
    public void dropInitialLeaderCards(int selection1, int selection2, String player) {

    }

    @Override
    public Map<String, List<Resource>> getMarbles(String rowOrColumn, int index) {
        return null;
    }

    public void computeCurrentPlayer(){

    }

    @Override
    public void dropLeader(int card) {

    }

    @Override
    public boolean switchShelves(String shelf1, String shelf2) {
        return false;
    }

    @Override
    public List<String> computeNextPossibleMoves(boolean alreadyPerfomedMove) {
        return new ArrayList<>();
    }

    @Override
    public List<DevelopmentCard> getBuyableDevelopementCards() {
        return null;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    @Override
    public void playLeader(int cardIndex) {

    }

    @Override
    public List<LeaderCard> getPlayableLeaderCards() {
        return null;
    }

    @Override
    public Map<String, Integer> computeStanding() {
        return null;
    }

    @Override
    public String computeWinner() {
        return null;
    }
}
