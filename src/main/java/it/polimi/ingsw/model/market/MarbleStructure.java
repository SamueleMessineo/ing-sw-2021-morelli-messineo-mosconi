package it.polimi.ingsw.model.market;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the 12 marbles plus the extra marble used during a player's turn
 * to acquire resources by shifting a row or column.
 */
public class MarbleStructure implements Serializable {
    private final List<Marble> marbles;
    private Marble extraMarble;

    /**
     * MarbleStructure class constructor.
     */
    public MarbleStructure(List<Marble> marbles, Marble extraMarble) {
        this.marbles = marbles;
        this.extraMarble = extraMarble;
    }

    /**
     * Fetch all the marbles, not including the extra.
     * @return A list of the 12 marbles.
     */
    public List<Marble> getMarbles() {
        return marbles;
    }

    /**
     * Fetch the extra marble.
     * @return The extra marble.
     */
    public Marble getExtraMarble() {
        return extraMarble;
    }

    /**
     * Fetches a column corresponding to the given index.
     * @param colIndex the index of the column to get.
     * @return The list of marbles corresponding to the requested column.
     */
    public List<Marble> getColumn(int colIndex) {
        List<Marble> column = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            column.add(marbles.get(colIndex + 4*i));
        }
        return new ArrayList<>(column);
    }

    /**
     * Fetches a row corresponding to the given index.
     * @param rowIndex the index of the row to get.
     * @return The list of marbles corresponding to the requested row.
     */
    public List<Marble> getRow(int rowIndex) {
        rowIndex = (rowIndex) * 4;
        return new ArrayList<>(marbles.subList(rowIndex, rowIndex + 4));
    }

    /**
     * Takes the index of a column and shifts it using the
     * extra marble.
     * @param colIndex the index of the column to shift.
     * @return The original column before it gets shifted.
     */
    public List<Marble> shiftColumn(int colIndex) {
        List<Marble> oldColumn = getColumn(colIndex);
        Marble newExtraMarble = marbles.get(colIndex);

        for (int i = 0; i < 2; i++) {
            marbles.set(colIndex + (i * 4), marbles.get(colIndex + ((i + 1) * 4)));
        }

        marbles.set(colIndex + (2 * 4), extraMarble);
        extraMarble = newExtraMarble;

        return oldColumn;
    }

    /**
     * Takes the index of a row and shifts it using the
     * extra marble.
     * @param rowIndex the index of the row to shift.
     * @return The original row before it gets shifted.
     */
    public List<Marble> shiftRow(int rowIndex) {
        List<Marble> oldRow = getRow(rowIndex);
        rowIndex = (rowIndex) * 4;
        Marble newExtraMarble = marbles.get(rowIndex);

        for (int i = 0; i < 3; i++) {
            marbles.set(rowIndex + i, marbles.get(rowIndex + i + 1));
        }

        marbles.set(rowIndex + 3, extraMarble);
        extraMarble = newExtraMarble;
        return oldRow;
    }

    @Override
    public String toString() {
        String result = "Marble Structure: \n";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                result += marbles.get((i*4)+j).name() + " ";
            }
            result += "\n";
        }
        result += "Extra Marble: " + extraMarble.name();
        return result;
    }
}
