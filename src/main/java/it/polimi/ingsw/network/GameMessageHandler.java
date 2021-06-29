package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.ClassicGameController;
import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.network.game.*;
import it.polimi.ingsw.server.Room;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.utils.GameUtils;


import java.security.InvalidParameterException;
import java.util.*;

/**
 * This class handles the messages coming from the client by calling controller methods and
 * computes the next message to send to the client
 */
public class GameMessageHandler {

    private final ClassicGameController gameController;
    private ClientConnection clientConnection;
    private final Room room;
    private boolean ready;

    public GameMessageHandler(ClassicGameController gameController, ClientConnection clientConnection, Room room) {
        this.gameController = gameController;
        this.clientConnection = clientConnection;
        this.room = room;
        ready = false;

    }

    /**
     * Sends the messages to select the initial resources or the initial leaders
     */
    public void initialSelections(){
        clientConnection.sendMessage(new UpdateGameStateMessage(gameController.getGame()));
        // calculate the player's position in the playing order
        int playingIndex = room.getConnections().indexOf(clientConnection) - room.getGame().getInkwellPlayer();
        if (playingIndex < 0) {
            playingIndex += room.getNumberOfPlayers();
        }

        if (playingIndex == 2 || playingIndex == 3) {
            // give 1 faith point if the player is the third or fourth to play
            gameController.movePlayer(room.getPlayerFromConnection(clientConnection).getUsername(), 1);
        }

        List<Resource> resourceOptions = new ArrayList<>(Arrays.asList(
                Resource.COIN, Resource.STONE, Resource.SERVANT, Resource.SHIELD));

        if (playingIndex == 0) {
            clientConnection.sendMessage(
                    new DropInitialLeaderCardsRequestMessage(
                            room.getPlayerFromConnection(clientConnection).getLeaderCards())
            );
        } else {
            // ask players to choose the type of resources they want to receive
            int resourceAmount = (playingIndex == 1 || playingIndex == 2) ? 1 : 2;
            Message message = new SelectInitialResourceRequestMessage(
                    resourceOptions, resourceAmount);
            clientConnection.sendMessage(message);
        }
    }

    /**
     * When everyone is ready sends the moves to the first player
     */
    private void startPlayingIfReady() {
        for (Player p : room.getGame().getActivePlayers()) {
            if (p.getLeaderCards().size() != 2) {
                clientConnection.sendMessage(new StringMessage("Waiting for other players to select their cards"));
                return;
            }

        }

        sendStateAndMovesForNextTurn();
    }

    /**
     * After the player have got some new resources from the market this methods send a message asking which resources
     * he wants to drop
     */
    private void askToDropResources() {
        System.out.println("merge resources");
        Map<Resource, Integer> allResources = new HashMap<>(room.getCurrentTurn().getConverted());
        System.out.println("ask to drop");
        System.out.println(allResources);
        clientConnection.sendMessage(new DropResourceRequestMessage(allResources));
    }

    /**
     * Receives the message containing the resources the player selected and tells the controller
     * to add these resources to the player warehouse
     * @param message containing the selected initial resources
     */
    public void handle(SelectInitialResourceResponseMessage message) {
        gameController.giveInitialResources(message.getSelectedResources(),
                room.getPlayerFromConnection(clientConnection).getUsername());
        clientConnection.sendMessage(new DropInitialLeaderCardsRequestMessage(
                room.getPlayerFromConnection(clientConnection).getLeaderCards()));
        GameUtils.writeGame(gameController.getGame(), room.getId());
    }

    /**
     * Receives the message containing the leader cards the player selected to drop and tells the controller
     * to remove these resources to the player leader cards
     * @param message containing the leader cards the player is dropping
     */
    public void handle(DropInitialLeaderCardsResponseMessage message){
        gameController.dropInitialLeaderCards(message.getCard1(), message.getCard2(), room.getPlayerFromConnection(clientConnection).getUsername());
        GameUtils.writeGame(gameController.getGame(), room.getId());
        ready = true;
        startPlayingIfReady();
    }

