package agents;

import board.Evaluator;
import board.Position;
import board.Rules;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jonni on 2/27/2017.
 */
public class StatusTest {

    private Status status;
    private Status statusBlack;

    @Before
    public void setUp() throws Exception {
        this.status = new Status(10, 10, 1, "white", new HeuristicValues(1,1,1,1,1));
        this.statusBlack = new Status(5,5,5,"black");
    }

    @Test
    public void testRole() {
        assertTrue(this.status.isWhite());
        assertFalse(this.statusBlack.isWhite());
    }

    @Test
    public void turnTest() {
        this.status.nextPlayer();
        this.statusBlack.nextPlayer();
        assertTrue(this.status.isMyTurn());
        assertFalse(this.statusBlack.isMyTurn());
        this.status.nextPlayer();
        this.statusBlack.nextPlayer();
        assertFalse(this.status.isMyTurn());
        assertTrue(this.statusBlack.isMyTurn());
    }

    @Test
    public void getterTest() {
        assertEquals(10, this.status.getRules().height);
        assertEquals(0,
                this.status.getEvaluator().whiteHeuristic(
                        this.status.getCurrentState(),
                        new Rules(10,10,1)
                ));
    }

    @Test
    public void updateTest() {
        this.status.nextPlayer();
        this.status.updateState(new int[]{3,2,3,3});
        assertTrue(this.status.getCurrentState().getWhite().contains(new Position(3,3)));
        assertFalse(this.status.getCurrentState().getWhite().contains(new Position(3,2)));
        this.status.nextPlayer();
        this.status.updateState(new int[]{4,4,3,3});
        assertTrue(this.status.getCurrentState().getBlack().contains(new Position(3,3)));
        assertFalse(this.status.getCurrentState().getBlack().contains(new Position(4,4)));
        assertFalse(this.status.getCurrentState().getWhite().contains(new Position(3,3)));
    }

    @Test
    public void timeLimitTest() {
        assertEquals(1 * 1000 - Rules.TIME_OFFSET, this.status.getTimeLimit());
        assertEquals(5 * 1000 - Rules.TIME_OFFSET, this.statusBlack.getTimeLimit());
    }


}