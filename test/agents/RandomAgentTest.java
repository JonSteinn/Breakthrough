package agents;

import board.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Jonni on 2/19/2017.
 */
public class RandomAgentTest {

    private Agent agent1;
    private Agent agent2;

    @Before
    public void setUp() throws Exception {
        this.agent1 = new RandomAgent();
        this.agent2 = new RandomAgent();
        this.agent1.cleanup();
        this.agent2.cleanup();
        this.agent1.init("white", 3, 5, 20000);
        this.agent2.init("black", 3, 5, 20000);
    }

    @Test
    public void startTest() {
        State init = new State(new Rules(3,5,1000));

        for (int i = 0; i < 100; i++) {
            assertTrue(
                    Integer.toString(i),
                    Actions.getWhiteActions(init).contains(new Move(parseMove(this.agent1.nextAction(null))))
            );
            this.agent1.init("white", 3, 5, 20000);
        }
    }

    @Test
    public void threeMovesTest() {
        State state = new State(new Rules(3,5,5));
        for (int i = 0; i < 100; i++) {
            boolean next = true;
            int[] nextMove = null;
            for (int j = 0; i < 3; i++) {
                nextMove = updateMove(state, next, this.agent1, this.agent2, nextMove);
                next = !next;
            }
        }
    }

    @Test
    public void playUntilOverTest () {
        for (int i = 0; i < 25; i++) {
            playUntilOver(3, 5, this.agent1, this.agent2);
            this.agent1.init("white", 3, 5, 20000);
            this.agent2.init("black", 3, 5, 20000);
        }
    }

    @Test
    public void playUntilOverTest2() {
        for (int i = 0; i < 1000; i++) {
            int w = (int)(Math.random() * 14) + 1;
            int h = (int)(Math.random() * 10) + 5;
            Agent max = new RandomAgent(), min = new RandomAgent();
            max.init("white", w, h, 20000);
            min.init("black", w, h, 20000);
            playUntilOver(w, h, max, min);
        }
    }

    private void playUntilOver(int width, int height, Agent white, Agent black) {
        Rules r = new Rules(width, height, 1);
        State state = new State(r);
        boolean maximizer = true;
        int[] nextMove = null;
        while ( maximizer ? (Evaluator.whiteTerminalCheck(state, r) == 0) : (Evaluator.blackTerminalCheck(state, r) == 0)) {
            nextMove = updateMove(state, maximizer, white, black, nextMove);
            if (maximizer) {
                state.whiteMove(new Move(nextMove));
            } else {
                state.blackMove(new Move(nextMove));
            }
            maximizer = !maximizer;
        }
    }

    private int[] updateMove(State state, boolean whiteToPlay, Agent white, Agent black, int[] m) {
        String w = white.nextAction(m);
        String b = black.nextAction(m);
        return whiteToPlay ? parseMove(w) : parseMove(b);
    }

    private int[] parseMove(String command) {
        int[] arr = new int[4];
        String[] sArr = command.replace("(move ","").replace(")","").split(" ");
        for (int i = 0; i < 4; i++) {
            arr[i] = Integer.parseInt(sArr[i]);
        }
        return arr;
    }
}