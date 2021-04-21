package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.ClientMessageHandler;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.setup.Room;

import java.util.ArrayList;

public class RoomDetailsMessage extends ClientMessage {
    private final ArrayList<String> players;
    private final int playersNum;
    private final String roomId;

    public RoomDetailsMessage(ArrayList<String> players, int playersNum, String roomId) {
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

    public String getRoomId() {
        return roomId;
    }


}
