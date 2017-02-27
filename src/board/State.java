package board;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jonni on 2/18/2017.
 *
 * A state in Breakthrough.
 */
public class State {

    /**
     * Creates a state from white's move.
     *
     * @param state State
     * @param move Move
     * @return State
     */
    public static State createBlackToMoveChild(State state, Move move) {
        State child = new State(state);
        child.whiteMove(move);
        return child;
    }

    /**
     * Creates a state from black's move.
     *
     * @param state State
     * @param move Move
     * @return State
     */
    public static State createWhiteToMoveChild(State state, Move move) {
        State child = new State(state);
        child.blackMove(move);
        return child;
    }

    private Set<Position> white;
    private Set<Position> black;

    /**
     * Constructor that fills first and last two rows of white and black
     * pawns respectively.
     *
     * @param rules Rules
     */
    public State(Rules rules) {
        this.white = new HashSet<>();
        this.black = new HashSet<>();
        for (int i = 1; i <= rules.width; i++) {
            this.white.add(new Position(i, 1));
            this.white.add(new Position(i, 2));
            this.black.add(new Position(i, rules.height - 1));
            this.black.add(new Position(i, rules.height));
        }
    }

    /**
     * Copy constructor.
     *
     * @param state State
     */
    public State(State state) {
        this.white = new HashSet<>(state.getWhite());
        this.black = new HashSet<>(state.getBlack());
    }

    /**
     * Update state with white's move.
     *
     * @param move Move
     */
    public void whiteMove(Move move) {
        this.white.remove(move.getFrom());
        this.white.add(move.getTo());
        if (move.getFrom().getX() != move.getTo().getX()) {
            this.black.remove(move.getTo());
        }
    }

    /**
     * Update state with black's move.
     *
     * @param move Move
     */
    public void blackMove(Move move) {
        this.black.remove(move.getFrom());
        this.black.add(move.getTo());
        if (move.getFrom().getX() != move.getTo().getX()) {
            this.white.remove(move.getTo());
        }
    }

    /**
     * Getter for white's pawns.
     *
     * @return State
     */
    public Set<Position> getWhite() {
        return this.white;
    }

    /**
     * Getter for black's pawns.
     *
     * @return State
     */
    public Set<Position> getBlack() {
        return this.black;
    }

    @Override
    public int hashCode() {
        return 31 * white.hashCode() + black.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        State other = (State) o;
        return this.white.equals(other.white) && this.black.equals(other.black);
    }

    @Override
    public String toString() {
        return "White: " + this.white.toString() + "\nBlack: " + this.black.toString();
    }
}