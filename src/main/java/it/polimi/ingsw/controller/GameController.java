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


    public void dropInitialLeaderCards(int selection1, int selection2, String player){
    }
}
