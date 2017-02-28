package board;

import agents.Status;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Jonni on 2/19/2017.
 */
public class ActionsTest {

    private State state;
    private Set<State> expectedStates;

    @Before
    public void setUp() throws Exception {
        this.state = cleanState(5, 5);
        this.expectedStates = new HashSet<>();
    }




    @Test
    public void orderedWhiteTest() {
        this.state.getWhite().add(new Position(3,3));
        this.state.getWhite().add(new Position(1,1));
        this.state.getWhite().add(new Position(2,2));
        this.state.getWhite().add(new Position(4,2));
        this.state.getWhite().add(new Position(4,4));
        this.state.getWhite().add(new Position(3,1));
        Queue<Move> pq = Actions.getOrderedByFurthestWhiteActions(state);
        int last = Integer.MAX_VALUE;
        while (!pq.isEmpty()) {
            int current = pq.poll().getTo().getY();
            assertTrue(last >= current);
            last = current;
        }
    }

    @Test
    public void orderedBlackTest() {
        this.state.getBlack().add(new Position(4,4));
        this.state.getWhite().add(new Position(3,3));
        this.state.getBlack().add(new Position(3,4));
        this.state.getBlack().add(new Position(2,2));
        this.state.getBlack().add(new Position(1,5));
        Queue<Move> pq = Actions.getOrderedByFurthestBlackActions(state);
        int last = Integer.MIN_VALUE;
        while (!pq.isEmpty()) {
            int current = pq.poll().getTo().getY();
            assertTrue(last <= current);
            last = current;
        }
    }

    @Test
    public void classTest() {
        // For the coverage ;)
        assertEquals(Actions.class, new Actions().getClass());
    }

    @Test
    public void successorStatesTest0() {
        // head to head stalemate
        this.state.getBlack().add(new Position(1,2));
        this.state.getWhite().add(new Position(1,1));

        Set<State> generatedState = new HashSet<>();
        for (Move m : Actions.getBlackActions(this.state)) {
            generatedState.add(State.createWhiteToMoveChild(state,m));
        }

        assertEquals(this.expectedStates, generatedState);
    }

    @Test
    public void successorStatesTest1() {
        // 1 pawn, no diagonal attacks, none blocking path

        this.state.getWhite().add(new Position(1,3));

        State s = cleanState(5, 5);
        s.getWhite().add(new Position(1, 4));
        this.expectedStates.add(s);

        Set<State> generatedState = new HashSet<>();
        for (Move m : Actions.getWhiteActions(this.state)) {
            generatedState.add(State.createBlackToMoveChild(state,m));
        }

        assertEquals(this.expectedStates, generatedState);
    }

    @Test
    public void successorStatesTest2() {
        // Two whites, one blocking the other. The blocked has a diagonal attack option.
        this.state.getWhite().add(new Position(1, 3));
        this.state.getWhite().add(new Position(1, 4));
        this.state.getBlack().add(new Position(2, 4));

        State s = cleanState(5, 5);
        s.getBlack().add(new Position(2, 4));
        s.getWhite().add(new Position(1, 5));
        s.getWhite().add(new Position(1, 3));
        this.expectedStates.add(s);
        s = cleanState(5, 5);
        s.getWhite().add(new Position(1,4));
        s.getWhite().add(new Position(2,4));
        this.expectedStates.add(s);

        Set<State> generatedState = new HashSet<>();
        for (Move m : Actions.getWhiteActions(this.state)) {
            generatedState.add(State.createBlackToMoveChild(state,m));
        }

        assertEquals(this.expectedStates, generatedState);
    }

    @Test
    public void successorStatesTest3() {
        // Two black pawns, one with all options and the other with none.
        this.state.getBlack().add(new Position(2,2));
        this.state.getBlack().add(new Position(5,5));
        this.state.getWhite().add(new Position(1,1));
        this.state.getWhite().add(new Position(3,1));
        this.state.getWhite().add(new Position(5,4));

        State s = cleanState(5,5);
        s.getBlack().add(new Position(2,1));
        s.getWhite().add(new Position(1,1));
        s.getWhite().add(new Position(3,1));
        s.getWhite().add(new Position(5,4));
        s.getBlack().add(new Position(5,5));
        this.expectedStates.add(s);

        s = cleanState(5,5);
        s.getBlack().add(new Position(1,1));
        s.getWhite().add(new Position(3,1));
        s.getWhite().add(new Position(5,4));
        s.getBlack().add(new Position(5,5));
        this.expectedStates.add(s);

        s = cleanState(5,5);
        s.getBlack().add(new Position(3,1));
        s.getWhite().add(new Position(1,1));
        s.getWhite().add(new Position(5,4));
        s.getBlack().add(new Position(5,5));
        this.expectedStates.add(s);

        Set<State> generatedState = new HashSet<>();
        for (Move m : Actions.getBlackActions(this.state)) {
            generatedState.add(State.createWhiteToMoveChild(state,m));
        }

        assertEquals(this.expectedStates, generatedState);
    }

    @Test
    public void successorStatesTest4() {
        this.state.getBlack().add(new Position(1,2));
        this.state.getBlack().add(new Position(2,2));
        this.state.getBlack().add(new Position(3,2));
        this.state.getWhite().add(new Position(2,1));

        Set<State> generatedState = new HashSet<>();
        for (Move m : Actions.getWhiteActions(this.state)) {
            generatedState.add(State.createBlackToMoveChild(state,m));
        }

        State s = cleanState(5,5);
        s.getBlack().add(new Position(3,2));
        s.getBlack().add(new Position(2,2));
        s.getWhite().add(new Position(1,2));
        this.expectedStates.add(s);
        s = cleanState(5,5);
        s.getBlack().add(new Position(1,2));
        s.getBlack().add(new Position(2,2));
        s.getWhite().add(new Position(3,2));
        this.expectedStates.add(s);

        assertEquals(this.expectedStates, generatedState);
    }

    private State cleanState(int w, int h) {
        State s = new State(new Rules(w, h, 5));
        s.getBlack().clear();
        s.getWhite().clear();
        return s;
    }
}