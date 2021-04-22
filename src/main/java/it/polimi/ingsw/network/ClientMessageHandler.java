package it.polimi.ingsw.network;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ServerConnection;
import it.polimi.ingsw.network.client.RoomDetailsMessage;

public class ClientMessageHandler {
    ServerConnection serverConnection;

    public ClientMessageHandler(Client client, ServerConnection serverConnection){
        this.serverConnection = serverConnection;
    }

    public void handle(RoomDetailsMessage roomDetailsMessage){
        serverConnection.getClient().getUi().displayRoomDetails(roomDetailsMessage.getPlayers(), roomDetailsMessage.getPlayersNum(), roomDetailsMessage.getRoomId());
    }
}
