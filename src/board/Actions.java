package board;

import java.util.*;

/**
 * Created by Jonni on 2/18/2017.
 *
 * A collection of static methods to generate possible moves.
 */
public class Actions {

    /**
     * Returns the possible moves for white as a max priority queue
     * with a int comparator on pawn's y position.
     *
     * @param state State
     * @return Queue of Move
     */
    public static Queue<Move> getOrderedByFurthestWhiteActions(State state) {
        Queue<Move> orderedMoves = new PriorityQueue<>(Comparator.comparing(o -> -o.getTo().getY()));
        fillWhite(orderedMoves, state);
        return orderedMoves;
    }

    /**
     * Returns a list of white's possible moves in no particular order.
     *
     * @param state State
     * @return ArrayList of Move
     */
    public static ArrayList<Move> getWhiteActions(State state) {
        ArrayList<Move> moves = new ArrayList<>();
        fillWhite(moves, state);
        return moves;
    }

    /**
     * Helper that fills collections for white.
     *
     * @param coll Collection of Move
     * @param state State
     */
    private static void fillWhite(Collection<Move> coll, State state) {
        for (Position position : state.getWhite()) {
            Position[] possibilities = position.frontOfWhite();
            if (!state.getWhite().contains(possibilities[1]) && !state.getBlack().contains(possibilities[1])) {
                coll.add(new Move(position, possibilities[1]));
            }
            if (state.getBlack().contains(possibilities[0])) {
                coll.add(new Move(position, possibilities[0]));
            }
            if (state.getBlack().contains(possibilities[2])) {
                coll.add(new Move(position, possibilities[2]));
            }
        }
    }

    /**
     * Returns the possible moves for black as a min priority queue
     * with a int comparator on pawn's y position.
     *
     * @param state State
     * @return Queue of Move
     */
    public static Queue<Move> getOrderedByFurthestBlackActions(State state) {
        Queue<Move> orderedMoves = new PriorityQueue<>(Comparator.comparingInt(o -> o.getTo().getY()));
        fillBlack(orderedMoves, state);
        return orderedMoves;
    }
    /**
     *
     * Returns a list of blacks's possible moves in no particular order.
     *
     * @param state State
     * @return ArrayList of Move
     */
    public static ArrayList<Move> getBlackActions(State state) {
        ArrayList<Move> moves = new ArrayList<>();
        fillBlack(moves, state);
        return moves;
    }

    /**
     * Helper that fills collections for black.
     *
     * @param coll Collection of Move
     * @param state State
     */
    private static void fillBlack(Collection<Move> coll, State state) {
        for (Position position : state.getBlack()) {
            Position[] possibilities = position.frontOfBlack();
            if (!state.getWhite().contains(possibilities[1]) && !state.getBlack().contains(possibilities[1])) {
                coll.add(new Move(position, possibilities[1]));
            }
            if (state.getWhite().contains(possibilities[0])) {
                coll.add(new Move(position, possibilities[0]));
            }
            if (state.getWhite().contains(possibilities[2])) {
                coll.add(new Move(position, possibilities[2]));
            }
        }
    }

}