    /**
     * Receives the move the player wants to perform and after checking if is valid sends back the
     * right message in order to perform the move
     * @param message the type of move the player wants to perform
     */
    public void handle(SelectMoveResponseMessage message){
        System.out.println("received");
        System.out.println(room.getCurrentTurn().getMoves());
        System.out.println(room.getPlayerFromConnection(clientConnection).getUsername());
        if(room.getCurrentTurn().isValidMove(message.getMove(), room.getPlayerFromConnection(clientConnection).getUsername())){
            System.out.println("sending");
            switch (message.getMove()){
                case("GET_MARBLES"):
                    clientConnection.sendMessage(new SelectMarblesRequestMessage(room.getGame().getMarket().getMarbleStructure()));
                    break;
                case("DROP_LEADER"):
                    clientConnection.sendMessage(new DropLeaderCardRequestMessage(room.getPlayerFromConnection(clientConnection).getLeaderCards()));
                    break;
                case ("PLAY_LEADER"):
                    clientConnection.sendMessage(new PlayLeaderRequestMessage(gameController.getPlayableLeaderCards()));
                    break;
                case("SWITCH_SHELVES"):
                    clientConnection.sendMessage(new SwitchShelvesRequestMessage(room.getPlayerFromConnection(clientConnection).getPlayerBoard().getWarehouse().getShelfNames()));
                    break;
                case("ACTIVATE_PRODUCTION"):
                    clientConnection.sendMessage(new ActivateProductionRequestMessage(room.getPlayerFromConnection(clientConnection).possibleProductionPowersToActive()));
                    break;
                case ("BUY_CARD"):
                    clientConnection.sendMessage(new BuyDevelopmentCardRequestMessage(gameController.getBuyableDevelopmentCards()));
                    break;
                case("END_TURN"):
                    endTurn();
                    break;
            }

        } else {
            clientConnection.sendMessage(new ErrorMessage("Invalid Move\nRetry"));
            clientConnection.sendMessage(new SelectMoveRequestMessage(room.getCurrentTurn().getMoves()));
        }
    }

    /**
     * This method receive the row or column that the user wants to shift and calls the method of the controller to perform it.
     * Then if the player has two leaders with marbles effect it ask the resource type else asks if he wants to drop any resource.
     * @param message the column of row the player wants to shift.
     */
    public void handle(SelectMarblesResponseMessage message){
        System.out.println("received get marbles response");
        Map<String, Map<Resource, Integer>> resources = gameController.getMarbles(message.getRowOrColumn(), message.getIndex());
        System.out.println("retrieved resources");
        System.out.println(resources);

        room.getCurrentTurn().setConverted(resources.get("converted"));
        room.getCurrentTurn().setToConvert(resources.get("toConvert").get(Resource.ANY));
        room.getCurrentTurn().setConversionOptions(new ArrayList<>(resources.get("conversionOptions").keySet()));
        System.out.println("turn set");

        if (resources.get("toConvert").containsKey(Resource.ANY) &&
                resources.get("toConvert").get(Resource.ANY) > 0) {
            System.out.println("ask for conversion help");
            int amountToConvert = resources.get("toConvert").get(Resource.ANY);
            List<Resource> options = new ArrayList<>(resources.get("conversionOptions").keySet());
            clientConnection.sendMessage(
                    new SelectResourceForWhiteMarbleRequestMessage(amountToConvert, options));
            return;
        }
        if (resources.get("converted").keySet().size() == 0) {
            room.getCurrentTurn().setAlreadyPerformedMove(true);
            sendNextMoves();
        } else
            askToDropResources();
    }

    /**
     * This method receives a leader card the player wants to drop and calls the corresponding controller method.
     * @param message that contains the index of the card the player wants to drop.
     */
    public void handle(DropLeaderCardResponseMessage message){
        gameController.dropLeader(message.getCard());
        clientConnection.sendMessage(new StringMessage("Your have " +room.getPlayerFromConnection(clientConnection).getFaithTrack().getPosition() +" faith points"));
        sendNextMoves();
    }

    /**
     * This method receives a leader card the player wants to play and calls the corresponding controller method.
     * @param message that contains the index of the card the player wants to play.
     */
    public void handle(PlayLeaderResponseMessage message){
        gameController.playLeader(message.getCardIndex());
        sendNextMoves();
    }

    /**
     * This method receive the name of two shelves the player wants to switch.
     * @param message is a message with 2 String one containing the name of the first shelf and one the other.
     */
    public void handle(SwitchShelvesResponseMessage message) {
        try {
            gameController.switchShelves(message.getShelf1(), message.getShelf2());
            sendNextMoves();
        } catch (InvalidParameterException e) {
            clientConnection.sendMessage(new ErrorMessage("You cannot switch these two shelves"));
            clientConnection.sendMessage((new SelectMoveRequestMessage(room.getCurrentTurn().getMoves())));
        }
    }

