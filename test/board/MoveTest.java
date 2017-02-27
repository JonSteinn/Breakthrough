package board;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jonni on 2/19/2017.
 */
public class MoveTest {

    private Move move;
    private Move move2;

    @Before
    public void setUp() throws Exception {
        this.move = new Move(new Position(1,3), new Position(2,4));
        this.move2 = new Move(new int[]{ 11, 55, 11, 54 });
    }

    @Test
    public void getterTest() {
        assertEquals("From (positions)", new Position(1, 3), this.move.getFrom());
        assertEquals("To (positions)", new Position(2, 4), this.move.getTo());
        assertEquals("From (array)", new Position(11, 55), this.move2.getFrom());
        assertEquals("To (array)", new Position(11, 54), this.move2.getTo());
    }

    @Test
    public void toStringTest() {
        assertEquals("(move 1 3 2 4)", this.move.toString());
    }

    @Test
    public void hashTest() {
        int hashFrom = this.move.getFrom().hashCode();
        int hashTo = this.move.getTo().hashCode();
        assertEquals(31 * hashFrom + hashTo, this.move.hashCode());
    }

    @Test
    public void equalsTest() {
        assertTrue("Equals", this.move.equals(new Move(this.move.getFrom(), this.move.getTo())));
        assertFalse("Not equals (from)", this.move.equals(new Move(new int[]{ 2,3,2,4 })));
        assertFalse("Not equals (to)", this.move.equals(new Move(new int[]{ 1,3,1,4 })));
        assertFalse("Not equals (from, to)", this.move.equals(new Move(new int[]{ 2,3,3,4 })));
    }
}