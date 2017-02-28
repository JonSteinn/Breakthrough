package board;

import agents.HeuristicValues;
import agents.Status;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jonni on 2/24/2017.
 */
public class EvaluatorTest {

    private State state;
    private Rules rules;

    @Before
    public void setUp() throws Exception {
        this.rules = new Rules(5,5,5);
        this.state = new State(this.rules);
    }

    @Test
    public void specialNotWinNextCaseWhite() {
        Status s = new Status(10, 10, 10, "white", new HeuristicValues(0,0,0,0,0));

        s.getCurrentState().getBlack().clear();
        s.getCurrentState().getWhite().clear();

        s.getCurrentState().getBlack().add(new Position(1,10));
        s.getCurrentState().getBlack().add(new Position(2, 10));
        s.getCurrentState().getBlack().add(new Position(3, 10));

        s.getCurrentState().getWhite().add(new Position(1,9));
        s.getCurrentState().getWhite().add(new Position(3,9));

        assertEquals(0, s.getEvaluator().blackHeuristic(s.getCurrentState(), s.getRules()));
    }

    @Test
    public void specialNotWinNextCaseBlack() {
        Status s = new Status(10, 10, 10, "black", new HeuristicValues(0,0,0,0,0));

        s.getCurrentState().getBlack().clear();
        s.getCurrentState().getWhite().clear();

        s.getCurrentState().getWhite().add(new Position(1,1));
        s.getCurrentState().getWhite().add(new Position(2, 1));
        s.getCurrentState().getWhite().add(new Position(3, 1));

        s.getCurrentState().getBlack().add(new Position(1,2));
        s.getCurrentState().getBlack().add(new Position(3,2));

        assertEquals(0, s.getEvaluator().whiteHeuristic(s.getCurrentState(), s.getRules()));
    }

    @Test
    public void reflectionTest() {
        for (int j = 0; j < 1000; j++) { // 1000 test from clean boards
            Rules rules = new Rules((int)(Math.random() * 8) + 3, (int)(Math.random() * 6) + 5, 1);
            Evaluator ev = new Evaluator( // Value don't matter for this test
                    new HeuristicValues(
                    1,
                    2,
                    3,
                    4,
                    5
                    ),
                    rules
            );

            State white = new State(rules);
            State black = new State(rules);
            white.getBlack().clear();
            white.getWhite().clear();
            black.getBlack().clear();
            black.getWhite().clear();

            for (int i = 0; i < (rules.width << 1); i++) {
                Position p = new Position((int) (Math.random() * rules.width + 1), (int) (Math.random() * (rules.height - 2) + 2));
                while (white.getBlack().contains(p) || white.getWhite().contains(p)) {
                    p = new Position((int) (Math.random() * rules.width + 1), (int) (Math.random() * (rules.height - 2) + 2));
                }
                if ((i & 1) == 1) {
                    white.getBlack().add(p);
                    black.getWhite().add(reflection(p, rules));
                } else {
                    white.getWhite().add(p);
                    black.getBlack().add(reflection(p, rules));
                }
                assertEquals(ev.whiteBoardValue(white, rules, true), ev.blackBoardValue(black, rules, true));
                assertEquals(ev.whiteHeuristic(white, rules), ev.blackHeuristic(black, rules));
            }
        }
    }

    private Position reflection(Position white, Rules rules) {
        return new Position(white.getX(), rules.height + 1 - white.getY());
    }


    @Test
    public void drawTest() {
        this.state.getBlack().clear();
        this.state.getWhite().clear();

        assertTrue(drawWhite());
        assertTrue(drawBlack());

        this.state.getWhite().add(new Position(3,3));
        assertFalse(drawWhite());
        assertTrue(drawBlack());
        this.state.getWhite().clear();

        this.state.getBlack().add(new Position(3,2));
        assertTrue(drawWhite());
        assertFalse(drawBlack());
        this.state.getBlack().clear();

        this.state.getWhite().add(new Position(3,3));
        this.state.getBlack().add(new Position(3,4));
        assertTrue(drawBlack());
        assertTrue(drawWhite());
        this.state.getBlack().clear();
        this.state.getWhite().clear();

        this.state.getWhite().add(new Position(3,3));
        this.state.getBlack().add(new Position(2,2));
        assertFalse(drawBlack());
        assertFalse(drawWhite());
        this.state.getBlack().clear();
        this.state.getWhite().clear();

        this.state.getWhite().add(new Position(2,2));
        this.state.getBlack().add(new Position(3,3));
        assertFalse(drawBlack());
        assertFalse(drawWhite());
        this.state.getBlack().clear();
        this.state.getWhite().clear();

        this.state.getWhite().add(new Position(4,1));
        this.state.getBlack().add(new Position(4,4));
        assertFalse(drawBlack());
        assertFalse(drawWhite());
        this.state.getBlack().clear();
        this.state.getWhite().clear();
    }

    private boolean drawWhite() {
        return Evaluator.whiteTerminalCheck(this.state, this.rules) == Evaluator.DRAW;
    }
    private boolean drawBlack() {
        return Evaluator.blackTerminalCheck(this.state, this.rules) == Evaluator.DRAW;
    }

    private boolean winWhite() {
        return Evaluator.blackTerminalCheck(this.state, this.rules) == Evaluator.LOST;
    }
    private boolean winBlack() {
        return Evaluator.whiteTerminalCheck(this.state, this.rules) == Evaluator.LOST;
    }

    @Test
    public void whiteGoal() {
        assertFalse(winWhite());
        this.state.getWhite().clear();
        this.state.getBlack().clear();
        assertFalse(winWhite());
        this.state.getWhite().add(new Position(1,4));
        assertFalse(winWhite());
        this.state.getWhite().add(new Position(2,5));
        assertTrue(winWhite());
    }

    @Test
    public void blackGoal() {
        assertFalse(winBlack());
        this.state.getWhite().clear();
        this.state.getBlack().clear();
        assertFalse(winBlack());
        this.state.getBlack().add(new Position(2,2));
        assertFalse(winBlack());
        this.state.getBlack().add(new Position(3,1));
        assertTrue(winBlack());
    }

    @Test
    public void changeValuesTest() {
        // Using to toString since does not have an equals method
        String expected = "count: 1\n" +
                "furthest: 2\n" +
                "movable: 3\n" +
                "unhindered: 4\n" +
                "lane control: 5\n" +
                "nextWin: 134";
        Evaluator ev = new Evaluator(new HeuristicValues(1,2,3,4,5), new Rules(3,5,5));
        assertEquals(expected, ev.toString());
        expected = "count: 5\n" +
                "furthest: 4\n" +
                "movable: 3\n" +
                "unhindered: 2\n" +
                "lane control: 1\n" +
                "nextWin: 176";
        ev.setValues(new HeuristicValues(5,4,3,2,1), new Rules(5,5,5));
        assertEquals(expected, ev.toString());
    }
}