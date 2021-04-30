package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.setup.Room;

import java.util.Random;

public class ClassicGameController extends GameController{
    private final Game game;

    public ClassicGameController(Room room) {
        this.game = room.getGame();
    }

    public void startGame(){
        //leaderCardsSelectionStep();
        selectStartingPlayer();
    }

    private void selectStartingPlayer() {
        Random r = new Random();
        game.setCurrentPlayer(r.nextInt(game.getPlayers().size()));
    }

    public Game getGame() {
        return game;
    }

    public void dropInitialLeaderCards(int selection1, int selection2, String player){
        game.getPlayerByUsername(player).dropInitialLeaderCards(selection1, selection2);

    }

    private boolean isGameOver(){
        for (Player player:
                game.getPlayers()) {
            if (player.getFaithTrack().getPosition() == player.getFaithTrack().getMaxposition())return true;
        }
        return false;
    }

    private void computeCurrentPlayer(){
        if (game.getPlayers().indexOf(game.getCurrentPlayer()) + 1 < game.getPlayers().size()) {
            game.setCurrentPlayer(game.getPlayers().indexOf(game.getCurrentPlayer()) + 1);
        } else {
            game.setCurrentPlayer(0);
        }
    }

    public void getMarbles(String rowOrColumn, int index){

    }


}
