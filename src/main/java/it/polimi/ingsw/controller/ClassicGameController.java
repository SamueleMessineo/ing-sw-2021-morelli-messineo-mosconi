package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarketCardStack;
import it.polimi.ingsw.model.player.FaithTrack;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.shared.*;
import it.polimi.ingsw.server.Room;
import it.polimi.ingsw.utils.GameUtils;

import java.security.InvalidParameterException;
import java.util.*;

public class ClassicGameController {
    private final Game game;

    public ClassicGameController(Room room) {
        this.game = room.getGame();
    }

    public ClassicGameController(Game game) { this.game = game; }

    public void startGame(){
        selectStartingPlayer();
    }

    private void selectStartingPlayer() {
        Random r = new Random();
        int firstPlayerIndex = r.nextInt(game.getPlayers().size());
        System.out.println("first player index: " + firstPlayerIndex);
        game.setCurrentPlayer(firstPlayerIndex);
        game.setInkwellPlayer(firstPlayerIndex);
    }

    public Game getGame() {
        return game;
    }

    public void movePlayer(String player, int positions) {
        for (int i = 0; i < positions; i++) {
            game.getPlayerByUsername(player).getFaithTrack().move();
        }
    }

    public void dropInitialLeaderCards(int selection1, int selection2, String player){
        game.getPlayerByUsername(player).dropInitialLeaderCards(selection1, selection2);
    }

    public void giveInitialResources(List<Resource> resources, String username) {
        Map<Resource, Integer> resourceMap = new HashMap<>();
        resourceMap.put(resources.get(0), resources.size());
        game.getPlayerByUsername(username).getPlayerBoard().getWarehouse()
                .placeOnShelf(resources.size() == 1 ? "top" : "middle", resourceMap);
    }

    public boolean isGameOver() {
        for (Player player:
                game.getPlayers()) {
            if ((player.getFaithTrack().getPosition() == FaithTrack.getMaxposition())||
                        player.getPlayerBoard().getCardStacks().get(0).size()+player.getPlayerBoard().getCardStacks().get(1).size()+player.getPlayerBoard().getCardStacks().get(2).size() == 7)return true;
        }
        return false;
    }

    public void computeNextPlayer(){
        int nextPlayer = (game.getPlayers().indexOf(game.getCurrentPlayer()) + 1) % game.getPlayers().size();
        game.setCurrentPlayer(nextPlayer);
        System.out.println(nextPlayer);
        if (!game.getPlayers().get(nextPlayer).isActive()) computeNextPlayer();
    }

