package search;

import agents.Status;
import board.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Jonni on 2/25/2017.
 */
public class AlphaBetaTest {

    @Test
    public void timeTest() {
        long t = System.currentTimeMillis();
        Status s = new Status(5,5,2, "white",
                5,10,15,25,5);
        new AlphaBeta(s).getBestMove();
        assertTrue(System.currentTimeMillis() - t < 2000L);

        t = System.currentTimeMillis();
        s = new Status(10,10,10, "white",
                5,10,15,25,5);
        new AlphaBeta(s).getBestMove();
        assertTrue(System.currentTimeMillis() - t < 10000L);
    }

    @Test
    public void simpleBestMoveTest() {
        Status s = new Status(5, 5, 1, "white",
                5,10,15,25,5);
        s.getCurrentState().getWhite().clear();
        s.getCurrentState().getBlack().clear();

        s.getCurrentState().getWhite().add(new Position(2,2));
        s.getCurrentState().getWhite().add(new Position(5,2));
        s.getCurrentState().getBlack().add(new Position(3,3));

        assertEquals(new Move(new int[]{2,2,3,3}).toString(), new AlphaBeta(s).getBestMove());

        s = new Status(5,5,1,"black",
                5,10,15,25,5);

        s.getCurrentState().getWhite().clear();
        s.getCurrentState().getBlack().clear();

        s.getCurrentState().getBlack().add(reflection(new Position(2,2), s.getRules()));
        s.getCurrentState().getBlack().add(reflection(new Position(5,2), s.getRules()));
        s.getCurrentState().getWhite().add(reflection(new Position(3,3), s.getRules()));

        assertEquals(
                new Move(reflection(new Position(2,2), s.getRules()),
                        reflection(new Position(3,3), s.getRules())).toString(),
                new AlphaBeta(s).getBestMove()
        );

    }

    @Test
    public void drawOverLossTest() {
        Status s = new Status(3,5,5, "white",
                5,10,15,25,5);
        s.getCurrentState().getWhite().clear();
        s.getCurrentState().getBlack().clear();
        s.getCurrentState().getWhite().add(new Position(2,2));
        s.getCurrentState().getBlack().add(new Position(3,3));
        s.getCurrentState().getBlack().add(new Position(3,5));
        Position from = new Position(2,2), to = new Position(3,3);
        Move expected = new Move(from, to);
        assertEquals(expected.toString(), new AlphaBeta(s).getBestMove());

        Status s2 = new Status(3,5,5, "black",
                5,10,15,25,5);
        reflectionState(s, s2);
        assertEquals(new Move(reflection(from, s2.getRules()), reflection(to,s2.getRules())).toString(),
                new AlphaBeta(s2).getBestMove());
    }

    @Test
    public void reflectionTest() {
        Status sW = new Status(10,10,1, "white",
                5,10,15,25,5);
        Status sB = new Status(10,10,1, "black",
                5,10,15,25,5);
        Move mW = parseMove(new AlphaBeta(sW).getBestMove());
        Move mB = parseMove(new AlphaBeta(sB).getBestMove());
        assertEquals(mW, new Move(diagonalReflection(mB.getFrom(),sB.getRules()), diagonalReflection(mB.getTo(), sB.getRules())));
    }

    @Test
    public void calledForNoMoveState() {
        Status sW = new Status(10,10,1,"white",
                 5,10,15,25,5);
        sW.getCurrentState().getBlack().clear();
        sW.getCurrentState().getWhite().clear();
        Status sB = new Status(10,10,1,"black",
                5,10,15,25,5);
        sB.getCurrentState().getBlack().clear();
        sB.getCurrentState().getWhite().clear();

        assertEquals(Status.NO_MOVE, new AlphaBeta(sW).getBestMove());
        assertEquals(Status.NO_MOVE, new AlphaBeta(sB).getBestMove());

        sW.getCurrentState().getBlack().add(new Position(4,4));
        sW.getCurrentState().getWhite().add(new Position(4,3));
        sB.getCurrentState().getBlack().add(new Position(4,4));
        sB.getCurrentState().getWhite().add(new Position(4,3));

        assertEquals(Status.NO_MOVE, new AlphaBeta(sW).getBestMove());
        assertEquals(Status.NO_MOVE, new AlphaBeta(sB).getBestMove());
    }

    @Test
    public void timeFailTest() {
        Status sW = new Status(5,5,0,"white",
                5,10,15,25,5);
        Status sB = new Status(5,5,0,"black",
                5,10,15,25,5);

        ArrayList<Move> whiteMoves = Actions.getWhiteActions(sW.getCurrentState());
        ArrayList<Move> blackMoves = Actions.getBlackActions(sB.getCurrentState());

        assertEquals(whiteMoves.get(0).toString(), new AlphaBeta(sW).getBestMove());
        assertEquals(blackMoves.get(0).toString(), new AlphaBeta(sB).getBestMove());
    }


    private void reflectionState(Status s1, Status s2) {
        s2.getCurrentState().getWhite().clear();
        s2.getCurrentState().getBlack().clear();
        for (Position p : s1.getCurrentState().getWhite()) {
            s2.getCurrentState().getBlack().add(reflection(p, s2.getRules()));
        }
        for (Position p : s1.getCurrentState().getBlack()) {
            s2.getCurrentState().getWhite().add(reflection(p, s2.getRules()));
        }
    }

    private Position reflection(Position white, Rules rules) {
        return new Position(white.getX(), rules.height + 1 - white.getY());
    }

    private Position diagonalReflection(Position pos, Rules rules) {
        return new Position(rules.width + 1 - pos.getX(), rules.height + 1 - pos.getY());
    }

    private Move parseMove(String command) {
        int[] arr = new int[4];
        String[] sArr = command.replace("(move ","").replace(")","").split(" ");
        for (int i = 0; i < 4; i++) {
            arr[i] = Integer.parseInt(sArr[i]);
        }
        return new Move(arr);
    }
}