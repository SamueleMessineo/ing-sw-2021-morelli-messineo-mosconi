package it.polimi.ingsw.controller;

import java.util.List;

public class Turn {
    private String currentPlayer;
    private List<String> moves;

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
        System.out.println("validating");
        return (moves.contains(move) && currentPlayer.equals(username));
    }
}
