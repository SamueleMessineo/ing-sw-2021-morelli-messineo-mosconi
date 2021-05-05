package it.polimi.ingsw.network;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ServerConnection;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.network.client.GameStateMessage;
import it.polimi.ingsw.view.UI;

public class ClientMessageHandler {
    private final ServerConnection serverConnection;
    private final UI ui;

    public ClientMessageHandler(Client client, ServerConnection serverConnection){
        this.serverConnection = serverConnection;
        this.ui = serverConnection.getClient().getUi();
    }

    public void handle(RoomDetailsMessage roomDetailsMessage){
        ui.displayRoomDetails(roomDetailsMessage.getPlayers(), roomDetailsMessage.getPlayersNum(), roomDetailsMessage.getRoomId());
    }

    public void handle(ErrorMessage message) {
        ui.displayError(message.getBody());
    }

    public void handle(StringMessage message){ ui.displayString(message.getBody());}

    public void handle(DropInitialLeaderCardsRequestMessage message){
        ui.selectLeaderCards(message.getLeaderCards());
    }

    public void handle(SelectMoveRequestMessage message) {
        ui.displayPossibleMoves(message.getMoves());
    }

    public void handle(GameStateMessage message) {
        ui.setGameState(message.getGame());
        ui.displayGameState();
    }

    public void handle(SelectMarblesRequestMessage message){
        ui.displayMarbles(message.getMarbleStructure());
    }

    public void handle(DropResourceRequestMessage message){
        ui.dropResources(message.getResources());
    }

    public void handle(DropLeaderCardRequestMessage message){
        ui.discardLeaderCard(message.getLeaderCards());
    }

    public void handle(SwitchShelvesRequestMessage message){
        ui.switchShelves(message.getShelves());
    }

    public void handle(SelectResourceForWhiteMarbleRequestMessage message) {

    }

    public void handle(ActivateProductionRequestMessage message){
        ui.activateProduction(message.getProductionPowers());
    }

    public void handle(BuyDevelopmentCardRequestMessage message){
        ui.buyDevelopmentCard(message.getDevelopmentCards());
    }

    public void handle(SelectStackToPlaceCardRequestMessage message){
        ui.selectStackToPlaceCard(message.getDevelopmentCards());
    }

    public void handle(PlayLeaderRequestMessage message){
        ui.playLeader(message.getLeaderCards());
    }

}
