package board;

import javafx.geometry.Pos;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jonni on 2/24/2017.
 */
public class Evaluator {

    /* Win/lose values are set to really high numbers but still less then P_INF / N_INF */
    public static final int WIN_VALUE = 2000000000;
    public static final int LOSE_VALUE = -WIN_VALUE;
    public static final int DRAW_VALUE = 0;

    /* Compare values for terminal checks */
    public static final int LOST = 2;
    public static final int DRAW = 1;
    public static final int NON_TERMINAL = 0;

    /**
     * Checks if a state is a terminal state for white. The return value
     * needs to be compared to terminal check values.
     *
     * @param state State
     * @param rules Rules
     * @return int
     */
    public static int whiteTerminalCheck(State state, Rules rules) {
        // Has priority over draw
        for (Position p : state.getBlack()) {
            if (p.getY() == 1) return LOST;
        }

        for (Position p : state.getWhite()) {
            // So this is not run multiple times

            Position[] possible = p.frontOfWhite();
            if (
                    state.getBlack().contains(possible[0]) ||
                    state.getBlack().contains(possible[2]) ||
                    (!state.getWhite().contains(possible[1]) && !state.getBlack().contains(possible[1]))
            ) {
                return NON_TERMINAL;
            }

        }
        // If neither has won, it is a draw if white can't move, otherwise a non terminal state.
        return DRAW;
    }

    /**
     * Checks if a state is a terminal state for black. The return value
     * needs to be compared to terminal check values.
     *
     * @param state State
     * @param rules Rules
     * @return int
     */
    public static int blackTerminalCheck(State state, Rules rules) {
        for (Position p : state.getWhite()) {
            if (p.getY() == rules.height) return LOST;
        }
        for (Position p : state.getBlack()) {
            Position[] possible = p.frontOfBlack();
            if (
                    state.getWhite().contains(possible[0]) ||
                    state.getWhite().contains(possible[2]) ||
                    (!state.getWhite().contains(possible[1]) && !state.getBlack().contains(possible[1]))
            ) {
                return NON_TERMINAL;
            }
        }
        return DRAW;
    }


    private int countValue;
    private int furthestValue;
    private int movableValue;
    private int unhinderedValue;
    private int laneControlValue;
    private int winNextValue;

    /**
     * Constructor for evaluation.
     *
     * @param countValue int (number of pawns)
     * @param furthestValue int (most advanced pawn)
     * @param movableValue int (number of pawns that can move)
     * @param unhinderedValue int (number of pawns such that they have a clear path to a goal state, times their advancement)
     * @param laneControlValue int (number of columns controlled)
     * @param rules rules
     */
    public Evaluator(int countValue,
                     int furthestValue,
                     int movableValue,
                     int unhinderedValue,
                     int laneControlValue,
                     Rules rules) {
        this.setValues(countValue, furthestValue, movableValue, unhinderedValue, laneControlValue, rules);
    }

    private int calculateWinNextValue(Rules rules) {
        return countValue * rules.width * 2
                        + furthestValue * rules.height
                        + movableValue * rules.width * 2
                        + unhinderedValue * rules.width * (2 * rules.height - 3)
                        + laneControlValue * rules.width
                        + 1;
    }

    public void setValues(int countValue,
                          int furthestValue,
                          int movableValue,
                          int unhinderedValue,
                          int laneControlValue,
                          Rules rules) {
        this.countValue = countValue;
        this.furthestValue = furthestValue;
        this.movableValue = movableValue;
        this.unhinderedValue = unhinderedValue;
        this.laneControlValue = laneControlValue;
        // Always set so that it is greater than the max of others combined.
        this.winNextValue = calculateWinNextValue(rules);
    }

    /**
     * The heuristic for a white-to-play state.
     *
     * @param state State
     * @param rules Rules
     * @return int
     */
    public int whiteHeuristic(State state, Rules rules) {
        return whiteBoardValue(state, rules, true) - blackBoardValue(state, rules, false);
    }

