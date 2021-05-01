package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.client.StringMessage;
import it.polimi.ingsw.network.setup.Room;
import it.polimi.ingsw.server.ClientConnection;

import java.util.ArrayList;
import java.util.List;

public interface GameController {


    public void startGame();


    public void dropInitialLeaderCards(int selection1, int selection2, String player);

    public List<Resource> getMarbles(String rowOrColumn, int index);

    public void dropLeader(int card);

    public void switchShelves(String shelf1, String shelf2);

}
