package it.polimi.ingsw.view;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.network.client.StringMessage;

import java.util.ArrayList;

public interface UI {

    void displayRoomDetails(ArrayList<String> players, int playersNum, int RoomId);

    void setup();

    void displayError(String body);

    void displayString(String body);

    void selectLeaderCards(ArrayList<LeaderCard> leaderCards);
}
