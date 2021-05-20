package it.polimi.ingsw.model.player;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FaithTrackTest {
    FaithTrack faithtrack;

    @Before
    public void setUp() {
        this.faithtrack=new FaithTrack();
    }

    @Test
    public void getVP() {
        for(int i=0; i<=faithtrack.getMaxposition(); i++){
            if(faithtrack.getPosition()<3)
                assertEquals(0,faithtrack.getVP());
            if(faithtrack.getPosition()<6 && faithtrack.getPosition()>=3)
                assertEquals(1,faithtrack.getVP());
            if(faithtrack.getPosition()<9 && faithtrack.getPosition()>=6)
                assertEquals(2,faithtrack.getVP());
            if(faithtrack.getPosition()<12 && faithtrack.getPosition()>=9)
                assertEquals(4,faithtrack.getVP());
            if(faithtrack.getPosition()<15 && faithtrack.getPosition()>=12)
                assertEquals(6,faithtrack.getVP());
            if(faithtrack.getPosition()<18 && faithtrack.getPosition()>=15)
                assertEquals(9,faithtrack.getVP());
            if(faithtrack.getPosition()<21 && faithtrack.getPosition()>=18)
                assertEquals(12,faithtrack.getVP());
            if(faithtrack.getPosition()<23 && faithtrack.getPosition()>=21)
                assertEquals(16,faithtrack.getVP());
            if(faithtrack.getPosition()== faithtrack.getMaxposition())
                assertEquals(20,faithtrack.getVP());
            faithtrack.move();
        }
        assertEquals(faithtrack.getMaxposition(),faithtrack.getPosition());
    }

    @Test
    public void inOnPopeSpace() {
        for(int i=0; i<=faithtrack.getMaxposition(); i+=8){
            if(faithtrack.getPosition()==8)
                assertEquals(1,faithtrack.inOnPopeSpace());
            if(faithtrack.getPosition()==16)
                assertEquals(2,faithtrack.inOnPopeSpace());
            if(faithtrack.getPosition()== faithtrack.getMaxposition())
                assertEquals(3,faithtrack.inOnPopeSpace());
            faithtrack.move();
        }
    }

    @Test
    public void isInPopeFavorByLevel() {
        for(int i=0; i<=faithtrack.getMaxposition(); i++){
            if(faithtrack.getPosition()>=5 && faithtrack.getPosition()<12) {
                assertTrue(faithtrack.isInPopeFavorByLevel(1));
                assertFalse(faithtrack.isInPopeFavorByLevel(2));
                assertFalse(faithtrack.isInPopeFavorByLevel(3));
                assertFalse(faithtrack.isInPopeFavorByLevel(4));
            }
            else if(faithtrack.getPosition()>=12 && faithtrack.getPosition()<19) {
                assertTrue(faithtrack.isInPopeFavorByLevel(1));
                assertTrue(faithtrack.isInPopeFavorByLevel(2));
                assertFalse(faithtrack.isInPopeFavorByLevel(3));
                assertFalse(faithtrack.isInPopeFavorByLevel(4));
            }
            else if(faithtrack.getPosition()>=19 && faithtrack.getPosition()<= faithtrack.getMaxposition()) {
                assertTrue(faithtrack.isInPopeFavorByLevel(1));
                assertTrue(faithtrack.isInPopeFavorByLevel(2));
                assertTrue(faithtrack.isInPopeFavorByLevel(3));
                assertFalse(faithtrack.isInPopeFavorByLevel(4));
            }
            faithtrack.move();
        }
    }
}