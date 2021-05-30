package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * This class has all the information of a single turn of the game
 */
public class Turn {
    private String currentPlayer;
    private List<String> moves;
//    private List<Resource> converted = new ArrayList<>();
    private Map<Resource, Integer> converted;
    private int toConvert;
//    private List<Resource> toConvert = new ArrayList<>();
    private List<Resource> conversionOptions = new ArrayList<>();
    private DevelopmentCard boughtDevelopmentCard;

    private boolean alreadyPerformedMove = false;

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

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * @return the moves tha player can perform
     */
    public List<String> getMoves() {
        return moves;
    }

    public void setMoves(List<String> moves) {
        this.moves = moves;
    }

    /**
     * @param move the move a player wants to make
     * @param username the player name
     * @return true if the player is the current player and can permorm the move
     */
    public boolean isValidMove(String move, String username){
        return moves.contains(move) && currentPlayer.equals(username);
    }


    public void setAlreadyPerformedMove(boolean alreadyPerformedMove) {
        this.alreadyPerformedMove = alreadyPerformedMove;
    }

    /**
     * Checks if a the current player has already performed one of the unique moves
     * @return true if the player has performed one move between activating production, buying a card and getting marbles
     */
    public boolean hasAlreadyPerformedMove() {
        return alreadyPerformedMove;
    }

    /**
     * @return the resources obtained by the player this turn by switching a row or column of marble structure
     */
    public Map<Resource, Integer> getConverted() {
        return converted;
    }

    public void setConverted(Map<Resource, Integer> converted) {
        this.converted = converted;
    }

    public int getToConvert() {
        return toConvert;
    }

    public void setToConvert(int toConvert) {
        this.toConvert = toConvert;
    }
//    public List<Resource> getConverted() {
//        return converted;
//    }
//
//    public void setConverted(List<Resource> converted) {
//        this.converted = converted;
//    }
//
//    public List<Resource> getToConvert() {
//        return toConvert;
//    }
//
//    public void setToConvert(List<Resource> toConvert) {
//        this.toConvert = toConvert;
//    }

    public List<Resource> getConversionOptions() {
        return conversionOptions;
    }

    public void setConversionOptions(List<Resource> conversionOptions) {
        this.conversionOptions = conversionOptions;
    }

    /**
     * @return the development card the player bought this turn
     */
    public DevelopmentCard getBoughtDevelopmentCard() {
        return boughtDevelopmentCard;
    }

    public void setBoughtDevelopmentCard(DevelopmentCard boughtDevelopmentCard) {
        this.boughtDevelopmentCard = boughtDevelopmentCard;
    }
}
