package it.polimi.ingsw.model.player;
import it.polimi.ingsw.model.shared.PopesFavorTile;

import java.util.ArrayList;

/**
 * The FaithTrack class stores the logic of the homonymous game component
 */
public class FaithTrack {
    private int position;
    private int lastCheckPoint;
    private ArrayList<PopesFavorTile> popesFavorTiles = new ArrayList<>();

    public FaithTrack() {
        this.position = 0;
        this.popesFavorTiles.add(0, new PopesFavorTile(2));
        this.popesFavorTiles.add(1, new PopesFavorTile(3));
        this.popesFavorTiles.add(2, new PopesFavorTile(4));
    }

    /**
     * @return the position of the player in the Faithtrack as an int
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return player's last checkpoint as an int
     */
    public int getLastCheckPoint() {
        return lastCheckPoint;
    }

    /**
     * @return an ArrayList with all the player's popeFavorTiles
     */
    public ArrayList<PopesFavorTile> getPopesFavorTiles() {
        return popesFavorTiles;
    }

    /**
     * Move the player on the next spot of the FaithTrack
     */
    public void move(){
        position++;
    }

    /**
     * @return '-1' if the player is not on a PopeSpace, else returns the PopeSpace level
     */
    public int inOnPopeSpace(){
        if(position%8==0) return position/8;
        else return -1;

        //può capitare che il giocatore passa la casella senza fermarsi?
    }

    /**
     * Used to know if the player has the right to receive points granted by a certain level PopeFavorTile
     * @param level
     * @return is true only if the player is in a position that grants him the PopeFavorTile of the level passed
     */
    public boolean isInPopeFavorByLevel(int level){
        if (level==1 && position>=5)return true;
        else if (level == 2 && position >= 12)return true;
        else if (level == 3 && position >= 19)return true;
        else return false;

    }
}
