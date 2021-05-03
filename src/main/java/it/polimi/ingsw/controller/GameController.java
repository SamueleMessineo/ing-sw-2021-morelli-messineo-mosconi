package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.shared.Resource;

import java.util.List;

public interface GameController {


    public void startGame();


    public void dropInitialLeaderCards(int selection1, int selection2, String player);

    public List<Resource> getMarbles(String rowOrColumn, int index);

    public void dropLeader(int card);

    public void switchShelves(String shelf1, String shelf2);

}