    public Map<String, Map<Resource, Integer>> getMarbles(String rowOrColumn, int index) {
        List<Marble> marbles;
        if(rowOrColumn.equals("ROW")){
            marbles = game.getMarket().getMarbleStructure().shiftRow(index);
        } else {
            marbles = game.getMarket().getMarbleStructure().shiftColumn(index);
        }

        Map<String, Map<Resource, Integer>> convertedMarbles = new HashMap<>(){{
            put("converted", new HashMap<>());
            put("toConvert", new HashMap<>());
            put("conversionOptions", new HashMap<>());
        }};
        convertedMarbles.get("toConvert").put(Resource.ANY, 0);
        List<Resource> effectsObjects = game.getCurrentPlayer().hasActiveEffectOn("Marbles");
        for (Resource effectObject : effectsObjects) {
            convertedMarbles.get("conversionOptions").put(effectObject, 1);
        }

        // convert all the marbles
        for (Marble marble : marbles) {
            String key = "converted";
            Resource resource = null;
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
                case WHITE:
                    if (effectsObjects.size() == 2) {
                        // the player has two active leader cards with effects on the marble structure
                        // need to ask the user how to convert them
                        key = "toConvert";
                        resource = Resource.ANY;
                    } else if (effectsObjects.size() == 1) {
                        // the player only has one active leader card with the marbles as scope
                        // can automatically convert
                        resource = effectsObjects.get(0);
                    }
                    break;
                default:
                    resource = GameUtils.convertMarbleToResource(marble);
                    break;
            }
            convertedMarbles.put(key, GameUtils.incrementValueInResourceMap(
                    convertedMarbles.get(key), resource, 1));
        }
        return convertedMarbles;
    }

    public void giveResourcesToPlayer(Map<Resource, Integer> resources, String playerUsername)
            throws InvalidParameterException {
        Player p = game.getPlayerByUsername(playerUsername);
        if (!p.getPlayerBoard().getWarehouse().canPlaceResources(resources)) {
            throw new InvalidParameterException("Not enough space to place these resources");
        } else {
            p.getPlayerBoard().getWarehouse().placeResources(resources);
        }
    }

    public void dropPlayerResources(Map<Resource, Integer> obtainedResources, Map<Resource,
            Integer> resourcesToDrop, String playerUsername) throws InvalidParameterException {
        if (resourcesToDrop == null) throw new InvalidParameterException();
        for (Resource r : resourcesToDrop.keySet()) {
            if (!obtainedResources.containsKey(r) ||
                    obtainedResources.get(r) < resourcesToDrop.get(r)) {
                throw new InvalidParameterException();
            }
        }
        Player player = game.getPlayerByUsername(playerUsername);
        Map<Resource, Integer> resourcesToAdd = new HashMap<>(obtainedResources);
        for (Resource r : resourcesToDrop.keySet()) {
            resourcesToAdd = GameUtils.incrementValueInResourceMap(resourcesToAdd, r, -resourcesToDrop.get(r));
        }
        if (!player.getPlayerBoard().getWarehouse().canPlaceResources(resourcesToAdd))
            throw new InvalidParameterException();

        player.getPlayerBoard().getWarehouse().placeResources(resourcesToAdd);
    }

    public void dropLeader(int card) {
        game.getCurrentPlayer().getFaithTrack().move();
        game.getCurrentPlayer().dropLeaderCard(card);
    }

    public boolean switchShelves(String shelf1, String shelf2) {
        return game.getCurrentPlayer().getPlayerBoard().getWarehouse().switchShelves(shelf1, shelf2);
    }

    public List<String> computeNextPossibleMoves(boolean alreadyPerformedMove) {
        List<String> moves = new ArrayList<>();
        Player player = game.getCurrentPlayer();

        if(!alreadyPerformedMove){
            moves.add("GET_MARBLES");
            if (player.canActivateBasicProduction() || player.canActivateProduction()) {
                System.out.println("add acitvate production");
                moves.add("ACTIVATE_PRODUCTION");
            }
            for (MarketCardStack cardsStack : game.getMarket().getCardsGrid()) {
                if(!cardsStack.isEmpty()) {
                    DevelopmentCard topCard = cardsStack.peek();
                    if (player.canBuyAndPlaceDevelopmentCard(topCard)) {
                        System.out.println("add buy card;");
                        moves.add("BUY_CARD");
                        break;
                    }
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

    public void activateProduction(List<Integer> selectedStacks, ProductionPower basicProduction, List<Integer> extraProductionPowers){
        if (selectedStacks != null) {
            game.getCurrentPlayer().getPlayerBoard().activateProduction(selectedStacks);
        }
        if (basicProduction != null) {
           game.getCurrentPlayer().getPlayerBoard().activateProductionPower(basicProduction);
        }
        if(extraProductionPowers != null){
            for (Integer extraProductionPower : extraProductionPowers) {
                game.getCurrentPlayer().getPlayerBoard().activateProductionPower(game.getCurrentPlayer().getPlayerBoard().getExtraProductionPowers().get(extraProductionPower));
            }

        }
    }

    public void movePlayer(Player playerToMove, int positions){
        for (int i = 0; i < positions; i++) {
            game.getCurrentPlayer().getFaithTrack().move();
            if(playerToMove.getFaithTrack().inOnPopeSpace()!= 0){
                for (Player player:
                     game.getPlayers()) {
                    if(player.getFaithTrack().isInPopeFavorByLevel(playerToMove.getFaithTrack().inOnPopeSpace())){
                        player.getFaithTrack().getPopesFavorTiles().get(playerToMove.getFaithTrack().inOnPopeSpace()-1).setState(PopesFavorTileState.ACTIVE);
                    } else        player.getFaithTrack().getPopesFavorTiles().get(playerToMove.getFaithTrack().inOnPopeSpace()-1).setState(PopesFavorTileState.INACTIVE);
                }

                for (Player player:
                        game.getInactivePlayers()) {
                    if(player.getFaithTrack().isInPopeFavorByLevel(playerToMove.getFaithTrack().inOnPopeSpace())){
                        player.getFaithTrack().getPopesFavorTiles().get(playerToMove.getFaithTrack().inOnPopeSpace()-1).setState(PopesFavorTileState.ACTIVE);
                    } else        player.getFaithTrack().getPopesFavorTiles().get(playerToMove.getFaithTrack().inOnPopeSpace()-1).setState(PopesFavorTileState.INACTIVE);
                }
            }
        }
    }

    public void playLeader(int cardIndex) {
        LeaderCard leaderCard = getPlayableLeaderCards().get(cardIndex);
        game.getCurrentPlayer().playLeaderCard(leaderCard);

        if(leaderCard.getEffectScope().equals("Storage")){
            game.getCurrentPlayer().getPlayerBoard().expandWarehouse(leaderCard.getEffectObject());
        }
        if(leaderCard.getEffectScope().equals("Production")){
            game.getCurrentPlayer().getPlayerBoard().setExtraProductionPowers(leaderCard.getEffectObject());
        }
    }

    public Map<String, Integer> computeStanding() {
        Map<String, Integer> standing = new HashMap<>();
        List<Player> standingList = new ArrayList<>();

        standingList.add(game.getPlayers().get(0));

        for (Player player:
                game.getPlayers()) {
            if(player.getVP() >= standingList.get(0).getVP())standingList.add(0,player);
        }

        int points;
        for (Player player:
                standingList) {
             points = player.getVP();
             standing.put(player.getUsername(), points);
        }

        return standing;
    }

    public String computeWinner() {
        String winner = game.getPlayers().get(0).getUsername();
        int points =  game.getPlayers().get(0).getVP();

        for (Player player:
                game.getPlayers()) {
            if(player.getVP() >= points){
                winner=player.getUsername();
            }
        }
        return winner;
    }



}

