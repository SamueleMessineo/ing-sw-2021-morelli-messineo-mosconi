package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Turn {
    private String currentPlayer;
    private List<String> moves;
//    private List<Resource> converted = new ArrayList<>();
    private Map<Resource, Integer> converted;
    private int toConvert;
//    private List<Resource> toConvert = new ArrayList<>();
    private List<Resource> conversionOptions = new ArrayList<>();
    private DevelopmentCard buyedDevelopmentCard;

    private boolean alreadyPerformedMove = false;

    public Turn(String currentPlayer, List<String> moves) {
        this.currentPlayer = currentPlayer;
        this.moves = moves;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<String> getMoves() {
        return moves;
    }

    public void setMoves(List<String> moves) {
        this.moves = moves;
    }

    public boolean isValidMove(String move, String username){
        return moves.contains(move) && currentPlayer.equals(username);
    }

    public void setAlreadyPerformedMove(boolean alreadyPerformedMove) {
        this.alreadyPerformedMove = alreadyPerformedMove;
    }

    public boolean hasAlreadyPerformedMove() {
        return alreadyPerformedMove;
    }

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

    public DevelopmentCard getBuyedDevelopmentCard() {
        return buyedDevelopmentCard;
    }

    public void setBuyedDevelopmentCard(DevelopmentCard buyedDevelopmentCard) {
        this.buyedDevelopmentCard = buyedDevelopmentCard;
    }
}
