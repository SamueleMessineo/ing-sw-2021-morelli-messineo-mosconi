package it.polimi.ingsw.network.setup;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.SetupMessageHandler;

public class CreateRoomMessage extends SetupMessage {

    private final boolean privateRoom;
    private final int numberOfPlayers;
    private final String username;

    public CreateRoomMessage(boolean privateRoom, int numberOfPlayers, String username) {
        this.privateRoom = privateRoom;
        this.numberOfPlayers = numberOfPlayers;
        this.username = username;
    }

    public boolean isPrivateRoom() {
        return privateRoom;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public String getUsername() {
        return username;
    }

    public void accept(SetupMessageHandler handler) {
        handler.handle(this);
    }

}
