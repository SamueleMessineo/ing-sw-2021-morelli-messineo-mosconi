package it.polimi.ingsw.view;

import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;

public interface UI {

    void displayRoomDetails(ArrayList<String> players, int playersNum, int RoomId);

    void setup();

    void displayError(String body);
}
