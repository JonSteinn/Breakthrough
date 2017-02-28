package agents;

import board.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Jonni on 2/27/2017.
 */
public class MyAgentTest {

    private Agent random;
    private Agent smart;
    private Random generator;

    @Before
    public void setUp() throws Exception {
        this.generator = new Random();
        this.random = new RandomAgent();
        this.smart = new MyAgent();
        this.random.cleanup();
        this.smart.cleanup();
    }

    @Test
    public void getterTest() {
        Agent a = new MyAgent();
        Agent b = new MyAgent();
        a.init("white", 5, 5, 5);
        b.init("black", 10, 20, 30);
        assertTrue(((MyAgent)a).getStatus().isWhite());
        assertEquals(5 * 1000 - Rules.TIME_OFFSET, ((MyAgent)a).getStatus().getTimeLimit());
        assertFalse(((MyAgent)b).getStatus().isWhite());
    }

    @Test // This takes a while!
    public void testEstimates() {
        //////////////////////////////////
        if (false) { // <--- remove to run (or set as true)
        //////////////////////////////////
            Agent agent1 = new MyAgent();
            Agent agent2 = new MyAgent();
            // The agent's architecture isn't that well built for testing, this is hardcoded. Change to whatever values he has.
            int[] values1 = new int[]{101, 257, 432, 290, 447};
            int[] values2 = new int[]{101, 257, 432, 290, 447};
            boolean agent2toRandomize = true;
            for (int round = 0; round < 10; round++) {
                setValues(values1, values2, agent2toRandomize);
                int wins1 = 0;
                int wins2 = 0;
                Rules rules = new Rules(5, 5, 1);
                for (int i = 0; i < 3; i++) {
                    boolean agent1IsWhite = Math.random() < 0.5;
                    if (agent1IsWhite) {
                        agent1.init("white", 5, 5, 1);
                        agent2.init("black", 5, 5, 1);
                        ((MyAgent) agent1).getStatus().getEvaluator().setValues(new HeuristicValues(values1[0], values1[1], values1[2], values1[3], values1[4]), rules);
                        ((MyAgent) agent2).getStatus().getEvaluator().setValues(new HeuristicValues(values2[0], values2[1], values2[2], values2[3], values2[4]), rules);
                        if (playUntilOver(agent1, agent2, rules) > 0) {
                            wins1++;
                        } else {
                            wins2++;
                        }
                    } else {
                        agent1.init("black", 5, 5, 1);
                        agent2.init("white", 5, 5, 1);
                        ((MyAgent) agent1).getStatus().getEvaluator().setValues(new HeuristicValues(values1[0], values1[1], values1[2], values1[3], values1[4]), rules);
                        ((MyAgent) agent2).getStatus().getEvaluator().setValues(new HeuristicValues(values2[0], values2[1], values2[2], values2[3], values2[4]), rules);
                        if (playUntilOver(agent2, agent1, rules) < 0) {
                            wins1++;
                        } else {
                            wins2++;
                        }
                    }
                }
                if (wins1 > wins2) {
                    System.out.println("AGENT 1 WINS!");
                    System.out.println("Win:\n" + ((MyAgent) agent1).getStatus().getEvaluator());
                    System.out.println("--------------------");
                    System.out.println("Lost:\n" + ((MyAgent) agent2).getStatus().getEvaluator());
                    System.out.println();
                    agent2toRandomize = true;
                } else if (wins2 > wins1) {
                    System.out.println("AGENT 2 WINS!");
                    System.out.println("Win:\n" + ((MyAgent) agent2).getStatus().getEvaluator());
                    System.out.println("--------------------");
                    System.out.println("Lost:\n" + ((MyAgent) agent1).getStatus().getEvaluator());
                    System.out.println();
                    agent2toRandomize = false;
                }
            }
        }
    }

    private void setValues(int[] v1, int[] v2, boolean a2toRan) {
        if (a2toRan) {
            randomizeValues(v2);
        } else {
            randomizeValues(v1);
        }
    }
    private void randomizeValues(int[] values) {
        values[0] = this.generator.nextInt(500) + 1;
        values[1] = this.generator.nextInt(500) + 1;
        values[2] = this.generator.nextInt(500) + 1;
        values[3] = this.generator.nextInt(500) + 1;
        values[4] = this.generator.nextInt(500) + 1;
    }

    @Test
    public void beatsRandomAsWhite3X5Test() {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            this.smart.init("white", 3, 5, 5);
            this.random.init("black", 3, 5, 5);
            Rules r = new Rules(3, 5, 5);
            sum += playUntilOver(this.smart, this.random, r);
        }
        assertTrue(sum > 7); // at most 1 loss or 2 draws, anything else is fail
        this.smart.cleanup();
        this.random.cleanup();
    }

    @Test // This takes a while!
    public void beatsRandomAsWhite9X9Test() {
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            this.smart.init("white", 9, 9, 1);
            this.random.init("black", 9, 9, 1);
            Rules r = new Rules(9, 9, 5);
            sum += playUntilOver(this.smart, this.random, r);
        }
        assertTrue(sum > 2); // should win all here
        this.smart.cleanup();
        this.random.cleanup();
    }


    @Test
    public void beatsRandomAsBlack3X5Test() {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            this.smart.init("black", 3, 5, 5);
            this.random.init("white", 3, 5, 5);
            Rules r = new Rules(3, 5, 5);
            sum += playUntilOver(this.random, this.smart, r);
        }
        assertTrue(sum < -7); // at most 1 loss or 2 draws, anything else is fail
        this.smart.cleanup();
        this.random.cleanup();
    }

    @Test // this takes a while!
    public void beatsRandomAsBlack9X9Test() {
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            this.smart.init("black", 9, 9, 1);
            this.random.init("white", 9, 9, 1);
            Rules r = new Rules(9, 9, 1);
            sum += playUntilOver(this.random, this.smart, r);
        }
        assertTrue(sum < -2); // should win all here
        this.smart.cleanup();
        this.random.cleanup();
    }

    private int playUntilOver(Agent white, Agent black, Rules rules) {
        State state = new State(rules);
        boolean whiteToPlay = true;
        int[] nextMove = null;
        while ( whiteToPlay ?
                (Evaluator.whiteTerminalCheck(state, rules) == 0) :
                (Evaluator.blackTerminalCheck(state, rules) == 0)
        ) {
            nextMove = updateMove(whiteToPlay, white, black, nextMove);
            if (whiteToPlay) {
                state.whiteMove(new Move(nextMove));
            } else {
                state.blackMove(new Move(nextMove));
            }
            ////////////////
            // DRAW STATE //
            ////////////////
            //LaTeX.drawState(state,rules, "", "", 0.5);
            whiteToPlay = !whiteToPlay;
        }
        return whiteWin(state, rules) ? 1 : (blackWin(state, rules) ? -1 : 0);
    }

    private boolean whiteWin(State s, Rules r) {
        for (Position pawn : s.getWhite()) {
            if (pawn.getY() == r.height) return true;
        }
        return false;
    }

    private boolean blackWin(State s, Rules r) {
        for (Position pawn : s.getBlack()) {
            if (pawn.getY() == 1) return true;
        }
        return false;
    }

    private int[] updateMove(boolean whiteToPlay, Agent white, Agent black, int[] m) {
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