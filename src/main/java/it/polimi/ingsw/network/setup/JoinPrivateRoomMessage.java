package it.polimi.ingsw.network.setup;

import it.polimi.ingsw.network.SetupMessageHandler;

public class JoinPrivateRoomMessage extends SetupMessage{

    private final int roomId;
    private final String username;

    public JoinPrivateRoomMessage(int roomId, String username) {
        this.roomId = roomId;
        this.username=username;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getUsername() {
        return username;
    }

    public void accept(SetupMessageHandler handler) {
        handler.handle(this);
    }

}
