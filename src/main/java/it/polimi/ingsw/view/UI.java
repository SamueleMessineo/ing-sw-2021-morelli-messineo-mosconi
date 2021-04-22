package it.polimi.ingsw.view;

import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;

public interface UI {

    public void displayRoomDetails(ArrayList<String> players, int playersNum, int RoomId);

    public void setup();
}