    /**
     * The heuristic for a black-to-play state.
     *
     * @param state State
     * @param rules Rules
     * @return int
     */
    public int blackHeuristic(State state, Rules rules) {
        return blackBoardValue(state, rules, true) - whiteBoardValue(state, rules, false);
    }

    /**
     * Evaluation of a state for white. The only thing that whiteToPlay affects is the
     * value of winNext. If it is his turn, we double it so that a state where both
     * players winNext is dominated by the player who's turn it is.
     *
     * @param state State
     * @param rules Rules
     * @param whiteToPlay boolean
     * @return int
     */
    public int whiteBoardValue(State state, Rules rules, boolean whiteToPlay) {
        int count = state.getWhite().size();
        Set<Integer> controlled = new HashSet<>();
        int furthest = 0;
        int canMove = 0;
        int unhindered = 0;
        int winNext = 0;

        Position bPawnL = null;
        Position bPawnR = null;

        for (Position pawn : state.getWhite()) {
            furthest = Math.max(furthest, pawn.getY());
            controlled.add(pawn.getX());

            Position[] movable = pawn.frontOfWhite();

            if (state.getBlack().contains(movable[0])) { // If left attack
                canMove++;
                if (pawn.getY() == rules.height - 1) {
                    if (whiteToPlay) {
                        winNext += 2;
                    } else {
                        winNext += 1;
                        bPawnL = new Position(pawn.getX() - 1, pawn.getY() + 1);
                    }
                }
            } else if (state.getBlack().contains(movable[2])) { // If right attack
                canMove++;
                if (pawn.getY() == rules.height - 1) {
                    if (whiteToPlay) {
                        winNext += 2;
                    } else {
                        winNext += 1;
                        bPawnR = new Position(pawn.getX() + 1, pawn.getY() + 1);
                    }
                }
            } else if (!state.getWhite().contains(movable[1]) && !state.getBlack().contains(movable[1])) {
                canMove++;
                if (pawn.getY() == rules.height - 1) winNext += 2;
                if (checkWhiteForward(pawn, rules, state)) unhindered += pawn.getY();
            }
        }

        /*// For Debugging
        System.out.println(count);
        System.out.println(furthest);
        System.out.println(canMove);
        System.out.println(unhindered);
        System.out.println(controlled.size());
        System.out.println(winNext);
        */

        // Special case of where black in middle could kill or go forward if his turn,
        // which results in a state where white does not win next.
        // BBB
        // W.W
        if (winNext == 2 && bPawnL != null && bPawnR != null && bPawnL.equals(bPawnR)) {
            winNext = 0;
        }

        return count * this.countValue +
                furthest * this.furthestValue +
                canMove * this.movableValue +
                unhindered * this.unhinderedValue +
                controlled.size() * this.laneControlValue +
                ((winNext > 1) ? (whiteToPlay ? 2 : 1) : 0) * this.winNextValue;

    }

