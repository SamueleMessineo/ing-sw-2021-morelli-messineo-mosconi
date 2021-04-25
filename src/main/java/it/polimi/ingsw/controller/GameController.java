package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.network.client.StringMessage;
import it.polimi.ingsw.network.setup.Room;
import it.polimi.ingsw.server.ClientConnection;

import java.util.ArrayList;

public abstract class GameController {
    private  Room room;
    private  Game game;

    public void startGame(){
        System.out.println("error");
    }

    public Player getPlayerFromConnection(ClientConnection clientConnection){
        return room.getGame().getPlayers().get(room.getConnections().indexOf(clientConnection));
    }

    public void dropInitialLeaderCards(int selection1, int selection2, ClientConnection clientConnection){
        getPlayerFromConnection(clientConnection).dropInitialLeaderCards(selection1, selection2);
        System.out.println("ok");
        System.out.println(room.getGame().getPlayers());
        for (Player p : room.getGame().getPlayers()) {
            System.out.println(p.getLeaderCards().size());
            if (p.getLeaderCards().size() != 2) return;
        }
        room.sendAll(new StringMessage("start playing"));
    }
}
