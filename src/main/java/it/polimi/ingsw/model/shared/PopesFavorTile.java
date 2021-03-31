package it.polimi.ingsw.model.shared;

/**
 * The PopesFavorTile class represents the Pope’s Favor tiles that are placed on the
 * corresponding spaces of the Faith Track
 */

public class PopesFavorTile {
    private final int score;
    private final PopesFavorTileState state;

    public PopesFavorTile(int score){
        this.state=PopesFavorTileState.INACTIVE;
        this.score=score;
    }

    public int getScore(){
        return score;
    }

    public PopesFavorTileState getState(){
        return state;
    }

}
