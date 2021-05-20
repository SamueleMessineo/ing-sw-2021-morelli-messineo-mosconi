package it.polimi.ingsw.model.shared;

import org.junit.Test;

import static org.junit.Assert.*;

public class SoloActionTypeTest {

    @Test
    public void values() {
//        GREEN, YELLOW,PURPLE, BLUE, PLUS_ONE,PLUS_TWO,
        assertEquals("GREEN",SoloActionType.GREEN.name());
        assertEquals("YELLOW",SoloActionType.YELLOW.name());
        assertEquals("BLUE",SoloActionType.BLUE.name());
        assertEquals("PLUS_ONE",SoloActionType.PLUS_ONE.name());
        assertEquals("PLUS_TWO",SoloActionType.PLUS_TWO.name());
    }
}