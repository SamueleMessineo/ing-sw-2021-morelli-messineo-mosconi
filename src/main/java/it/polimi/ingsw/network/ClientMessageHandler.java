package it.polimi.ingsw.network;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ServerConnection;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.network.client.UpdateGameStateMessage;
import it.polimi.ingsw.view.UI;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientMessageHandler {
    private final ServerConnection serverConnection;
    private final UI ui;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private ClientMessage latestMessage = null;

    public ClientMessageHandler(Client client, ServerConnection serverConnection){
        this.serverConnection = serverConnection;
        this.ui = serverConnection.getClient().getUi();
    }

    public void handle(RoomDetailsMessage roomDetailsMessage){
        executor.submit(() -> ui.displayRoomDetails(roomDetailsMessage.getPlayers(),
                roomDetailsMessage.getPlayersNum(), roomDetailsMessage.getRoomId()));
    }

    public void handle(ErrorMessage message) {
        executor.submit(() -> ui.displayError(message.getBody()));
        if (latestMessage != null)
            latestMessage.accept(this);
     }

    public void handle(StringMessage message) {
        executor.submit(() -> ui.displayString(message.getBody()));
    }

    public void handle(DropInitialLeaderCardsRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.selectLeaderCards(message.getLeaderCards()));
    }

    public void handle(SelectMoveRequestMessage message) {
        latestMessage = message;
        executor.submit(() -> ui.displayPossibleMoves(message.getMoves()));
    }

    public void handle(UpdateGameStateMessage message) {
        executor.submit(() -> ui.setGameState(message.getGame()));
    }

    public void handle(UpdateAndDisplayGameStateMessage message){
        ui.setGameState(message.getGame());
        ui.displayGameState();
    }

    public void handle(SelectMarblesRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.selectMarbles(message.getMarbleStructure()));
    }

    public void handle(DropResourceRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.dropResources(message.getResources()));
    }

    public void handle(DropLeaderCardRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.discardLeaderCard(message.getLeaderCards()));
    }

    public void handle(SwitchShelvesRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.switchShelves(message.getShelves()));
    }

    public void handle(SelectResourceForWhiteMarbleRequestMessage message) {
        latestMessage = message;

    }

    public void handle(ActivateProductionRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.activateProduction(message.getProductionPowers()));
    }

    public void handle(BuyDevelopmentCardRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.buyDevelopmentCard(message.getDevelopmentCards()));
    }

    public void handle(SelectStackToPlaceCardRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.selectStackToPlaceCard(message.getStacks()));
    }

    public void handle(PlayLeaderRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.playLeader(message.getLeaderCards()));
    }

    public void handle(SelectInitialResourceRequestMessage message) {
        latestMessage = message;
        executor.submit(() -> ui.selectInitialResources(message.getResources(), message.getAmount()));
    }
    public void handle(GameOverMessage message){
        executor.submit(() -> ui.gameOver(message.getWinner(), message.getStanding()));
    }
}
