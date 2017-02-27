package board;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Jonni on 2/18/2017.
 */
public class StateTest {

    private State initState;
    private State advancedState;
    private Rules rules;

    @Before
    public void setUp() throws Exception {
        this.rules = new Rules(3,5,5);
        this.initState = new State(this.rules);

        this.advancedState = new State(this.rules);
        Set<Position> maxi = this.advancedState.getWhite();
        Set<Position> mini = this.advancedState.getBlack();
        maxi.clear();
        mini.clear();
        maxi.add(new Position(2, 4));
        mini.add(new Position(1, 3));
    }

    @Test
    public void gettersTest() {
        Set<Position> expectedWhite = new HashSet<>();
        expectedWhite.add(new Position(1, 1));
        expectedWhite.add(new Position(2, 1));
        expectedWhite.add(new Position(3, 1));
        expectedWhite.add(new Position(1, 2));
        expectedWhite.add(new Position(2, 2));
        expectedWhite.add(new Position(3, 2));
        Set<Position> expectedBlack = new HashSet<>();
        expectedBlack.add(new Position(1, 4));
        expectedBlack.add(new Position(2, 4));
        expectedBlack.add(new Position(3, 4));
        expectedBlack.add(new Position(1, 5));
        expectedBlack.add(new Position(2, 5));
        expectedBlack.add(new Position(3, 5));
        assertEquals("White", expectedWhite, this.initState.getWhite());
        assertEquals("Black", expectedBlack, this.initState.getBlack());
    }

    @Test
    public void equalsTest() {
        assertTrue("Equals",
                new State(new Rules(14,61,1))
                        .equals(new State(new Rules(14,61,1))));
        State expectedAdvancedState = new State(this.rules);
        Set<Position> maxi = expectedAdvancedState.getWhite();
        Set<Position> mini = expectedAdvancedState.getBlack();
        maxi.clear();
        mini.clear();
        maxi.add(new Position(2, 4));
        mini.add(new Position(1, 3));
        assertTrue("Equals (copy constructor)", this.advancedState.equals(expectedAdvancedState));

        State state = new State(this.rules);
        state.getWhite().clear();
        assertFalse("White", this.initState.equals(state));

        state = new State(this.rules);
        state.getBlack().clear();
        assertFalse("Black", this.initState.equals(state));

        state = new State(this.rules);
        state.getWhite().clear();
        state.getBlack().clear();
        assertFalse("White and black", this.initState.equals(state));
    }

    @Test
    public void hashCodeTest() {
        assertEquals(
                this.initState.getWhite().hashCode() * 31 + this.initState.getBlack().hashCode(),
                this.initState.hashCode()
        );
    }

    @Test
    public void toStringTest() {
        assertEquals("White: [(2,4)]\nBlack: [(1,3)]", this.advancedState.toString());
    }

}