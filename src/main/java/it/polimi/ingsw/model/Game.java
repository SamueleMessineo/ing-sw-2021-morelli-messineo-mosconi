package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.market.MarketCardStack;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.SoloActionType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Defines the main game class, it holds all the information of a game in progress
 */

public class Game {
    private ArrayList<Player> players;
    private Player currentPlayer;
    private Market market;
    private ArrayList<SoloActionType> soloActionTypes;
    private ArrayList<DevelopmentCard> developmentCards=new ArrayList<>();
    private ArrayList<LeaderCard> leaderCards=new ArrayList<>();



    public Game()  {
        this.players = new ArrayList<Player>();
        this.market = new Market();
        //TODO single soloActionType for single player

        try {
            loadResources();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        market.setCards(developmentCards);
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

    private void loadResources() throws FileNotFoundException{
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/developmentCards.json"));

        Type listType = new TypeToken<List<DevelopmentCard>>() {
        }.getType();

        developmentCards = gson.fromJson(reader, listType);

        reader = new BufferedReader(new FileReader("src/main/resources/leaderCards.json"));
        listType = new TypeToken<List<LeaderCard>>() {
        }.getType();

        leaderCards=gson.fromJson(reader,listType);
        Collections.shuffle(leaderCards);
    }

    public void addPlayer(String username){
        Player newPlayer=new Player(username);
        players.add(newPlayer);
        giveLeaderCardsToPlayer(newPlayer);
    }

    public void removePlayer(Player player){
        players.remove(player);
    }

    private void giveLeaderCardsToPlayer(Player player){
        player.setLeaderCards(new ArrayList<LeaderCard>(leaderCards.subList(0,4)));
        leaderCards.removeAll(leaderCards.subList(0,4));
    }


}
