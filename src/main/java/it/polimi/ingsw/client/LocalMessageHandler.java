package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.ClassicGameController;
import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.game.*;
import it.polimi.ingsw.utils.GameUtils;
import it.polimi.ingsw.view.UI;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LocalMessageHandler class, it receives messages from localClient and calls the controller methods
 */
public class LocalMessageHandler {
    private final UI ui;
    private final ClassicGameController gameController;
    private Player player;
    private Turn currentTurn;

    /**
     * Local MessageHandler constructor
     */
    public LocalMessageHandler(UI ui, ClassicGameController gameController) {
        this.ui = ui;
        this.gameController = gameController;
    }

    /**
     * Sets the player in  the controller and the game state in the ui and ask the user to select the initial leaders.
     */
    public void startPlaying(){
        gameController.getGame().addPlayer(new Player(ui.getUsername()).getUsername());
        gameController.getGame().setCurrentPlayer(0);
        player=gameController.getGame().getCurrentPlayer();
        ui.setGameState(gameController.getGame());
        ui.selectLeaderCards(player.getLeaderCards());
    }

    /**
     * Receives the message containing the leader cards the player selected to drop and tells the controller
     * to remove these resources to the player leader cards
     * @param message containing the leader cards the player is dropping
     */
    public void handle(DropInitialLeaderCardsResponseMessage message){
        gameController.dropInitialLeaderCards(message.getCard1(), message.getCard2(), player.getUsername());
        startTurn();
    }

    /**
     * Computes player's moves and sends them to the ui, it also saves the game.
     */
    public void startTurn(){
        currentTurn = new Turn(player.getUsername(), gameController.computeNextPossibleMoves(false));
        GameUtils.writeGame(gameController.getGame(), 42);
        ui.setGameState(GameUtils.readGame(42));
        ui.displayGameState();
        ui.displayPossibleMoves(currentTurn.getMoves());
    }

    /**
     * Computes the next moves and shows them in the ui.
     * @param alreadyPerformedMoves in order to compute the next moves it needs to know if one of the unique moves
     *                              has already been performed.
     */
    private void nextMoves(boolean alreadyPerformedMoves){
        if(!checkGameOver()){
            currentTurn.setAlreadyPerformedMove(alreadyPerformedMoves);
            currentTurn.setMoves(gameController.computeNextPossibleMoves(currentTurn.hasAlreadyPerformedMove()));
            GameUtils.writeGame(gameController.getGame(), 42);
            ui.setGameState(GameUtils.readGame(42));
            ui.displayGameState();
            ui.displayPossibleMoves(currentTurn.getMoves());
        }
    }

    /**
     * Checks if the game is over, if true shows the winner in the ui and deletes the game.
     * @return true if the game is over.
     */
    private boolean checkGameOver(){
        if(gameController.isGameOver()){
            Map<String, Integer> standing = gameController.computeStanding();
            String winner = gameController.computeWinner();
            ui.gameOver(winner, standing);
            GameUtils.deleteSavedGame(42);
            return true;
        } else return false;
    }

    /**
     * Checks the move the user selected and calls the right method in the ui.
     * @param message the message containing the move the user selected.
     */
    public void handle(SelectMoveResponseMessage message){
        if(currentTurn.isValidMove(message.getMove(), player.getUsername())){
            switch (message.getMove()){
                case("GET_MARBLES"):
                    ui.selectMarbles(gameController.getGame().getMarket().getMarbleStructure());
                    break;
                case("DROP_LEADER"):
                    ui.discardLeaderCard(player.getLeaderCards());
                    break;
                case ("PLAY_LEADER"):
                    ui.playLeader(gameController.getPlayableLeaderCards());
                    break;
                case("SWITCH_SHELVES"):
                    ui.switchShelves(player.getPlayerBoard().getWarehouse().getShelfNames());
                    break;
                case("ACTIVATE_PRODUCTION"):
                    ui.activateProduction(player.possibleProductionPowersToActive());
                    break;
                case ("BUY_CARD"):
                    ui.buyDevelopmentCard(gameController.getBuyableDevelopmentCards());
                    break;
                case("END_TURN"):
                    endTurn();
                    break;
            }
        } else {
            ui.displayError("Invalid Move.");
            ui.displayPossibleMoves(currentTurn.getMoves());
        }
    }

    /**
     * End the turn, does the LorenzoIlMagnifico move and starts the next turn.
     */
    private void endTurn(){
        gameController.computeNextPlayer();
        startTurn();
    }

    /**
     * Calls the drop leader card  method of the controller.
     * @param message a message containing the leader card the user wants to drop.
     */
    public void handle(DropLeaderCardResponseMessage message){
        gameController.dropLeader(message.getCard());
        ui.displayString("Your have " +player.getFaithTrack().getPosition() +" faith points");
        nextMoves(currentTurn.hasAlreadyPerformedMove());
    }

    /**
     * Calls the play leader card  method of the controller.
     * @param message a message containing the leader card the user wants to play.
     */
    public void handle(PlayLeaderResponseMessage message){
        gameController.playLeader(message.getCardIndex());
        nextMoves(currentTurn.hasAlreadyPerformedMove());
    }

    /**
     * Calls the switch shelves  method in the controller.
     * @param message a message with the name of the shelves the user wants to switch.
     */
    public void handle(SwitchShelvesResponseMessage message){
        try {
            gameController.switchShelves(message.getShelf1(), message.getShelf2());
            nextMoves(currentTurn.hasAlreadyPerformedMove());
        } catch (InvalidParameterException e) {
            ui.displayError("You cannot switch these two shelves");
            ui.displayPossibleMoves(currentTurn.getMoves());
        }
    }

