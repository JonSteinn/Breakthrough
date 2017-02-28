package search;

import board.Rules;
import board.State;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jonni on 2/28/2017.
 */
public class TableEntryTest {

    private TableEntry tEntry;

    @Before
    public void setUp() throws Exception {
        this.tEntry = new TableEntry(new State(new Rules(5,5,5)), 20, -5, TableEntry.EntryType.ALPHA);
    }

    @Test
    public void entryTest() {
        assertEquals(new State(new Rules(5,5,5)), this.tEntry.state);
        assertEquals(20, this.tEntry.depth);
        assertEquals(-5, this.tEntry.value);
    }

}