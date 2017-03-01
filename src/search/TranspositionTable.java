package search;

import board.State;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonni on 2/27/2017.
 */
public class TranspositionTable {

    public static final int MAX_ENTRIES = 50000;
    private static final int WHITE_INDEX = 0;
    private static final int BLACK_INDEX = 1;
    private Map<Integer, TableEntry[]> table;

    public TranspositionTable() {
        this.table = new HashMap<>();
    }

    public void clean() {
        this.table.clear();
    }
    public boolean isEmpty() {
        return this.table.isEmpty();
    }
    public int size() {
        return this.table.size();
    }

    public TableEntry findWhiteState(State state) {
        TableEntry[] entries = getEntries(state);
        if (entries == null) return null;
        return checkEntry(state, entries[WHITE_INDEX]);
    }

    public void addWhiteState(State state, int value, int depth) {
        int hash = state.hashCode() % MAX_ENTRIES;
        TableEntry[] entries = this.table.get(hash);
        if (entries == null) this.table.put(hash, new TableEntry[] { new TableEntry(state, depth, value), null });
        else if (entries[WHITE_INDEX] == null || (entries[WHITE_INDEX].depth < depth)) {
                entries[WHITE_INDEX] = new TableEntry(state, depth, value);
        }
    }

    public TableEntry findBlackState(State state) {
        TableEntry[] entries = getEntries(state);
        if (entries == null) return null;
        return checkEntry(state, entries[BLACK_INDEX]);
    }

    public void addBlackState(State state, int value, int depth) {
        int hash = state.hashCode() % MAX_ENTRIES;
        TableEntry[] entries = this.table.get(hash);
        if (entries == null) this.table.put(hash, new TableEntry[]{ null, new TableEntry(state, depth, value)});
        else if (entries[BLACK_INDEX] == null || entries[BLACK_INDEX].depth < depth) {
            entries[BLACK_INDEX] = new TableEntry(state, depth, value);
        }
    }

    private TableEntry[] getEntries(State state) {
        return this.table.get(state.hashCode() % MAX_ENTRIES);
    }
    private TableEntry checkEntry(State state, TableEntry entry) {
        if (entry == null) return null;
        if (entry.state.equals(state)) return entry;
        return null;
    }

}

