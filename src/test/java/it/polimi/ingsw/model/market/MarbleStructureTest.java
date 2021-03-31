package it.polimi.ingsw.model.market;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

//100% covered
public class MarbleStructureTest {

    private MarbleStructure structure;

    @Before
    public void setUp() throws Exception {
        ArrayList<Marble> marbles = new ArrayList<>();
        marbles.add(Marble.BLUE);
        marbles.add(Marble.GREY);
        marbles.add(Marble.BLUE);
        marbles.add(Marble.YELLOW);
        marbles.add(Marble.BLUE);
        marbles.add(Marble.PURPLE);
        marbles.add(Marble.BLUE);
        marbles.add(Marble.GREY);
        marbles.add(Marble.WHITE);
        marbles.add(Marble.GREY);
        marbles.add(Marble.BLUE);
        marbles.add(Marble.GREY);
        structure = new MarbleStructure(marbles, Marble.PURPLE);
    }

    @Test
    public void getColumn() {
        ArrayList<Marble> expected0 = new ArrayList<>();
        expected0.add(Marble.BLUE);
        expected0.add(Marble.BLUE);
        expected0.add(Marble.WHITE);
        assertEquals(expected0, structure.getColumn(0));

        ArrayList<Marble> expected1 = new ArrayList<>();
        expected1.add(Marble.GREY);
        expected1.add(Marble.PURPLE);
        expected1.add(Marble.GREY);
        assertEquals(expected1, structure.getColumn(1));

        ArrayList<Marble> expected2 = new ArrayList<>();
        expected2.add(Marble.BLUE);
        expected2.add(Marble.BLUE);
        expected2.add(Marble.BLUE);
        assertEquals(expected2, structure.getColumn(2));

        ArrayList<Marble> expected3 = new ArrayList<>();
        expected3.add(Marble.YELLOW);
        expected3.add(Marble.GREY);
        expected3.add(Marble.GREY);
        assertEquals(expected3, structure.getColumn(3));
    }

    @Test
    public void getRow() {
        ArrayList<Marble> expected0 = new ArrayList<>();
        expected0.add(Marble.BLUE);
        expected0.add(Marble.GREY);
        expected0.add(Marble.BLUE);
        expected0.add(Marble.YELLOW);
        assertEquals(expected0, structure.getRow(0));

        ArrayList<Marble> expected1 = new ArrayList<>();
        expected1.add(Marble.BLUE);
        expected1.add(Marble.PURPLE);
        expected1.add(Marble.BLUE);
        expected1.add(Marble.GREY);
        assertEquals(expected1, structure.getRow(1));

        ArrayList<Marble> expected2 = new ArrayList<>();
        expected2.add(Marble.WHITE);
        expected2.add(Marble.GREY);
        expected2.add(Marble.BLUE);
        expected2.add(Marble.GREY);
        assertEquals(expected2, structure.getRow(2));
    }

    @Test
    public void shiftColumn() {
        for (int i = 0; i<=3; i++) {
            List<Marble> oldColumn = structure.getColumn(i);
            assertEquals(oldColumn,structure.shiftColumn(i));

        }
    }

    @Test
    public void shiftRow() {
        for (int i = 0; i <= 2; i++) {
            List<Marble> oldRow = structure.getRow(i);
            assertEquals(oldRow, structure.shiftRow(i));

        }
    }
}