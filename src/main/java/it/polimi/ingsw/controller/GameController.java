package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.shared.Resource;

import java.util.List;
import java.util.Map;

public interface GameController {
    void startGame();

    void dropInitialLeaderCards(int selection1, int selection2, String player);

    Map<String, List<Resource>> getMarbles(String rowOrColumn, int index);

    void dropLeader(int card);

    void switchShelves(String shelf1, String shelf2);
}