    /**
     * Calls the buy development card in the controller  and if the user has to choose  where to place it it asks it in
     * the ui, else calls the  place card  method  in the controller.
     * @param message containing the card that the user is buying.
     */
    public void handle(BuyDevelopmentCardResponseMessage message){
        DevelopmentCard developmentCard = gameController.getBuyableDevelopmentCards().get(message.getSelectedCardIndex());
        List<Integer> stacks = gameController.getStacksToPlaceCard(player, developmentCard);
        currentTurn.setBoughtDevelopmentCard(developmentCard);
        if(stacks.size()==1 || gameController.getGame().getCurrentPlayer().getPlayerBoard().getCardStacks().get(stacks.get(0)).isEmpty()){
            gameController.buyDevelopmentCard(stacks.get(0),developmentCard);
            currentTurn.setAlreadyPerformedMove(true);
            nextMoves(true);
        }else {
            ui.selectStackToPlaceCard(stacks);
        }
    }

    /**
     * Converts the white marbles in the  way the user wanted.
     * @param message a message that contains user converted resources.
     */
    public void handle(SelectResourceForWhiteMarbleResponseMessage message) {
        Map<Resource, Integer> resourcesConverted = message.getResources();
        int amountConverted = 0;
        for (Map.Entry<Resource, Integer> entry : resourcesConverted.entrySet()) {
            amountConverted += entry.getValue();
        };
        List<Resource> conversionOptions= currentTurn.getConversionOptions();
        if(!resourcesConverted.keySet().containsAll(conversionOptions)
                || amountConverted != currentTurn.getToConvert()){
            ui.displayError("Invalid conversion, try again!\n");
            return;
        }
        resourcesConverted = GameUtils.sumResourcesMaps(currentTurn.getConverted(), resourcesConverted);
        Map<Resource, Integer> converted = new HashMap<>(resourcesConverted);
        // remove empty keys
        for (Map.Entry<Resource, Integer> entry : resourcesConverted.entrySet()) {
            if (entry.getValue()==0) converted.remove(entry.getKey());
        }
        currentTurn.setConverted(converted);
        askToDropResources();
    }

    /**
     * Asks the user to drop the resources in the ui.
     */
    private void askToDropResources() {
        Map<Resource, Integer> allResources = new HashMap<>(currentTurn.getConverted());
        ui.dropResources(allResources);
    }

    /**
     * Calls the methods in the controller to sets the resources to the player.
     * @param message a message containing the resources the player just got.
     */
    public void handle(SelectMarblesResponseMessage message){
        Map<String, Map<Resource, Integer>> resources = gameController.getMarbles(message.getRowOrColumn(), message.getIndex());

        currentTurn.setConverted(resources.get("converted"));
        currentTurn.setToConvert(resources.get("toConvert").get(Resource.ANY));
        currentTurn.setConversionOptions(new ArrayList<>(resources.get("conversionOptions").keySet()));

        if (resources.get("toConvert").containsKey(Resource.ANY) &&
                resources.get("toConvert").get(Resource.ANY) > 0) {
            List<Resource> options = new ArrayList<>(resources.get("conversionOptions").keySet());
            ui.selectResourceForWhiteMarbles(resources.get("toConvert").get(Resource.ANY), options);
            return;
        }
        if (resources.get("converted").keySet().size() == 0) {
            nextMoves(true);
        } else
            askToDropResources();
    }

    /**
     * Calls the controller methods to put the card on the selected stack.
     * @param message a message containing the stacks on which the user wants to place the card.
     */
    public void handle(SelectStackToPlaceCardResponseMessage message){
        if(player.getPlayerBoard().getCardStacks().get(message.getSelectedStackIndex()).canPlaceCard(currentTurn.getBoughtDevelopmentCard())){
           gameController.buyDevelopmentCard(message.getSelectedStackIndex(), currentTurn.getBoughtDevelopmentCard());
            currentTurn.setAlreadyPerformedMove(true);
            nextMoves(true);
        }else {
            ui.displayError("Action Could not be completed");
            nextMoves(false);
        }
    }

    /**
     * Calls the controller methods to drop the resources and move LorenzoIlMagnifico.
     * @param message containing the resources the player wants to drop.
     */
    public void handle(DropResourcesResponseMessage message){
        Map<Resource, Integer> resourcesConverted =currentTurn.getConverted();
        // check if the selected resources are valid
        try {
            gameController.dropPlayerResources(resourcesConverted, message.getResourcesToDrop(),
                    player.getUsername());
            nextMoves(true);
        } catch (InvalidParameterException e) {
            ui.displayError("The selected resources are invalid");
            ui.dropResources(currentTurn.getConverted());
            //nextMoves(false);
        }

    }

    /**
     * Calls the activate production method of the controller.
     * @param message a message containing the index of the stacks on which the player is activating the production.
     */
    public void handle(ActivateProductionResponseMessage message){
        if(message.getSelectedStacks()!=null || message.getBasicProduction() != null || message.getExtraProductionPowers() != null) {
            gameController.activateProduction(message.getSelectedStacks(), message.getBasicProduction(), message.getExtraProductionPowers(), message.getExtraOutput());
            ui.displayString("Your update strongbox: " + gameController.getGame().getCurrentPlayer().getPlayerBoard().getStrongbox());
            nextMoves(true);
        }
        else {
            ui.displayError("Nothing could be done");
            nextMoves(false);
        }
    }

    /**
     * Assign 100 resources of each  type to the player.
     * @param message signaling the player wants to cheat.
     */
    public void handle(GetResourcesCheatMessage message) {
        gameController.giveExtraResources();
        nextMoves(currentTurn.hasAlreadyPerformedMove());
    }
}
