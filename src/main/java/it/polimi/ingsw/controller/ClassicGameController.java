package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarketCardStack;
import it.polimi.ingsw.model.player.FaithTrack;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerCardStack;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.shared.*;
import it.polimi.ingsw.server.Room;
import it.polimi.ingsw.utils.GameUtils;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * ClassicGameController is the controller for multi-player games
 */
public class ClassicGameController {
    private final Game game;


    public ClassicGameController(Room room) {
        this.game = room.getGame();
    }

    public ClassicGameController(Game game) { this.game = game; }

    /**
     * Starts the game
     */
    public void startGame(){
        selectStartingPlayer();
    }

    /**
     * Sets a random player as the first to play
     */
    private void selectStartingPlayer() {
        Random r = new Random();
        int firstPlayerIndex = r.nextInt(game.getActivePlayers().size());
        game.setCurrentPlayer(firstPlayerIndex);
        game.setInkwellPlayer(firstPlayerIndex);
    }

    /**
     * @return the Game class which is the primary class of the model
     */
    public Game getGame() {
        return game;
    }

    /**
     * Removes the two leader cards the player wanted to drop
     * @param selection1 index of the first card to drop
     * @param selection2 index of the second card to drop
     * @param player the player that selected the two cards
     */
    public void dropInitialLeaderCards(int selection1, int selection2, String player){
        game.getPlayerByUsername(player).dropInitialLeaderCards(selection1, selection2);
    }

    /**
     * Gives the chosen initial resources to the players
     * @param resources resources chosen
     * @param username player that select the resources
     */
    public void giveInitialResources(List<Resource> resources, String username) {
        Map<Resource, Integer> resourceMap = new HashMap<>();
        resourceMap.put(resources.get(0), resources.size());
        game.getPlayerByUsername(username).getPlayerBoard().getWarehouse()
                .placeOnShelf(resources.size() == 1 ? "top" : "middle", resourceMap);
    }

    /**
     * @return true if the game is over, else false
     */
    public boolean isGameOver() {
        for (Player player:
                game.getActivePlayers()) {
            if ((player.getFaithTrack().getPosition() >= FaithTrack.getMaxposition())||
                        player.getPlayerBoard().getCardStacks().get(0).size()+player.getPlayerBoard().getCardStacks().get(1).size()+player.getPlayerBoard().getCardStacks().get(2).size() >= 7)return true;
        }
        return false;
    }

    /**
     * sets the next player as current player
     */
    public void computeNextPlayer(){
        int nextPlayer = (game.getActivePlayers().indexOf(game.getCurrentPlayer()) + 1) % game.getActivePlayers().size();
        game.setCurrentPlayer(nextPlayer);
        if (!game.getActivePlayers().get(nextPlayer).isActive()) computeNextPlayer();
    }

