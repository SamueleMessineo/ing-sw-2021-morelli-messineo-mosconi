package it.polimi.ingsw.network;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ServerConnection;
import it.polimi.ingsw.network.client.RoomDettailsMessage;

public class ClientMessageHandler {
    ServerConnection serverConnection;

    public ClientMessageHandler(Client client, ServerConnection serverConnection){
        this.serverConnection = serverConnection;
    }

    public void handle(RoomDettailsMessage roomDettailsMessage){
        serverConnection.getClient().getUi().displayRoomDettails(roomDettailsMessage.getPlayers(), roomDettailsMessage.getPlayersNum(), roomDettailsMessage.getRoomId());
    }
}
