package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.network.setup.CreateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPrivateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPublicRoomMessage;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Server;

public class SetupMessageHandler {
    private final ServerController serverController;
    private final ClientConnection clientConnection;
    public SetupMessageHandler(Server server, ClientConnection clientConnection) {
        this.serverController=server.getServerController();
        this.clientConnection=clientConnection;
    }

    public void handle(CreateRoomMessage createRoomMessage) {
        serverController.createRoom(createRoomMessage.isPrivateRoom(), createRoomMessage.getUsername(),
                                    createRoomMessage.getNumberOfPlayers(), clientConnection);
    }

    public void handle(JoinPublicRoomMessage joinPublicRoomMessage) {
       serverController.addPlayerToPublicRoom(joinPublicRoomMessage.getNumberOfPlayers(), joinPublicRoomMessage.getUsername(), clientConnection);
    }

    public void handle(JoinPrivateRoomMessage joinPrivateRoomMessage) {
       serverController.addPlayerByRoomId(joinPrivateRoomMessage.getUsername(),joinPrivateRoomMessage.getRoomId(), clientConnection);
    }
}
