package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarketCardStack;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.server.Room;

import java.util.*;
import java.util.stream.IntStream;

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

    public void computeCurrentPlayer(){
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
    public boolean switchShelves(String shelf1, String shelf2) {
        return game.getCurrentPlayer().getPlayerBoard().getWarehouse().switchShelves(shelf1, shelf2);
    }

    @Override
    public List<String> computeNextPossibleMoves(boolean alreadyPerfomedMove) {
        List<String> moves = new ArrayList<>();
        Player player = game.getCurrentPlayer();

        if(!alreadyPerfomedMove){
            //System.out.println(player.getPlayerBoard().getResources().values().stream().flatMapToInt(num -> IntStream.of(Integer.parseInt(String.valueOf(num)))).sum());
            //Se non funzionerà qualcosa forse sarà la riga seguente
            if(player.getPlayerBoard().getResources().values().stream().flatMapToInt(num -> IntStream.of(Integer.parseInt(String.valueOf(num)))).sum()>=2)moves.add("ACTIVATE_PRODUCTION");
            moves.add("GET_MARBLES");

            for (MarketCardStack cardsStack:
                 game.getMarket().getCardsGrid()) {
                 //player.canBuyDevelopementCard(cardStack.peek)
            }


            moves.add("BUY_CARD");
        } else {
            moves.add("END_TURN");
        }

        //FOR DEBUG
        moves.add("END_TURN");

        if(player.getLeaderCards().size() > 0){
            moves.add("DROP_LEADER");
        }

        for (int i = 0; i < player.getLeaderCards().size(); i++) {
            if(player.canPlayLeader(i)){
                System.out.println(i);
                moves.add("PLAY_LEADER");
                break;
            }
        }

        if(player.getPlayerBoard().getWarehouse().getShelf("top").getResourceNumber()>0||
                player.getPlayerBoard().getWarehouse().getShelf("middle").getResourceNumber()>0||
                player.getPlayerBoard().getWarehouse().getShelf("bottom").getResourceNumber()>0){
            moves.add("SWITCH_SHELVES");
        }
        return moves;
    }
}
