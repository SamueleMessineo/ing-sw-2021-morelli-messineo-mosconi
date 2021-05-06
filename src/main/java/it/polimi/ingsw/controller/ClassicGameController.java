package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarketCardStack;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Shelf;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.PopesFavorTileState;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.ProductionPower;
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
        int firstPlayerIndex = r.nextInt(game.getPlayers().size());
        game.setCurrentPlayer(firstPlayerIndex);
        game.setInkwellPlayer(firstPlayerIndex);
    }

    public Game getGame() {
        return game;
    }

    public void dropInitialLeaderCards(int selection1, int selection2, String player){
        game.getPlayerByUsername(player).dropInitialLeaderCards(selection1, selection2);

    }

    public boolean isGameOver() {
        for (Player player:
                game.getPlayers()) {
            if ((player.getFaithTrack().getPosition() == player.getFaithTrack().getMaxposition())||
                        player.getPlayerBoard().getCardStacks().get(0).size()+player.getPlayerBoard().getCardStacks().get(1).size()+player.getPlayerBoard().getCardStacks().get(2).size() == 7)return true;
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
            put("converted", new ArrayList<>());
            put("toConvert", new ArrayList<>());
        }};

        List<Resource> effectsObjects = game.getCurrentPlayer().hasActiveEffectOn("Marbles");
        convertedMarbles.put("conversionOptions", effectsObjects);

        // convert all the marbles
        for (Marble marble : marbles) {
            switch (marble) {
                case RED: // convert to vatican point
                    // move the player forward on the track
                    game.getCurrentPlayer().getFaithTrack().move();
                    // check if it landed on a Pope space
                    int playerOnPope = game.getCurrentPlayer().getFaithTrack().inOnPopeSpace();
                    if (playerOnPope != -1) {
                        // activate vatican report
                        for (Player player : game.getPlayers()) {
                            if (player.getFaithTrack().isInPopeFavorByLevel(playerOnPope)) {
                                // if a player is in the popes favour area, activate the corresponding tile
                                player.getFaithTrack().getPopesFavorTiles().get(playerOnPope)
                                        .setState(PopesFavorTileState.ACTIVE);
                            } else {
                                // if a player is not in the popes favour area, discard the corresponding tile
                                player.getFaithTrack().getPopesFavorTiles().get(playerOnPope)
                                        .setState(PopesFavorTileState.DISCARDED);
                            }
                        }
                    }
                    break;
                case BLUE: // convert to shied
                    convertedMarbles.get("converted").add(Resource.SHIELD);
                    break;
                case GREY: // convert to stone
                    convertedMarbles.get("converted").add(Resource.STONE);
                    break;
                case PURPLE: // convert to servant
                    convertedMarbles.get("converted").add(Resource.SERVANT);
                    break;
                case YELLOW: // convert to coin
                    convertedMarbles.get("converted").add(Resource.COIN);
                    break;
                case WHITE:
                    if (effectsObjects.size() == 2) {
                        // the player has two active leader cards with effects on the marble structure
                        // need to ask the user how to convert them
                        convertedMarbles.get("toConvert").add(Resource.ANY);
                    } else if (effectsObjects.size() == 1) {
                        // the player only has one active leader card with the marbles as scope
                        // can automatically convert
                        convertedMarbles.get("converted").add(effectsObjects.get(0));
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
        game.getCurrentPlayer().dropLeaderCard(card);
    }

    @Override
    public boolean switchShelves(String shelf1, String shelf2) {
        return game.getCurrentPlayer().getPlayerBoard().getWarehouse().switchShelves(shelf1, shelf2);
    }

    @Override
    public List<String> computeNextPossibleMoves(boolean alreadyPerformedMove) {
        List<String> moves = new ArrayList<>();
        Player player = game.getCurrentPlayer();
        System.out.println("compute moves");

        if(!alreadyPerformedMove){
            if (player.canActivateProduction()) {
                moves.add("ACTIVATE_PRODUCTION");
            }
            moves.add("GET_MARBLES");
            for (MarketCardStack cardsStack : game.getMarket().getCardsGrid()) {
                DevelopmentCard topCard = cardsStack.peek();
                if (player.canBuyAndPlaceDevelopmentCard(topCard)) {
                    moves.add("BUY_CARD");
                    break;
                }
            }
        } else {
            moves.add("END_TURN");
        }

        //FOR DEBUG
        //moves.add("END_TURN");

        if(player.getLeaderCards().size() > 0){
            moves.add("DROP_LEADER");
        }

        for (int i = 0; i < player.getLeaderCards().size(); i++) {
            if (player.canPlayLeader(i)) {
                moves.add("PLAY_LEADER");
                break;
            }
        }

        Warehouse warehouse = player.getPlayerBoard().getWarehouse();
        if(warehouse.canSwitchShelves(warehouse.getShelf("top"), warehouse.getShelf("middle"))||
                warehouse.canSwitchShelves(warehouse.getShelf("top"), warehouse.getShelf("bottom"))||
                warehouse.canSwitchShelves(warehouse.getShelf("top"), warehouse.getShelf("extra1"))||
                warehouse.canSwitchShelves(warehouse.getShelf("top"), warehouse.getShelf("extra2"))||
                warehouse.canSwitchShelves(warehouse.getShelf("middle"), warehouse.getShelf("bottom"))||
                warehouse.canSwitchShelves(warehouse.getShelf("middle"), warehouse.getShelf("extra1"))||
                warehouse.canSwitchShelves(warehouse.getShelf("middle"), warehouse.getShelf("extra2"))||
                warehouse.canSwitchShelves(warehouse.getShelf("bottom"), warehouse.getShelf("extra1"))||
                warehouse.canSwitchShelves(warehouse.getShelf("bottom"), warehouse.getShelf("extra2"))

        ) moves.add("SWITCH_SHELVES");

        return moves;
    }

    public List<DevelopmentCard> getBuyableDevelopementCards(){
        List<DevelopmentCard> developmentCards = new ArrayList<>();

        for (MarketCardStack cardsStack : game.getMarket().getCardsGrid()) {
            DevelopmentCard topCard = cardsStack.peek();
            if (game.getCurrentPlayer().canBuyAndPlaceDevelopmentCard(topCard)) {
                developmentCards.add(topCard);
            }
        }
        return developmentCards;
    }

    public List<LeaderCard> getPlayableLeaderCards(){
        List<LeaderCard> leaderCards = new ArrayList<>();

        for (int i = 0; i < game.getCurrentPlayer().getLeaderCards().size(); i++) {
            if (game.getCurrentPlayer().canPlayLeader(i)) {
                leaderCards.add(game.getCurrentPlayer().getLeaderCards().get(i));
            }
        }
        return leaderCards;
    }

    @Override
    public void playLeader(int cardIndex) {
        LeaderCard leaderCard = getPlayableLeaderCards().get(cardIndex);
        game.getCurrentPlayer().getPlayedLeaderCards().add(leaderCard);
        game.getCurrentPlayer().getLeaderCards().remove(leaderCard);

        if(leaderCard.getEffectScope().equals("Storage")){
            game.getCurrentPlayer().getPlayerBoard().expandWarehouse(leaderCard.getEffectObject());
        }
        if(leaderCard.getEffectScope().equals("Production")){
            game.getCurrentPlayer().getPlayerBoard().setExtraProductionPowers(leaderCard.getEffectObject());
        }
    }
}

