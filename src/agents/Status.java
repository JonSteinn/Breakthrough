package agents;

import board.Evaluator;
import board.Move;
import board.Rules;
import board.State;

/**
 * Created by Jonni on 2/24/2017.
 *
 * A class to keep track of a game's progress.
 */
public class Status {

    public final static String WHITE_NAME = "white";
    public final static String NO_MOVE = "noop";
    public final static int TIME_OFFSET = 40;

    private boolean myTurn;
    private boolean isWhite;
    private long timeLimit;
    private Rules rules;
    private State currentState;
    private Evaluator evaluator;

    /**
     * Constructor for Status without evaluation.
     *
     * @param w int (width)
     * @param h int (height)
     * @param time int (seconds)
     * @param role String
     */
    public Status(int w, int h, int time, String role) {
        this.rules = new Rules(w,h,time);
        this.currentState = new State(this.rules);
        this.isWhite = Status.WHITE_NAME.equals(role);
        this.myTurn = !this.isWhite;
        this.timeLimit = time * 1000 - Status.TIME_OFFSET;
    }

    /**
     *
     * @param w
     * @param h
     * @param time
     * @param role
     * @param countValue
     * @param furthestValue
     * @param movableValue
     * @param unhinderedValue
     * @param laneControlValue
     */
    public Status(int w, int h, int time, String role, int countValue, int furthestValue, int movableValue, int unhinderedValue, int laneControlValue) {
        this(w,h,time,role);
        this.evaluator = new Evaluator(
                countValue,
                furthestValue,
                movableValue,
                unhinderedValue,
                laneControlValue,
                rules
        );
    }

    /**
     * Updates the state by applying move.
     *
     * @param move int[4]
     */
    public void updateState(int[] move) {
        if (myTurn == this.isWhite) {
            this.currentState.whiteMove(new Move(move));
        } else {
            this.currentState.blackMove(new Move(move));
        }
    }

    /**
     * Getter for time limit.
     *
     * @return long (ms)
     */
    public long getTimeLimit() {
        return this.timeLimit;
    }

    /**
     * Getter for evaluator.
     *
     * @return Evaluator
     */
    public Evaluator getEvaluator() {
        return this.evaluator;
    }

    /**
     * Update who's turn it is.
     */
    public void nextPlayer() {
        this.myTurn = !this.myTurn;
    }

    /**
     * Returns true iff it's agent's turn to play.
     *
     * @return boolean
     */
    public boolean isMyTurn() {
        return this.myTurn;
    }

    /**
     * Returns true iff agent plays as white.
     *
     * @return boolean
     */
    public boolean isWhite() {
        return this.isWhite;
    }

    /**
     * Getter for game rules.
     *
     * @return Rules
     */
    public Rules getRules() {
        return this.rules;
    }

    /**
     * Getter for current state of game.
     *
     * @return State
     */
    public State getCurrentState() {
        return this.currentState;
    }
}
