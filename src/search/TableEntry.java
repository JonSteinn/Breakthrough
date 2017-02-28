package search;

import board.State;

/**
 * Created by Jonni on 2/27/2017.
 */
public class TableEntry {
    public State state;
    public int depth;
    public int alpha;
    public int beta;
    public boolean player;
    public TableEntry(State state, int alpha, int beta, int depth) {
        this.state = state;
        this.depth = depth;
        this.alpha = alpha;
        this.beta = beta;
    }
}