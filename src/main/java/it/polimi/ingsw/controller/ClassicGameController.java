package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.server.Room;

import java.util.*;

public class ClassicGameController implements GameController{
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
    public Map<String, List<Resource>> getMarbles(String rowOrColumn, int index) {
        List<Marble> marbles;
        if(rowOrColumn.equals("ROW")){
            marbles = game.getMarket().getMarbleStructure().shiftRow(index);
        } else {
            marbles = game.getMarket().getMarbleStructure().shiftColumn(index);
        }

        Map<String, List<Resource>> convertedMarbles = new HashMap<>(){{
            put("white", new ArrayList<>());
            put("notWhite", new ArrayList<>());
        }};

        int nWhiteMarblesToAskConvert = 0;
        List<Resource> resources = game.getCurrentPlayer().hasActiveEffectOn("Marbles");
        convertedMarbles.put("conversionOptions", resources);

        for (Marble marble : marbles) {
            switch (marble) {
                case RED:
                    //convertedMarbles.get("notWhite").add(Resource.FAITH);
                    break;
                case BLUE:
                    convertedMarbles.get("notWhite").add(Resource.SHIELD);
                    break;
                case GREY:
                    convertedMarbles.get("notWhite").add(Resource.STONE);
                    break;
                case PURPLE:
                    convertedMarbles.get("notWhite").add(Resource.SERVANT);
                    break;
                case YELLOW:
                    convertedMarbles.get("notWhite").add(Resource.COIN);
                    break;
                case WHITE:
                    if (resources.size() == 2) {
                        // need to ask the user how to convert
                        //nWhiteMarblesToAskConvert++;
                        convertedMarbles.get("white").add(Resource.ANY);
                    } else if (resources.size() == 1) {
                        // can automatically convert
                        convertedMarbles.get("white").add(resources.get(0));
                    }
                    break;
                default:
                    break;
            }
        }
        return convertedMarbles;
    }

    @Override
    public void dropLeader(int card) {
        game.getCurrentPlayer().getFaithTrack().move();
        game.getCurrentPlayer().getLeaderCards().remove(card);
    }

    @Override
    public void switchShelves(String shelf1, String shelf2) {
        game.getCurrentPlayer().getPlayerBoard().getWarehouse().switchShelves(shelf1, shelf2);
    }
}
