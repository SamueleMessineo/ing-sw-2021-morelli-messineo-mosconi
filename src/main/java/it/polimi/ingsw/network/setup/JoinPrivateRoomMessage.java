package it.polimi.ingsw.network.setup;

import it.polimi.ingsw.network.SetupMessageHandler;

public class JoinPrivateRoomMessage extends SetupMessage{

    private final String roomId;
    private final String username;


    public JoinPrivateRoomMessage(String roomId, String username) {
        this.roomId = roomId;
        this.username=username;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getUsername() {
        return username;
    }

    public void accept(SetupMessageHandler handler) {
        handler.handle(this);
    }

}