    /**
     * represents the action of getting the marbles from the marble market
     * @param rowOrColumn can be "ROW" if the player desires to shift a row or "COLUMN" if he wants a column
     * @param index the index of row/column the player desires to shift
     * @return the marbles of the selected row/column
     */
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
            if (marble == Marble.RED) {
                // move the player forward on the track
                game.getCurrentPlayer().getFaithTrack().move();
                // check if it landed on a Pope space
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

    /**
     * Drops the resources the player does not wish to retain moving other payers and add the others to player's warehouse
     * @param obtainedResources all the resources the player got shifting the marker
     * @param resourcesToDrop the resources the player does not wish to retain
     * @param playerUsername tha player tha selected the resources
     * @throws InvalidParameterException if there are incompatibilities between obtained resources and resource to drop
     */
    public void dropPlayerResources(Map<Resource, Integer> obtainedResources, Map<Resource,
            Integer> resourcesToDrop, String playerUsername) throws InvalidParameterException {
        if (resourcesToDrop == null) throw new InvalidParameterException("array is null");
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

        List<Player> other = new ArrayList<>(game.getActivePlayers());
        other.remove(player);
        movePlayers(other, totalDropped);
    }

    /**
     * Moves a list of players of given positions and activates pope report
     * @param players list of players to move
     * @param positions how many positions the players havwe to move
     */
    public void movePlayers(List<Player> players, int positions){
        if(!players.isEmpty()){
            for (int i = 0; i <positions; i++) {
                // for each dropped resource, move all the other players by one
                for (Player otherPlayer : players) {
                    otherPlayer.getFaithTrack().move();
                }
                // and check if the pope favor gets activated
                for (Player p : game.getActivePlayers()) {
                    activatePopeReport();
                }
            }
        }
    }

    /**
     * Represents the move of dropping a leader card
     * @param card the index of the card to drop
     */
    public void dropLeader(int card) {
        movePlayer(game.getCurrentPlayer().getUsername(), 1);
        game.getCurrentPlayer().dropLeaderCard(card);
    }

    /**
     * Represents the move of switching to warehouse shelves
     * @param shelf1 first shelf
     * @param shelf2 second shelf
     * @throws InvalidParameterException if the two shelves can not be switched
     */
    public void switchShelves(String shelf1, String shelf2) throws InvalidParameterException {
        if (game.getCurrentPlayer().getPlayerBoard().getWarehouse().canSwitchShelves(shelf1, shelf2)) {
            game.getCurrentPlayer().getPlayerBoard().getWarehouse().switchShelves(shelf1, shelf2);
        } else {
            throw new InvalidParameterException();
        }
    }

    /**
     * Computes player's next possible moves
     * @param alreadyPerformedMove true if a player has already performed one of the unique moves
     * @return the list of the possible moves
     */
    public List<String> computeNextPossibleMoves(boolean alreadyPerformedMove) {
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
        if( warehouse.canSwitchShelves("top",    "middle") ||
            warehouse.canSwitchShelves("top",    "bottom") ||
            warehouse.canSwitchShelves("top",    "extra1") ||
            warehouse.canSwitchShelves("top",    "extra2") ||
            warehouse.canSwitchShelves("middle", "bottom") ||
            warehouse.canSwitchShelves("middle", "extra1") ||
            warehouse.canSwitchShelves("middle", "extra2") ||
            warehouse.canSwitchShelves("bottom", "extra1") ||
            warehouse.canSwitchShelves("bottom", "extra2")
        ) moves.add("SWITCH_SHELVES");

        return moves;
    }

    /**
     * @return the development cards the player can buy
     */
    public List<DevelopmentCard> getBuyableDevelopmentCards(){
        List<DevelopmentCard> developmentCards = new ArrayList<>();

        for (MarketCardStack cardsStack : game.getMarket().getCardsGrid()) {
            if(cardsStack.isEmpty()) continue;
            DevelopmentCard topCard = cardsStack.peek();
            if (game.getCurrentPlayer().canBuyAndPlaceDevelopmentCard(topCard)) {
                developmentCards.add(topCard);
            }
        }
        return developmentCards;
    }

    /**
     * @return the leader cards the player can play
     */
    public List<LeaderCard> getPlayableLeaderCards(){
        List<LeaderCard> leaderCards = new ArrayList<>();

        for (int i = 0; i < game.getCurrentPlayer().getLeaderCards().size(); i++) {
            if (game.getCurrentPlayer().canPlayLeader(i)) {
                leaderCards.add(game.getCurrentPlayer().getLeaderCards().get(i));
            }
        }
        return leaderCards;
    }

    /**
     * Represents the activate production move
     * @param selectedStacks the list of the indexes of the selected stacks out of the ones that can be activated
     * @param basicProduction the basic production power the player wants to use
     * @param extraProductionPowers the list of the indexes of the selected extra stacks out of the ones that can be activated
     * @param extraOutput the resources the player wants as outputs of extra powers
     */
    public void activateProduction(List<Integer> selectedStacks, ProductionPower basicProduction, List<Integer> extraProductionPowers, List<Resource> extraOutput){
        if (selectedStacks != null) {
            List<ProductionPower> powers = game.getCurrentPlayer().possibleProductionPowersToActive();

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
        if(extraProductionPowers != null && !extraProductionPowers.isEmpty()){
            int outRes = 0;
            for (Integer extraProductionPower : extraProductionPowers) {
                movePlayer(game.getCurrentPlayer().getUsername(), 1);
                ProductionPower productionPower = game.getCurrentPlayer().getPlayerBoard().getExtraProductionPowers().get((Integer) extraProductionPower);
                productionPower.getOutput().put(extraOutput.get(outRes), 1);
                productionPower.getOutput().remove(Resource.ANY);
                productionPower.getOutput().remove(Resource.FAITH);
                game.getCurrentPlayer().getPlayerBoard().activateProductionPower(productionPower);
                productionPower.getOutput().remove(extraOutput.get(outRes),1);
                productionPower.getOutput().put(Resource.ANY, 1);
                productionPower.getOutput().put(Resource.FAITH, 1);
                outRes++;
            }
        }
    }

    /**
     * Moves a player of given positions and activate Pope report
     * @param playerName  the player to move
     * @param positions  the amount of positions to move the player
     */
    public void movePlayer(String playerName, int positions){
        Player playerToMove = game.getPlayerByUsername(playerName);
        for (int i = 0; i < positions; i++) {
            playerToMove.getFaithTrack().move();
            activatePopeReport();
        }
    }

    /**
     * Checks if a player is on a pope space, if yes sets all other player's Pope favor tiles to active or discarded
     */
    public void activatePopeReport() {
        for (Player player : game.getActivePlayers()) {
            int popeLevel = player.getFaithTrack().inOnPopeSpace();
            // for each player, if the player is on a pope space
            // check if the other players are in that space's area
            if (popeLevel != -1) {
                PopesFavorTile currentTile = player.getFaithTrack().getPopesFavorTiles().get(popeLevel-1);
                if (currentTile.getState() == PopesFavorTileState.INACTIVE) {
                    currentTile.setState(PopesFavorTileState.ACTIVE);
                    List<Player> allPlayers = new ArrayList<>();
                    allPlayers.addAll(game.getInactivePlayers());
                    allPlayers.addAll(game.getActivePlayers());
                    for (Player otherPlayer : allPlayers) {
                        // not current player
                        if (!otherPlayer.getUsername().equals(player.getUsername())) {
                            PopesFavorTile otherPlayersTile = otherPlayer.getFaithTrack().getPopesFavorTiles().get(popeLevel - 1);
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
    }

    /**
     * Represents the action of buying a development card
     * @param stackIndex index of the stack where the card has to placed
     * @param developmentCard the just bought card
     */
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

    /**
     * Represents the action of playing a leader
     * @param cardIndex the index of the leader card to play
     */
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

    /**
     * Computes the final standing
     * @return the standing sorted by VP
     */
    public Map<String, Integer> computeStanding() {
        Map<String, Integer> standing = new LinkedHashMap<>();
        List<Player> standingList = new ArrayList<>(game.getActivePlayers());
        standingList.sort((o1, o2) -> o2.getVP() - o1.getVP());

        int points;
        for (Player player:
                standingList) {
             points = player.getVP();
             standing.put(player.getUsername(), points);
        }
        return standing;
    }

    /**
     * Computes the winner
     * @return the player with the highest VP
     */
    public String computeWinner() {
        String winner = game.getActivePlayers().get(0).getUsername();
        int points =  game.getActivePlayers().get(0).getVP();

        for (Player player:
                game.getActivePlayers()) {
            if(player.getVP() >= points){
                winner=player.getUsername();
                points = player.getVP();
            }
        }
        return winner;
    }

    /**
     * Given a development card it computes the stacks where the player can place the card
     * @param player the player that bought the card
     * @param developmentCard the bought development card
     * @return the list of the indexes  of  the stacks where the card can be placed
     */
    public List<Integer> getStacksToPlaceCard(Player player, DevelopmentCard developmentCard){
        List<Integer> stacks = new ArrayList<>();
        List<PlayerCardStack> allStacks = player.getPlayerBoard().getCardStacks();
        for (int i = 0; i < allStacks.size(); i++) {
            PlayerCardStack cardStack = allStacks.get(i);
            if((developmentCard.getLevel()==0 && cardStack.size()== 0) || cardStack.canPlaceCard(developmentCard)){
                stacks.add(i);
            }
        }
        return stacks;
    }

    /**
     * Cheat to put 100 resources of each type in every player strongbox
     */
    public void giveExtraResources() {
        Map<Resource, Integer> extraResources = new HashMap<>();
        extraResources.put(Resource.COIN, 100);
        extraResources.put(Resource.STONE, 100);
        extraResources.put(Resource.SERVANT, 100);
        extraResources.put(Resource.SHIELD, 100);
        for (Player player : game.getActivePlayers()) {
            player.getPlayerBoard().getStrongbox().addResources(extraResources);
        }
    }
}

