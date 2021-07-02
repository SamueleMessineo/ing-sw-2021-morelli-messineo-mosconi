package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.Resource;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * This class has all the information of a single turn of the game
 */
public class Turn {
    private String currentPlayer;
    private List<String> moves;
    private Map<Resource, Integer> converted;
    private int toConvert;
    private List<Resource> conversionOptions = new ArrayList<>();
    private DevelopmentCard boughtDevelopmentCard;

    private boolean alreadyPerformedMove = false;

    /**
     * Turn class constructor.
     */
    public Turn(String currentPlayer, List<String> moves) {
        this.currentPlayer = currentPlayer;
        this.moves = moves;
    }

    /**
     * @return the player that is playing
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player.
     * @param currentPlayer the Player that is playing.
     */
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * @return the moves tha player can perform
     */
    public List<String> getMoves() {
        return moves;
    }

    /**
     * Sets the current moves.
     * @param moves the moves the user can perform.
     */
    public void setMoves(List<String> moves) {
        this.moves = moves;
    }

    /**
     * @param move the move a player wants to make
     * @param username the player name
     * @return true if the player is the current player and can perform the move
     */
    public boolean isValidMove(String move, String username){
        return moves.contains(move) && currentPlayer.equals(username);
    }


    /**
     * Sets already performed move
     * @param alreadyPerformedMove true if the player has already done one of  the unique moves of the turn
     */
    public void setAlreadyPerformedMove(boolean alreadyPerformedMove) {
        this.alreadyPerformedMove = alreadyPerformedMove;
    }

    /**
     * Checks if a the current player has already performed one of the unique moves.
     * @return true if the player has performed one move between activating production, buying a card and getting marbles.
     */
    public boolean hasAlreadyPerformedMove() {
        return alreadyPerformedMove;
    }

    /**
     * @return the resources obtained by the player this turn by switching a row or column of marble structure.
     */
    public Map<Resource, Integer> getConverted() {
        return converted;
    }

    /**
     * Sets the coverted marbles in this turn.
     * @param converted a map with the converted marbles.
     */
    public void setConverted(Map<Resource, Integer> converted) {
        this.converted = converted;
    }

    /**
     * Gets the number of marbles  to convert in this turn.
     * @return the number of marbles  to convert in this turn.
     */
    public int getToConvert() {
        return toConvert;
    }

    /**
     * Sets to marbles  to convert.
     * @param toConvert the marbles to convert.
     */
    public void setToConvert(int toConvert) {
        this.toConvert = toConvert;
    }

    /**
     * Gets the conversion options.
     * @return a list of Resources.
     */
    public List<Resource> getConversionOptions() {
        return conversionOptions;
    }

    /**
     * Sets the conversion  options.
     */
    public void setConversionOptions(List<Resource> conversionOptions) {
        this.conversionOptions = conversionOptions;
    }

    /**
     * @return the development card the player bought this turn.
     */
    public DevelopmentCard getBoughtDevelopmentCard() {
        return boughtDevelopmentCard;
    }

    /**
     * Sets the development card bought in this turn.
     * @param boughtDevelopmentCard the development card bought in this turn.
     */
    public void setBoughtDevelopmentCard(DevelopmentCard boughtDevelopmentCard) {
        this.boughtDevelopmentCard = boughtDevelopmentCard;
    }
}
