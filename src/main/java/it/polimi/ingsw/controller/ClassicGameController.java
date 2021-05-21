package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarketCardStack;
import it.polimi.ingsw.model.player.FaithTrack;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerCardStack;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.shared.*;
import it.polimi.ingsw.network.game.GameMessage;
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
        System.out.println(game.getPlayers().get(nextPlayer));
        if (!game.getPlayers().get(nextPlayer).isActive()) computeNextPlayer();
    }

    public Map<String, Map<Resource, Integer>> getMarbles(String rowOrColumn, int index) {
        List<Marble> marbles;
        if(rowOrColumn.equals("ROW")){
            marbles = game.getMarket().getMarbleStructure().shiftRow(index);
        } else {
            marbles = game.getMarket().getMarbleStructure().shiftColumn(index);
        }

        Map<String, Map<Resource, Integer>> convertedMarbles = new HashMap<>();
        convertedMarbles.put("converted", new HashMap<>());
        convertedMarbles.put("toConvert", new HashMap<>());
        convertedMarbles.get("toConvert").put(Resource.ANY, 0);
        convertedMarbles.put("conversionOptions", new HashMap<>());
        List<Resource> effectsObjects = game.getCurrentPlayer().hasActiveEffectOn("Marbles");
        for (Resource effectObject : effectsObjects) {
            convertedMarbles.get("conversionOptions").put(effectObject, 1);
        }

        // convert all the marbles
        for (Marble marble : marbles) {
            System.out.println(marble.name());
            if (marble == Marble.RED) {
                // move the player forward on the track
                game.getCurrentPlayer().getFaithTrack().move();
                // check if it landed on a Pope space
//                tryPopeReport(game.getCurrentPlayer());
                activatePopeReport();
            } else {
                String key = "converted";
                Resource resource = null;
                if (marble == Marble.WHITE) {
                    if (effectsObjects.size() == 2) {
                        // the player has two active leader cards with effects on the marble structure
                        // need to ask the user how to convert them
                        key = "toConvert";
                        resource = Resource.ANY;
                    } else if (effectsObjects.size() == 1) {
                        // the player only has one active leader card with the marbles as scope
                        // can automatically convert
                        resource = effectsObjects.get(0);
                    } else {
                        continue;
                    }
                } else {
                    resource = GameUtils.convertMarbleToResource(marble);
                }
                convertedMarbles.put(key, GameUtils.incrementValueInResourceMap(
                        convertedMarbles.get(key), resource, 1));
            }
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
        if (resourcesToDrop == null) throw new InvalidParameterException("array is null");
        System.out.println(obtainedResources);
        System.out.println(resourcesToDrop);
        int totalDropped = 0;
        for (Resource r : resourcesToDrop.keySet()) {
            if (!obtainedResources.containsKey(r) ||
                    obtainedResources.get(r) < resourcesToDrop.get(r)) {
                throw new InvalidParameterException("resources not in original map");
            }
            totalDropped += resourcesToDrop.get(r);
        }
        Player player = game.getPlayerByUsername(playerUsername);
        Map<Resource, Integer> resourcesToAdd = new HashMap<>(obtainedResources);
        for (Resource r : resourcesToDrop.keySet()) {
            resourcesToAdd = GameUtils.incrementValueInResourceMap(resourcesToAdd, r, -resourcesToDrop.get(r));
        }
        if (!player.getPlayerBoard().getWarehouse().canPlaceResources(resourcesToAdd))
            throw new InvalidParameterException("not enough");

        player.getPlayerBoard().getWarehouse().placeResources(resourcesToAdd);

        List<Player> other = new ArrayList<>(game.getPlayers());
        other.remove(player);
        movePlayers(other, totalDropped);

    }

    public void movePlayers(List<Player> players, int positions){
        if(!players.isEmpty()){
            for (int i = 0; i <positions; i++) {
                // for each dropped resource, move all the other players by one
                for (Player otherPlayer : players) {
                    otherPlayer.getFaithTrack().move();

                }
                // and check if the pope favor gets activated
                for (Player p : game.getPlayers()) {
                    activatePopeReport();
                }
            }
        }

    }

    public void dropLeader(int card) {
        movePlayer(game.getCurrentPlayer().getUsername(), 1);
        game.getCurrentPlayer().dropLeaderCard(card);
    }

    public boolean switchShelves(String shelf1, String shelf2) {
        return game.getCurrentPlayer().getPlayerBoard().getWarehouse().switchShelves(shelf1, shelf2);
    }

    public List<String> computeNextPossibleMoves(boolean alreadyPerformedMove) {
        System.out.println("computing");
        List<String> moves = new ArrayList<>();
        Player player = game.getCurrentPlayer();

        if(!alreadyPerformedMove){
            moves.add("GET_MARBLES");
            if (player.canActivateBasicProduction() || player.canActivateProduction()) {
                moves.add("ACTIVATE_PRODUCTION");
            }

            for (MarketCardStack cardsStack : game.getMarket().getCardsGrid()) {
                if(!cardsStack.isEmpty()) {
                    DevelopmentCard topCard = cardsStack.peek();
                    if (player.canBuyAndPlaceDevelopmentCard(topCard)) {
                        GameUtils.debug(topCard.toString());
                        moves.add("BUY_CARD");
                        break;
                    }
                }
            }
        } else {
            moves.add("END_TURN");
        }

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

        System.out.println("computed");
        return moves;
    }

    public List<DevelopmentCard> getBuyableDevelopementCards(){
        List<DevelopmentCard> developmentCards = new ArrayList<>();

        for (MarketCardStack cardsStack : game.getMarket().getCardsGrid()) {
            if(cardsStack.isEmpty())continue;
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

    public void activateProduction(List<Integer> selectedStacks, ProductionPower basicProduction, List<Integer> extraProductionPowers, List<Resource> extraOutput){
        if (selectedStacks != null) {
            List<ProductionPower> powers = game.getCurrentPlayer().possibleProductionPowersToActive();
            GameUtils.debug("powers: " + powers);

            for (Integer index: selectedStacks){
                ProductionPower productionPower = powers.get(index);
                if(productionPower.getOutput().containsKey(Resource.FAITH)){
                    for (int j = 0; j < productionPower.getOutput().get(Resource.FAITH); j++) {
                        game.getCurrentPlayer().getFaithTrack().move();
                        activatePopeReport();
                    }
                }
                game.getCurrentPlayer().getPlayerBoard().activateProductionPower(productionPower);
            }
        }
        if (basicProduction != null) {
           game.getCurrentPlayer().getPlayerBoard().activateProductionPower(basicProduction);
        }
        if(extraProductionPowers != null){
            int outRes = 0;
            for (Integer extraProductionPower : extraProductionPowers) {
                System.out.println(extraProductionPowers);
                ProductionPower productionPower = game.getCurrentPlayer().getPlayerBoard().getExtraProductionPowers().get((Integer) extraProductionPower);
                GameUtils.debug("extra " + productionPower.toString());
                productionPower.getOutput().put(extraOutput.get(outRes), 1);
                productionPower.getOutput().remove(Resource.ANY);
                game.getCurrentPlayer().getPlayerBoard().activateProductionPower(productionPower);
                productionPower.getOutput().remove(extraOutput.get(outRes),1);
                productionPower.getOutput().put(Resource.ANY, 1);
                outRes++;
            }
        }
    }

    public void movePlayer(String playerName, int positions){
        Player playerToMove = game.getPlayerByUsername(playerName);
        for (int i = 0; i < positions; i++) {
            playerToMove.getFaithTrack().move();
            activatePopeReport();
        }
    }

    public void activatePopeReport() {
        GameUtils.debug("reporting to the Pope");
        for (Player player : game.getPlayers()) {
            GameUtils.debug(String.valueOf(player.getFaithTrack().inOnPopeSpace()));
            int popeLevel = player.getFaithTrack().inOnPopeSpace();
            // for each player, if the player is on a pope space
            // check if the other players are in that space's area
            if (popeLevel != -1) {
                player.getFaithTrack().getPopesFavorTiles().get(popeLevel-1).setState(PopesFavorTileState.ACTIVE);
                for (Player otherPlayer : game.getPlayers()) {
                    // not current player
                    if (!otherPlayer.getUsername().equals(player.getUsername())) {
                        PopesFavorTile otherPlayersTile = otherPlayer.getFaithTrack().getPopesFavorTiles().get(popeLevel-1);
                        // if the other player's tile is inactive
                        if (otherPlayersTile.getState() == PopesFavorTileState.INACTIVE) {
                            // if player is in pope favor
                            if (otherPlayer.getFaithTrack().isInPopeFavorByLevel(popeLevel)) {
                                otherPlayersTile.setState(PopesFavorTileState.ACTIVE);
                            } else { // if player is not in pope favor
                                otherPlayersTile.setState(PopesFavorTileState.DISCARDED);
                            }
                        }
                    }
                }
            }
        }
    }

    public void buyDevelopmentCard(Integer stackIndex, DevelopmentCard developmentCard){
        game.getCurrentPlayer().getPlayerBoard().getCardStacks().get(stackIndex).add(developmentCard);
        game.getCurrentPlayer().getPlayerBoard().payResourceCost(game.getCurrentPlayer().computeDiscountedCost(developmentCard));
        for (MarketCardStack stack:
             game.getMarket().getCardsGrid()) {
            if(!stack.isEmpty() && stack.peek().equals(developmentCard)){
                stack.pop();
                break;
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
        List<Player> standingList = new ArrayList<>(game.getPlayers());

        /*
        standingList.add(game.getPlayers().get(0));
        for (Player player:
                game.getPlayers()) {
            if(player.getVP() >= standingList.get(0).getVP())standingList.add(0,player);

        }

         */

        standingList.sort(Comparator.comparingInt(Player::getVP));

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

    public List<Integer> getStacksToPlaceCard(Player player, DevelopmentCard developmentCard){
        List<Integer> stacks = new ArrayList<>();
        List<PlayerCardStack> allStacks = player.getPlayerBoard().getCardStacks();
        //System.out.println(allStacks);
        for (int i = 0; i < allStacks.size(); i++) {
            PlayerCardStack cardStack = allStacks.get(i);
            System.out.println("card stack size" + cardStack.size());
            System.out.println("can place" + cardStack.canPlaceCard(developmentCard));
            if((developmentCard.getLevel()==0 && cardStack.size()== 0) || cardStack.canPlaceCard(developmentCard)){
             //   stacks.add(allStacks.indexOf(cardStack));
                stacks.add(i);
            }
        }

        System.out.println("stack indexes: " + stacks);
        return stacks;
    }
}

