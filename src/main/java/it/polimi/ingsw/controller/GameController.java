package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.client.StringMessage;
import it.polimi.ingsw.server.Room;
import it.polimi.ingsw.server.ClientConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface GameController {
    void startGame();

    void dropInitialLeaderCards(int selection1, int selection2, String player);

    Map<String, List<Resource>> getMarbles(String rowOrColumn, int index);

    void dropLeader(int card);

    public boolean switchShelves(String shelf1, String shelf2);

    public List<String> computeNextPossibleMoves(boolean alreadyPerfomedMove);

}
