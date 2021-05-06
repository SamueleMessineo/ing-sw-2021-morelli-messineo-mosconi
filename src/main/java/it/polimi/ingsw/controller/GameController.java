package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface GameController {
    void startGame();

    void dropInitialLeaderCards(int selection1, int selection2, String player);

    void giveInitialResources(List<Resource> resources, String username);

    Map<String, List<Resource>> getMarbles(String rowOrColumn, int index);

    void dropLeader(int card);

    void computeCurrentPlayer();

    boolean switchShelves(String shelf1, String shelf2);

    List<String> computeNextPossibleMoves(boolean alreadyPerfomedMove);

    List<DevelopmentCard> getBuyableDevelopementCards();

    boolean isGameOver();

    void playLeader(int cardIndex);

    List<LeaderCard> getPlayableLeaderCards();
}
