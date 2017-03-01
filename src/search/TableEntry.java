package search;

import board.State;

/**
 * Created by Jonni on 2/27/2017.
 */
public class TableEntry {

    public State state;
    public int depth;
    public int value;

    public TableEntry(State state, int depth, int value) {
        this.state = state;
        this.depth = depth;
        this.value = value;
    }
}