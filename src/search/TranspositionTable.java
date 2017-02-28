package search;

import board.State;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonni on 2/27/2017.
 */
public class TranspositionTable {

    private static final int MAX_ENTRIES = 1000;
    private static final int WHITE_INDEX = 0;
    private static final int BLACK_INDEX = 1;
    private Map<Integer, TableEntry[]> table;

    public TranspositionTable() {
        this.table = new HashMap<>();
    }

    public TableEntry findState(State state, boolean white) {
        int hash = state.hashCode() % MAX_ENTRIES;
        TableEntry[] entries = this.table.get(hash);
        if (entries == null) {
            return null;
        }
        TableEntry entry = entries[white ? WHITE_INDEX : BLACK_INDEX];
        if (entry == null) return null;
        if (entry.state.equals(state)) {
            return entry;
        }
        return null;
    }

    public void addState(State state, int alpha, int beta, int depth, boolean white) {
        int hash = state.hashCode();
        TableEntry[] entries = this.table.get(hash);
        if (entries == null) {
            this.table.put(
                    hash,
                    white ? new TableEntry[] { new TableEntry(state, alpha, beta, depth), null } :
                            new TableEntry[] { new TableEntry(state, alpha, beta, depth), null }
            );
        } else {
            entries[white ? WHITE_INDEX : BLACK_INDEX] = new TableEntry(state, alpha, beta, depth);
        }
    }

}

