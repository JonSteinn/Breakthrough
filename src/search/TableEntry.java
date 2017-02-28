package search;

import board.State;

/**
 * Created by Jonni on 2/27/2017.
 */
public class TableEntry {

    public enum EntryType {
        ALPHA, BETA, BEST
    }

    public State state;
    public int depth;
    public int value;
    public EntryType type;

    public TableEntry(State state, int depth, int value, EntryType type) {
        this.state = state;
        this.depth = depth;
        this.value = value;
        this.type = type;
    }
}