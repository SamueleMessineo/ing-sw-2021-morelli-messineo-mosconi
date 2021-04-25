package it.polimi.ingsw.network;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ServerConnection;
import it.polimi.ingsw.network.client.*;
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

    public void handle(DropLeaderCardsRequestMessage message){
        System.out.println("handling");
        ui.selectLeaderCards(message.getLeaderCards());
    }

    public void handle(PossibleMovesMessage message) {
        System.out.println("possible moves");
    }
}
