package search;

import board.Position;
import board.Rules;
import board.State;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Jonni on 2/28/2017.
 */
public class TranspositionTableTest {

    private Random rand;
    private TranspositionTable tTable;

    @Before
    public void setUp() throws Exception {
        this.tTable = new TranspositionTable();
        this.rand = new Random();
    }

    @Test
    public void addWhiteTest() {
        State s = new State(new Rules(5,5,5));
        s.getBlack().clear();
        s.getWhite().clear();
        s.getWhite().add(new Position(2,2));
        s.getBlack().add(new Position(2,3));
        this.tTable.addWhiteState(s, 3, 55);

        TableEntry te = this.tTable.findWhiteState(new State(s));

        assertEquals(new State(s), te.state);
        assertEquals(55, te.depth);
        assertEquals(3, te.value);

        this.tTable.addWhiteState(new State(s), 15, 555);

        te = this.tTable.findWhiteState(new State(s));

        assertEquals(new State(s), te.state);
        assertEquals(555, te.depth);
        assertEquals(15, te.value);
    }

    @Test
    public void addBlackTest() {
        State s = new State(new Rules(26,26,5));
        s.getBlack().clear();
        s.getWhite().clear();
        s.getWhite().add(new Position(20,21));
        s.getBlack().add(new Position(5,3));
        this.tTable.addBlackState(s, -1234, 2);

        TableEntry te = this.tTable.findBlackState(new State(s));

        assertEquals(new State(s), te.state);
        assertEquals(2, te.depth);
        assertEquals(-1234, te.value);

        this.tTable.addWhiteState(new State(s), -13523, 15);

        te = this.tTable.findWhiteState(new State(s));

        assertEquals(new State(s), te.state);
        assertEquals(15, te.depth);
        assertEquals(-13523, te.value);
    }

    @Test
    public void clearTest() {
        this.tTable.addWhiteState(new State(new Rules(1,1,1)),5,5);
        this.tTable.addBlackState(new State(new Rules(1,1,1)),5,5);
        assertFalse(this.tTable.isEmpty());
        this.tTable.clean();
        assertTrue(this.tTable.isEmpty());
    }

    private boolean done;
    @Test
    public void collisionTesting() {

        // Gives you a <timeLimit> seconds to find non equals states with same index.
        long timeLimit = 1000; // <-- increase if fails or skip test

        done = false;
        long time = System.currentTimeMillis();
        Thread experiment = new Thread(() -> {
            State a;
            State b;
            while (true) {
                a = new State(new Rules(25, 25, 0));
                b = new State(new Rules(25, 25, 0));
                a.getBlack().clear();
                b.getWhite().clear();
                if (fillStates(a,b)) {
                    break;
                }
            }

            tTable.addBlackState(b, 5, 12);
            assertEquals(null,tTable.findBlackState(a));

            tTable.addWhiteState(a, 14, 30);
            assertEquals(null, tTable.findWhiteState(b));

            tTable.addBlackState(a, 123, 51);
            assertEquals(a, tTable.findBlackState(a).state);

            tTable.addWhiteState(b, 135, 1124);
            assertEquals(b, tTable.findWhiteState(b).state);

            done = true;
        });
        experiment.start();

        while (!done && (System.currentTimeMillis() - time) < timeLimit) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!done) {
            System.out.println("Timed out");
            experiment.interrupt();
            assertTrue("Increase time limit and try again", false);
        } else {
            try {
                experiment.join();
                System.out.println("Experiment successfully finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean fillStates(State a, State b) {
        for (int i = 0; i < 1000; i++) {
            if (i % 4 == 0) {
                a.getWhite().add(new Position( rand.nextInt(25) + 1,  rand.nextInt(25) + 1));
            } else if (i % 4 == 1) {
                b.getWhite().add(new Position( rand.nextInt(25) + 1,  rand.nextInt(25) + 1));
            } else if (i % 4 == 2) {
                a.getBlack().add(new Position( rand.nextInt(25) + 1,  rand.nextInt(25) + 1));
            } else {
                b.getBlack().add(new Position( rand.nextInt(25) + 1,  rand.nextInt(25) + 1));
            }
            if (!a.equals(b) &&
                    ((a.hashCode() % TranspositionTable.MAX_ENTRIES) == (b.hashCode() % TranspositionTable.MAX_ENTRIES))) {
                return true;
            }
        }
        return false;
    }

}