    /**
     * This method is called if a player has 2 leader with Marble scope get marbles from the market.
     * @param message with a map containing the resources the player bought.
     */
    public void handle(SelectResourceForWhiteMarbleResponseMessage message) {
        Map<Resource, Integer> resourcesConverted=message.getResources();
        int amountConverted = 0;
        for (Map.Entry<Resource, Integer> entry : resourcesConverted.entrySet()) {
            amountConverted += entry.getValue();
        }
        Turn currentTurn = room.getCurrentTurn();
        List<Resource> conversionOptions= currentTurn.getConversionOptions();
        if(!resourcesConverted.keySet().containsAll(conversionOptions)
                || amountConverted != currentTurn.getToConvert()){
            clientConnection.sendMessage(new ErrorMessage("Invalid conversion, try again!"));
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
     * This method receives the card of which the player wants to activate production.
     * @param message  with the basic production power, the production powers activated and extra production activated
     *                 with their output resources.
     */
    public void handle(ActivateProductionResponseMessage message){
        if(message.getSelectedStacks()!=null || message.getBasicProduction() != null || message.getExtraProductionPowers() != null) {
            gameController.activateProduction(message.getSelectedStacks(), message.getBasicProduction(), message.getExtraProductionPowers(), message.getExtraOutput());
            clientConnection.sendMessage(new StringMessage("Your update strongbox: " + room.getGame().getCurrentPlayer().getPlayerBoard().getStrongbox()));
            room.getCurrentTurn().setAlreadyPerformedMove(true);
            sendNextMoves();
        }
        else {
            clientConnection.sendMessage(new ErrorMessage("Nothing could be done"));
            sendNextMoves();
        }
    }

    /**
     * This method is called when a player wants do drop resources he just got.
     * @param message with the resources the player wants to drop.
     */
    public void handle(DropResourcesResponseMessage message){
        Map<Resource, Integer> resourcesConverted = room.getCurrentTurn().getConverted();
        System.out.println(resourcesConverted);
        // check if the selected resources are valid
        try {
            gameController.dropPlayerResources(resourcesConverted, message.getResourcesToDrop(),
                    room.getPlayerFromConnection(clientConnection).getUsername());
        } catch (InvalidParameterException e) {
            System.out.println(e.getMessage());
            clientConnection.sendMessage(new ErrorMessage("The selected resources are invalid"));
            return;
        }
        room.getCurrentTurn().setAlreadyPerformedMove(true);
        sendNextMoves();
    }

    /**
     * This method is called when a player buy a card.
     * @param message with the card the player is buying.
     */
    public void handle(BuyDevelopmentCardResponseMessage message){
        DevelopmentCard developmentCard = gameController.getBuyableDevelopmentCards().get(message.getSelectedCardIndex());
        List<Integer> stacks = gameController.getStacksToPlaceCard(room.getGame().getCurrentPlayer(), developmentCard);
        room.getCurrentTurn().setBoughtDevelopmentCard(developmentCard);
        if(stacks.size()==1 || gameController.getGame().getCurrentPlayer().getPlayerBoard().getCardStacks().get(stacks.get(0)).isEmpty()){
            gameController.buyDevelopmentCard(stacks.get(0),developmentCard);
            room.getCurrentTurn().setAlreadyPerformedMove(true);
            sendNextMoves();
        }else {
            clientConnection.sendMessage(new SelectStackToPlaceCardRequestMessage(stacks));
        }

    }

    /**
     * This method is called if the player has to choose on which stack he wants to place a card.
     * @param message with the index of the stack on which the player is placying the card.
     */
    public void handle(SelectStackToPlaceCardResponseMessage message){
        if(room.getGame().getCurrentPlayer().getPlayerBoard().getCardStacks().get(message.getSelectedStackIndex()).canPlaceCard(room.getCurrentTurn().getBoughtDevelopmentCard())){
           gameController.buyDevelopmentCard(message.getSelectedStackIndex(), room.getCurrentTurn().getBoughtDevelopmentCard());
            room.getCurrentTurn().setAlreadyPerformedMove(true);
            sendNextMoves();
        }else {
            clientConnection.sendMessage(new ErrorMessage("Action Could not be completed"));
            sendNextMoves();
        }

    }

    /**
     * This method is used to give extra resources to all players when debugging
     * @param message signaling the extra resources
     */
    public void handle(GetResourcesCheatMessage message) {
        gameController.giveExtraResources();
        room.sendAll(new UpdateAndDisplayGameStateMessage(room.getGame()));
        for (ClientConnection connection : room.getConnections()) {
            Player p = room.getPlayerFromConnection(connection);
            if (p.getUsername().equals(room.getCurrentTurn().getCurrentPlayer())) {
                connection.sendMessage(new SelectMoveRequestMessage(room.getCurrentTurn().getMoves()));
            }
        }
    }

    /**
     * This method is called when a player ended his turn, if the game is over sends the standing otherwise sends the
     * moves to the next player.
     */
    private void sendStateAndMovesForNextTurn(){
        ClientConnection currentPlayer = room.getConnections().get(room.getGame().getActivePlayers().indexOf(
                room.getGame().getCurrentPlayer()));
        GameUtils.debug(room.getPlayerFromConnection(currentPlayer).getUsername());
        GameUtils.writeGame(gameController.getGame(), room.getId());
        room.sendAll(new UpdateAndDisplayGameStateMessage(room.getGame()));

        if(!gameController.isGameOver() || (room.getGame().getActivePlayers().indexOf(room.getPlayerFromConnection(currentPlayer)) != room.getGame().getInkwellPlayer())){
            room.setCurrentTurn(new Turn(room.getPlayerFromConnection(currentPlayer).getUsername(), gameController.computeNextPossibleMoves(false)));
            SelectMoveRequestMessage selectMoveRequestMessage = new SelectMoveRequestMessage(room.getCurrentTurn().getMoves());
            currentPlayer.sendMessage(selectMoveRequestMessage);
        } else {
            Map<String, Integer> standing = gameController.computeStanding();
            String winner = gameController.computeWinner();
            room.sendAll(new GameOverMessage(winner, standing));
            GameUtils.deleteSavedGame(room.getId());
        }
    }

    /**
     * After a player performed a move this sends the next moves he can do.
     */
    private void sendNextMoves(){
        room.getCurrentTurn().setMoves(gameController.computeNextPossibleMoves(room.getCurrentTurn().hasAlreadyPerformedMove()));
        clientConnection.sendMessage(new UpdateAndDisplayGameStateMessage(room.getGame()));
        clientConnection.sendMessage(new SelectMoveRequestMessage(room.getCurrentTurn().getMoves()));
        GameUtils.writeGame(gameController.getGame(), room.getId());
    }

    /**
     * Computes the next player and calls the method to send the moves to the next player.
     */
    private void endTurn(){
        gameController.computeNextPlayer();
        sendStateAndMovesForNextTurn();
        GameUtils.debug(gameController.getGame().getCurrentPlayer().getUsername());
    }

    /**
     * This methods deactivates a connection.
     * @param connection the connection to deactivate.
     */
    public void deactivateConnection(ClientConnection connection) {
        connection.setConnected(false);
        Player disconnectedPlayer = room.getPlayerFromConnection(connection);
        if(!ready){
            disconnectedPlayer.setActive(false);
            room.sendAll(new StringMessage(disconnectedPlayer.getUsername() + " disconnected"));
            return;
        }
        room.sendAll(new ErrorMessage(disconnectedPlayer.getUsername() + " disconnected"));

        if(room.getCurrentTurn()!= null && disconnectedPlayer.equals(room.getGame().getPlayerByUsername(room.getCurrentTurn().getCurrentPlayer()))){
            GameUtils.debug("ending turn");
            endTurn();
        } else  room.sendAll(new UpdateGameStateMessage(room.getGame()));

        disconnectedPlayer.setActive(false);

        System.out.println("players" + gameController.getGame().getActivePlayers() + "conncetions" + room.getConnections().size());

    }

    /**
     * Sets the clientConnection
     * @param clientConnection to set
     */
    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    /**
     * Checks if the player is ready to start the game.
     * @return True if the player choose the initial leaders  and resources
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Sets ready to true if the player choose the initial leaders  and resources
     * @param ready true if the player choose the initial leaders  and resources
     */
    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
