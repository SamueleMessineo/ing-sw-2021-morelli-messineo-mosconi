package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.shared.Resource;

import java.util.List;
import java.util.ArrayList;

public class Turn {
    private String currentPlayer;
    private List<String> moves;
    private List<Resource> nonWhiteMarbles = new ArrayList<>();
    private List<Resource> whiteMarbles = new ArrayList<>();
    private boolean AlreadyPerformedMove = false;

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
        AlreadyPerformedMove = alreadyPerformedMove;
    }

    public boolean haveAlreadyPerformedMove() {
        return AlreadyPerformedMove;
    }

    public List<Resource> getNonWhiteMarbles() {
        return nonWhiteMarbles;
    }

    public void setNonWhiteMarbles(List<Resource> nonWhiteMarbles) {
        this.nonWhiteMarbles = nonWhiteMarbles;
    }

    public List<Resource> getWhiteMarbles() {
        return whiteMarbles;
    }

    public void setWhiteMarbles(List<Resource> whiteMarbles) {
        this.whiteMarbles = whiteMarbles;
    }
}
