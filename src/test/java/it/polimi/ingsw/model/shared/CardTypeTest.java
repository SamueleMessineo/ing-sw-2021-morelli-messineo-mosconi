package it.polimi.ingsw.model.shared;

import org.junit.Test;

import static org.junit.Assert.*;

public class CardTypeTest {

    @Test
    public void testToString() {
        CardType cardTypeTest=CardType.BLUE;
        assertEquals("🟦",cardTypeTest.toString());

        cardTypeTest=CardType.GREEN;
        assertEquals("🟩",cardTypeTest.toString());

        cardTypeTest=CardType.YELLOW;
        assertEquals("🟨",cardTypeTest.toString());

        cardTypeTest=CardType.PURPLE;
        assertEquals("🟪",cardTypeTest.toString());
    }
}