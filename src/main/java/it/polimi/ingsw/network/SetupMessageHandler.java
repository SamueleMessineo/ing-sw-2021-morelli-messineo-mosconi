package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.network.setup.CreateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPrivateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPublicRoomMessage;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Server;

/**
 * This class handles the game setup messages.
 */
public class SetupMessageHandler {
    private final ServerController serverController;
    private final ClientConnection clientConnection;
    public SetupMessageHandler(Server server, ClientConnection clientConnection) {
        this.serverController=server.getServerController();
        this.clientConnection=clientConnection;
    }

    /**
     * This method tells the controller to create a  room with the parameters contained in message.
     * @param createRoomMessage a message with room-host name, for how many player is the room and if it is private.
     */
    public void handle(CreateRoomMessage createRoomMessage) {
        serverController.createRoom(createRoomMessage.isPrivateRoom(), createRoomMessage.getUsername(),
                                    createRoomMessage.getNumberOfPlayers(), clientConnection);
    }

    /**
     * This method tells the server that the player wants to join a game with given players.
     * @param joinPublicRoomMessage a message with player name and number of players of the game he wants to join.
     */
    public void handle(JoinPublicRoomMessage joinPublicRoomMessage) {
       serverController.addPlayerToPublicRoom(joinPublicRoomMessage.getNumberOfPlayers(), joinPublicRoomMessage.getUsername(), clientConnection);
    }

    /**
     * This method tells the server that the player wants to join a game with given id.
     * @param joinPrivateRoomMessage a message with player name and game id of the game he wants to join
     */
    public void handle(JoinPrivateRoomMessage joinPrivateRoomMessage) {
       serverController.addPlayerByRoomId(joinPrivateRoomMessage.getUsername(),joinPrivateRoomMessage.getRoomId(), clientConnection);
    }
}
