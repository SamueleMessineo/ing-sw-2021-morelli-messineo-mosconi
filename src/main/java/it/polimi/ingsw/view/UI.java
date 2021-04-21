package it.polimi.ingsw.view;

import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;

public interface UI {

    public void displayRoomDettails(ArrayList<Player> players, int playersNum, String RoomId);
}
