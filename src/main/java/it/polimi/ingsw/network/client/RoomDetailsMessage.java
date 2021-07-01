package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.ClientMessageHandler;

import java.util.ArrayList;

/**
 * Message with the details of the room.
 */
public class RoomDetailsMessage extends ClientMessage {
    private final ArrayList<String> players;
    private final int playersNum;
    private final int roomId;

    public RoomDetailsMessage(ArrayList<String> players, int playersNum, int roomId) {
        this.players = players;
        this.playersNum = playersNum;
        this.roomId = roomId;
    }

    public void accept(ClientMessageHandler clientMessageHandler){clientMessageHandler.handle(this); }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public int getPlayersNum() {
        return playersNum;
    }

    public int getRoomId() {
        return roomId;
    }


}
