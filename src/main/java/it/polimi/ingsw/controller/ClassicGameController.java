package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.setup.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClassicGameController extends GameController{
    private final Game game;

    public ClassicGameController(Room room) {
        this.game = room.getGame();
    }

    public void startGame(){
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

    @Override
    public List<Resource> getMarbles(String rowOrColumn, int index) {
        List<Marble> marbles;
        if(rowOrColumn.equals("ROW")){
            marbles = game.getMarket().getMarbleStructure().shiftRow(index);
            System.out.println("fatto");
            System.out.println(marbles);
            return calculateEquivalentResources(marbles);
        } else {
            return calculateEquivalentResources(game.getMarket().getMarbleStructure().shiftColumn(index));
        }
    }

    private List<Resource> calculateEquivalentResources(List<Marble> marbles){
        List<Resource> resources = new ArrayList<>();
        for (Marble marble:
                marbles) {
            switch (marble){
                case RED:
                    resources.add(Resource.FAITH);
                    break;
                case BLUE:
                    resources.add(Resource.SHIELD);
                    break;
                case GREY:
                    resources.add(Resource.STONE);
                    break;
                case PURPLE:
                    resources.add(Resource.SERVANT);
                    break;
                case YELLOW:
                    resources.add(Resource.COIN);
                    break;
                default:
                    break;
            }
        }
        return resources;
    }

}
