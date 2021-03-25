package it.polimi.ingsw.model.market;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
        ArrayList<Marble> expected = new ArrayList<>();
        expected.add(Marble.BLUE);
        expected.add(Marble.BLUE);
        expected.add(Marble.WHITE);
        assertEquals(expected, structure.getColumn(1));
    }

    @Test
    public void getRow() {
        ArrayList<Marble> expected = new ArrayList<>();
        expected.add(Marble.BLUE);
        expected.add(Marble.GREY);
        expected.add(Marble.BLUE);
        expected.add(Marble.YELLOW);
        assertEquals(expected, structure.getRow(1));
    }

    @Test
    public void shiftColumn() {
    }

    @Test
    public void shiftRow() {
//        List<Marble> oldRow = structure.getRow(1);
//        System.out.println(oldRow);
//        structure.shiftRow(1);
//        System.out.println(oldRow);
//        oldRow = structure.getRow(1);
//        System.out.println(oldRow);
//        ArrayList<Marble> expected = new ArrayList<>();
//        expected.add(Marble.BLUE);
//        expected.add(Marble.GREY);
//        expected.add(Marble.BLUE);
//        expected.add(Marble.YELLOW);
//        assertEquals(expected, oldRow);
//        assertEquals(structure.getExtraMarble(), Marble.BLUE);
    }
}