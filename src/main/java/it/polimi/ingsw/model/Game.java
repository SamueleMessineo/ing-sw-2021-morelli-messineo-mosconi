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

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Defines the main game class, it holds all the information of a game in progress
 */

public class Game implements Serializable{
    private ArrayList<Player> players;
    private int currentPlayer;
    private int inkwellPlayer;
    private Market market;
    private Stack<SoloActionType> soloActionTypes;
    private ArrayList<DevelopmentCard> developmentCards=new ArrayList<>();
    private ArrayList<LeaderCard> leaderCards=new ArrayList<>();
    private Player LorenzoIlMagnifico;

    /**
     * Game class constructor.
     */
    public Game()  {
        this.players = new ArrayList<Player>();
        this.market = new Market();
        try {
            loadResources();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        market.setCards(developmentCards);
    }

    /**
     * Returns the player that is currently playing.
     * @return The Player object of the current player.
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    /**
     * Sets the index of the player that is currently playing.
     * @param playerIndex The index of the new playing player.
     */
    public void setCurrentPlayer(int playerIndex) {
        currentPlayer = playerIndex;
    }

    /**
     * @return the index of the first player to play the game
     */
    public int getInkwellPlayer() {
        return inkwellPlayer;
    }

    /**
     * Sets the inkwell player.
     * @param inkwellPlayer index of first player.
     */
    public void setInkwellPlayer(int inkwellPlayer) {
        this.inkwellPlayer = inkwellPlayer;
    }

    /**
     * Returns the market object.
     * @return The market object.
     */
    public Market getMarket() {
        return market;
    }

    /**
     * Returns the list of players in the game.
     * @return The list of players in the game.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the list of active players in the game.
     * @return The list of players in the game.
     */
    public ArrayList<Player> getActivePlayers() {
        ArrayList<Player> activePlayers = new ArrayList<>();
        for (Player p : players) {
            if (p.isActive()) activePlayers.add(p);
        }
        return activePlayers;
    }

    /**
     * Returns the list of inactive players in the game.
     * @return The list of players in the game.
     */
    public ArrayList<Player> getInactivePlayers() {
        ArrayList<Player> inactivePlayers = new ArrayList<>();
        for (Player p : players) {
            if (!p.isActive()) inactivePlayers.add(p);
        }
        return inactivePlayers;
    }

    /**
     * Reads the leader and development cards from the filesystem and builds the classes.
     * @throws FileNotFoundException If the files are not found.
     */
    private void loadResources() throws FileNotFoundException{
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Reader reader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream("developmentCards.json")));
        Type listType = new TypeToken<List<DevelopmentCard>>() {}.getType();
        developmentCards = gson.fromJson(reader, listType);

        reader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream("leaderCards.json")));
        //reader = new BufferedReader(new FileReader("src/main/resources/riggedLeaderCards.json"));
        listType = new TypeToken<List<LeaderCard>>(){}.getType();
        leaderCards=gson.fromJson(reader,listType);
        Collections.shuffle(leaderCards);
    }

    /**
     * Adds a new player to the game.
     * @param username The username of the new player.
     */
    public void addPlayer(String username){
        Player newPlayer=new Player(username);
        players.add(newPlayer);
        giveLeaderCardsToPlayer(newPlayer);
    }

    /**
     * Removes a player from the game.
     * @param player The player object to remove.
     */
    public void removePlayer(Player player){
        players.remove(player);
    }

    /**
     * Assigns to a player their initial leader cards.
     * @param player The player.
     */
    private void giveLeaderCardsToPlayer(Player player){
        player.setLeaderCards(new ArrayList<LeaderCard>(leaderCards.subList(0,4)));
        leaderCards.removeAll(leaderCards.subList(0,4));
    }

    /**
     * Gets a Player associated with a username.
     * @param username the username of the player.
     * @return the Player associated with username.
     */
    public Player getPlayerByUsername(String username){
        if(username.trim().replaceAll(" ","").toLowerCase().equals("lorenzoilmagnifico"))return getLorenzoIlMagnifico();
        try {
            return players.stream().filter(player -> player.getUsername().toLowerCase().trim().equals(username.toLowerCase().trim())).findFirst().orElseThrow();
        } catch (NoSuchElementException e){
            return null;
        }

    }

    /**
     * Gets LorenzoIlMagnifico.
     * @return LorenzoIlMagnifico.
     */
    public Player getLorenzoIlMagnifico() {
        return LorenzoIlMagnifico;
    }

    /**
     * Sets LorenzoIlMagnifico if the game is a soloGame.
     * @param lorenzoIlMagnifico a Player with LorenzoIlMagnifico as username.
     */
    public void setLorenzoIlMagnifico(Player lorenzoIlMagnifico) {
        LorenzoIlMagnifico = lorenzoIlMagnifico;
    }

    /**
     * Sets the seven soloActionTiles and shuffles them.
     */
    public void setSoloActionTypes() {
        soloActionTypes = new Stack<>();
        soloActionTypes.add(SoloActionType.PLUS_ONE);
        soloActionTypes.add(SoloActionType.PLUS_TWO);
        soloActionTypes.add(SoloActionType.GREEN);
        soloActionTypes.add(SoloActionType.BLUE);
        soloActionTypes.add(SoloActionType.PURPLE);
        soloActionTypes.add(SoloActionType.YELLOW);

        Collections.shuffle(soloActionTypes);

    }

    /**
     * Gets the SoloActionTiles.
     * @return a list with the SoloActionTiles.
     */
    public Stack<SoloActionType> getSoloActionTypes() {
        return soloActionTypes;
    }

    /**
     * Removes two cards from a stack associated with the soloActionTile.
     * @param soloActionType that contains the color of the stack.
     */
    public void removeCardsByLorenzo(SoloActionType soloActionType){

        for (int i = market.getCardsGrid().size()-1; i >= 0 ; i--) {
            MarketCardStack stack = market.getCardsGrid().get(i);
            if(!stack.isEmpty()){
                if( stack.getType().name().equals(soloActionType.name())){
                    stack.pop();
                    if(!stack.isEmpty()){
                        stack.pop();
                    }
                    return;
                }
            }
        }
    }
}
