package it.polimi.ingsw.model.player;
import it.polimi.ingsw.model.shared.PopesFavorTile;

import java.util.ArrayList;

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

    public int getPosition() {
        return position;
    }

    public int getLastCheckPoint() {
        return lastCheckPoint;
    }

    public ArrayList<PopesFavorTile> getPopesFavorTiles() {
        return popesFavorTiles;
    }

    public void move(){
        position++;
    }

    public int inOnPopeSpace(){
        if(position%8==0) return position/8;
        else return -1;

        //può capitare che il giocatore passa la casella senza fermarsi?
    }

    public boolean isInPopeFavorByLevel(int level){
        if (level==1 && position>=5)return true;
        else if (level == 2 && position >= 12)return true;
        else if (level == 3 && position >= 19)return true;
        else return false;

    }
}
