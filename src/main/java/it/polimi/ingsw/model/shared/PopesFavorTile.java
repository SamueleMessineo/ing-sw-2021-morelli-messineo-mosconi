package it.polimi.ingsw.model.shared;

import java.io.Serializable;

/**
 * The PopesFavorTile class represents the Pope’s Favor tiles that are placed on the
 * corresponding spaces of the Faith Track.
 */

public class PopesFavorTile implements Serializable {
    private final int score;
    private PopesFavorTileState state;

    /**
     * PopesFavorTile class constructor.
     */
    public PopesFavorTile(int score){
        this.state=PopesFavorTileState.INACTIVE;
        this.score=score;
    }

    /**
     * Gets the score of the tile.
     * @return score.
     */
    public int getScore(){
        return score;
    }

    /**
     * Gets the state of tile (ACTIVE, INACTIVE, DISCARDED).
     * @return state.
     */
    public PopesFavorTileState getState(){
        return state;
    }

    /**
     * Sets the state of tile.
     * @param state the state of tile (ACTIVE, INACTIVE, DISCARDED).
     */
    public void setState(PopesFavorTileState state) {
        this.state = state;
    }

}
