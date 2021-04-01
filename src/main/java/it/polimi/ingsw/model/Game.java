package it.polimi.ingsw.model;

import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.SoloActionType;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Defines the main game class, it holds all the information of a game in progress
 */

public class Game {
    private ArrayList<Player> players;
    private Player currentPlayer;
    private Market market;
    private ArrayList<SoloActionType> soloActionTypes;


    public Game()  {
        this.players = new ArrayList<Player>();
        this.market = new Market();
        //TODO single soloActionType for single player
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Market getMarket() {
        return market;
    }


    public ArrayList<Player> getPlayers() {
        return players;
    }
}
