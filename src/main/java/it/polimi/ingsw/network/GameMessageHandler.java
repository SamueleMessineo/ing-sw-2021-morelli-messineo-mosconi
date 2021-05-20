package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.ClassicGameController;
import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerCardStack;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.network.game.*;
import it.polimi.ingsw.server.Room;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.utils.GameUtils;


import java.security.InvalidParameterException;
import java.util.*;

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

    public void initialSelections(){
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

    private void startPlayingIfReady() {
        for (Player p : room.getGame().getPlayers()) {
            if (p.getLeaderCards().size() != 2) return;
        }

        sendStateAndMovesForNextTurn();
    }

    private void askToDropResources() {
        System.out.println("merge resources");
        Map<Resource, Integer> allResources = new HashMap<>(room.getCurrentTurn().getConverted());
        System.out.println("ask to drop");
        System.out.println(allResources);
        clientConnection.sendMessage(new DropResourceRequestMessage(allResources));
    }

    public void handle(SelectInitialResourceResponseMessage message) {
        gameController.giveInitialResources(message.getSelectedResources(),
                room.getPlayerFromConnection(clientConnection).getUsername());
        clientConnection.sendMessage(new DropInitialLeaderCardsRequestMessage(
                room.getPlayerFromConnection(clientConnection).getLeaderCards()));
    }

    public void handle(DropInitialLeaderCardsResponseMessage message){
        gameController.dropInitialLeaderCards(message.getCard1(), message.getCard2(), room.getPlayerFromConnection(clientConnection).getUsername());
        GameUtils.saveGameState(room.getGame(), room.getId());
        clientConnection.sendMessage(new StringMessage("Waiting for other players to select their cards"));
        ready = true;
        startPlayingIfReady();
    }

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
                    clientConnection.sendMessage(new BuyDevelopmentCardRequestMessage(gameController.getBuyableDevelopementCards()));
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

    public void handle(DropLeaderCardResponseMessage message){
        gameController.dropLeader(message.getCard());
        clientConnection.sendMessage(new StringMessage("Your have " +room.getPlayerFromConnection(clientConnection).getFaithTrack().getPosition() +" faith points"));
        sendNextMoves();
    }

    public void handle(PlayLeaderResponseMessage message){
        gameController.playLeader(message.getCardIndex());
        sendNextMoves();
    }

    public void handle(SwitchShelvesResponseMessage message){
        if(gameController.switchShelves(message.getShelf1(), message.getShelf2())){
                    sendNextMoves();
        } else {
            clientConnection.sendMessage(new ErrorMessage("You cannot switch these two shelves"));
            clientConnection.sendMessage((new SelectMoveRequestMessage(room.getCurrentTurn().getMoves())));
        }
    }

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
        currentTurn.setConverted(resourcesConverted);
        askToDropResources();
    }

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

    public void handle(BuyDevelopmentCardResponseMessage message){
        DevelopmentCard developmentCard = gameController.getBuyableDevelopementCards().get(message.getSelectedCardIndex());
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

    public void handle(SelectStackToPlaceCardResponseMessage message){
        if(room.getGame().getCurrentPlayer().getPlayerBoard().getCardStacks().get(message.getSelectedStackIndex()).canPlaceCard(room.getCurrentTurn().getBoughtDevelopmentCard())){
           gameController.buyDevelopmentCard(message.getSelectedStackIndex(), room.getCurrentTurn().getBoughtDevelopmentCard());
            room.getCurrentTurn().setAlreadyPerformedMove(true);
            sendNextMoves();
        }else {
            clientConnection.sendMessage(new StringMessage("Action Could not be completed"));
            sendNextMoves();
        }

    }

    private void sendStateAndMovesForNextTurn(){

        ClientConnection currentPlayer = room.getConnections().get(room.getGame().getPlayers().indexOf(
                room.getGame().getCurrentPlayer()));
        GameUtils.debug(room.getPlayerFromConnection(currentPlayer).getUsername());
        room.sendAll(new UpdateAndDisplayGameStateMessage(room.getGame()));

        if(!gameController.isGameOver() || (room.getGame().getPlayers().indexOf(room.getPlayerFromConnection(currentPlayer)) != room.getGame().getInkwellPlayer())){
            room.setCurrentTurn(new Turn(room.getPlayerFromConnection(currentPlayer).getUsername(), gameController.computeNextPossibleMoves(false)));
            SelectMoveRequestMessage selectMoveRequestMessage = new SelectMoveRequestMessage(room.getCurrentTurn().getMoves());
            currentPlayer.sendMessage(selectMoveRequestMessage);
        } else {
            Map<String, Integer> standing = gameController.computeStanding();
            String winner = gameController.computeWinner();
            clientConnection.sendMessage(new GameOverMessage(winner, standing));
        }
    }

    private void sendNextMoves(){
        room.getCurrentTurn().setMoves(gameController.computeNextPossibleMoves(room.getCurrentTurn().hasAlreadyPerformedMove()));
        clientConnection.sendMessage(new UpdateGameStateMessage(room.getGame()));
        clientConnection.sendMessage(new SelectMoveRequestMessage(room.getCurrentTurn().getMoves()));
    }


    private void endTurn(){
        gameController.computeNextPlayer();
        sendStateAndMovesForNextTurn();
        GameUtils.debug(gameController.getGame().getCurrentPlayer().getUsername());
    }

    public void deactivateConnection(ClientConnection connection) {
        connection.setConnected(false);
        Player disconnectedPlayer = room.getPlayerFromConnection(connection);
        if(!ready){
            disconnectedPlayer.setActive(false);
            room.sendAll(new StringMessage(disconnectedPlayer.getUsername() + " disconnected"));
            return;
        }
        room.sendAll(new StringMessage(disconnectedPlayer.getUsername() + " disconnected"));

        if(room.getCurrentTurn()!= null && disconnectedPlayer.equals(room.getGame().getPlayerByUsername(room.getCurrentTurn().getCurrentPlayer()))){
            GameUtils.debug("ending turn");
            endTurn();
        } else  room.sendAll(new UpdateGameStateMessage(room.getGame()));

        disconnectedPlayer.setActive(false);
        /*
        if(room.getGame().getPlayers().size() == 1){
            System.out.println("game over");
            room.sendAll(new StringMessage("Game ended for lack of players"));
            // room.sendAll(new GameOverMessage(gameController.computeWinner(), gameController.computeStanding()));
        }
         */

        System.out.println("players" + gameController.getGame().getPlayers() + "conncetions" + room.getConnections().size());

    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public boolean isReady() {
        return ready;
    }
}
