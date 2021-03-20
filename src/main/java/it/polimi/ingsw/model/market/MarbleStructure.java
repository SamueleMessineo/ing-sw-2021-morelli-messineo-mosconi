package it.polimi.ingsw.model.market;

import java.util.ArrayList;
import java.util.List;

public class MarbleStructure {
    private final ArrayList<Marble> marbles;
    private Marble extraMarble;

    public MarbleStructure() {
        this.marbles = new ArrayList<>();
        this.extraMarble = Marble.BLUE;
    }

    public List<Marble> getMarbles() {
        return marbles;
    }

    public Marble getExtraMarble() {
        return extraMarble;
    }

    public List<Marble> getColumn(int colIndex) {
        colIndex -= 1;
        List<Marble> column = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            column.add(marbles.get(colIndex + 4*i));
        }
        return column;
    }

    public List<Marble> getRow(int rowIndex) {
        rowIndex = (rowIndex - 1) * 4;
        return marbles.subList(rowIndex, rowIndex + 3);
    }

    public List<Marble> shiftColumn(int colIndex) {
        colIndex -= 1;
        Marble newExtraMarble = marbles.get(colIndex);
        List<Marble> oldColumn = getColumn(colIndex);

        for (int i = 0; i < 2; i++) {
            marbles.set(colIndex + (i * 4), marbles.get(colIndex + ((i + 1) * 4)));
        }

        marbles.set(colIndex + (2 * 4), extraMarble);
        extraMarble = newExtraMarble;

        return oldColumn;
    }

    public List<Marble> shiftRow(int rowIndex) {
        rowIndex = (rowIndex - 1) * 4;
        Marble newExtraMarble = marbles.get(rowIndex + 3);
        List<Marble> oldRow = getRow(rowIndex);

        for (int i = 0; i < 3; i++) {
            marbles.set(rowIndex + i, marbles.get(rowIndex + i + 1));
        }

        marbles.set(rowIndex + 3, extraMarble);
        extraMarble = newExtraMarble;

        return oldRow;
    }
}