    /**
     * Evaluation of a state for black. The only thing that blackToPlay affects is the
     * value of winNext. If it is his turn, we double it so that a state where both
     * players winNext is dominated by the player who's turn it is.
     *
     * @param state State
     * @param rules Rules
     * @param blackToPlay boolean
     * @return int
     */
    public int blackBoardValue(State state, Rules rules, boolean blackToPlay) {
        int count = state.getBlack().size();
        Set<Integer> controlled = new HashSet<>();
        int furthest = rules.height + 1;
        int canMove = 0;
        int unhindered = 0;
        int winNext = 0;

        Position wPawnL = null;
        Position wPawnR = null;

        for (Position pawn : state.getBlack()) {
            furthest = Math.min(furthest, pawn.getY());
            controlled.add(pawn.getX());

            Position[] movable = pawn.frontOfBlack();
            if (state.getWhite().contains(movable[0])) {
                canMove++;
                if (pawn.getY() == 2) {
                    if (blackToPlay) {
                        winNext += 2;
                    } else {
                        winNext += 1;
                        wPawnL = new Position(pawn.getX() - 1, pawn.getY() - 1);
                    }
                }
            } else if (state.getWhite().contains(movable[2])) {
                canMove++;
                if (pawn.getY() == 2) {
                    if (blackToPlay) {
                        winNext += 2;
                    } else {
                        winNext += 1;
                        wPawnR = new Position(pawn.getX() + 1, pawn.getY() - 1);
                    }
                }
            } else if (!state.getWhite().contains(movable[1]) && !state.getBlack().contains(movable[1])) {
                canMove++;
                if (pawn.getY() == 2) winNext += 2;
                if (checkBlackForward(pawn, rules, state)) unhindered += rules.height + 1 - pawn.getY();
            }
        }
        furthest = rules.height + 1 - furthest;

        /*// For Debugging
        System.out.println(count);
        System.out.println(furthest);
        System.out.println(canMove);
        System.out.println(unhindered);
        System.out.println(controlled.size());
        System.out.println(winNext);
        */

        // Special case of where white in middle could kill or go forward if his turn,
        // which results in a state where black does not win next.
        // B.B
        // WWW
        if (winNext == 2 && wPawnL != null && wPawnR != null && wPawnL.equals(wPawnR)) {
            winNext = 0;
        }

        return count * this.countValue +
                furthest * this.furthestValue +
                canMove * this.movableValue +
                unhindered * this.unhinderedValue +
                controlled.size() * this.laneControlValue +
                ((winNext > 1) ? (blackToPlay ? 2 : 1) : 0) * this.winNextValue;
    }

    /**
     * Returns true if non of the columns x, x-1, x+1 have a piece on them from pawns y position + 1 and upwards.
     *
     * @param pawn Position
     * @param rules Rules
     * @param state State
     * @return boolean
     */
    private boolean checkWhiteForward(
            Position pawn,
            Rules rules,
            State state
    ) {
        return (whiteEmptyLane(pawn.getX(), pawn.getY(), rules, state) &&
                whiteEmptyLane(pawn.getX() - 1, pawn.getY(), rules, state) &&
                whiteEmptyLane(pawn.getX() + 1, pawn.getY(), rules, state));
    }

    /**
     * Check if path from position is clear.
     *
     * @param x int
     * @param y int
     * @param rules Rules
     * @param state State
     * @return boolean
     */
    private boolean whiteEmptyLane(int x, int y, Rules rules, State state) {
        if (x < 1 || x > rules.width) return true;
        for (int i = y + 1; i <= rules.height; i++) {
            Position p = new Position(x, i);
            if (state.getBlack().contains(p) || state.getWhite().contains(p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Identical to white version a part from going downwards.
     *
     * @param pawn Position
     * @param rules Rules
     * @param state State
     * @return boolean
     */
    private boolean checkBlackForward(
            Position pawn,
            Rules rules,
            State state
    ) {
        return (blackEmptyLane(pawn.getX(), pawn.getY(), rules, state) &&
                blackEmptyLane(pawn.getX()-1, pawn.getY(), rules, state) &&
                blackEmptyLane(pawn.getX()+1, pawn.getY(), rules, state));
    }

    /**
     * This is identical to its white counterpart beside checking downwards.
     *
     * @param x int
     * @param y int
     * @param rules Rules
     * @param state State
     * @return boolean
     */
    private boolean blackEmptyLane(int x, int y, Rules rules, State state) {
        if (x < 1 || x > rules.width) return true;
        for (int i = y - 1; i >= 1; i--) {
            Position p = new Position(x, i);
            if (state.getBlack().contains(p) || state.getWhite().contains(p)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "count: " + this.countValue + "\n" +
                "furthest: " + this.furthestValue + "\n" +
                "movable: " + this.movableValue + "\n" +
                "unhindered: " + this.movableValue + "\n" +
                "lane control: " + this.laneControlValue + "\n" +
                "nextWin: " + this.winNextValue;
    }
}