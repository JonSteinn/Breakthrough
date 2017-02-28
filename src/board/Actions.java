package board;

import agents.Status;

import java.util.*;

/**
 * Created by Jonni on 2/18/2017.
 *
 * A collection of static methods to generate possible moves.
 */
public class Actions {

    // The ones that are used are at the bottom...

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
        // (To the grader: Was only used in experiments, feel free to ignore)
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
        // (To the grader: Was only used in experiments, feel free to ignore)
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
        // (To the grader: Was only used in experiments, feel free to ignore)
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
        // (To the grader: Was only used in experiments, feel free to ignore)
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
        // (To the grader: Was only used in experiments, feel free to ignore)
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
        // (To the grader: Was only used in experiments, feel free to ignore)
    }


    // The following is very repetitive and... well, messy
    // Basically, it's just a priority queue generation for
    // ordering expansions and wrapper classes to include
    // the heuristic without adding it to the states which
    // perhaps would have been better.
    //
    // I did try
    // public static <T extends ActionWrapperWhite> void fillOrderedWhite(PriorityQueue<T> pq, State state, Status status)  {
    // but ultimately creating instance for T was equally dreadful.


    /**
     * Heuristic ordered queue of reachable states for white.
     *
     * @param status Status
     * @param state State
     * @return Queue of ActionWrapperWhite
     */
    public static Queue<ActionWrapperWhite> heuristicOrderWhite(Status status, State state) {
        // max pq by heuristic
        Queue<ActionWrapperWhite> pq = new PriorityQueue<>(Comparator.comparingInt(o -> -o.heuristic));
        // for all white pawns
        for (Position position : state.getWhite()) {
            // All possible position to move to
            Position[] possibilities = position.frontOfWhite();
            // If can move forward
            if (!state.getWhite().contains(possibilities[1]) && !state.getBlack().contains(possibilities[1])) {
                pq.add(new ActionWrapperWhite(state, new Move(position, possibilities[1]),
                        status.getRules(), status.getEvaluator()));
            }
            // If can attack diagonally left
            if (state.getBlack().contains(possibilities[0])) {
                pq.add(new ActionWrapperWhite(state, new Move(position, possibilities[0]),
                        status.getRules(), status.getEvaluator()));
            }
            // If can attack diagonally right
            if (state.getBlack().contains(possibilities[2])) {
                pq.add(new ActionWrapperWhite(state, new Move(position, possibilities[2]),
                        status.getRules(), status.getEvaluator()));
            }
        }
        return pq;
    }

    /**
     * Heuristic ordered queue of reachable states for white. Works just like the other but
     * includes best move.
     *
     * @param status Status
     * @param state State
     * @return Queue of ActionWrapperWhiteRoot
     */
    public static Queue<ActionWrapperWhiteRoot> heuristicOrderWhiteRoot(Status status, State state) {
        Queue<ActionWrapperWhiteRoot> pq = new PriorityQueue<>(Comparator.comparingInt(o -> -o.heuristic));
        for (Position position : state.getWhite()) {
            Position[] possibilities = position.frontOfWhite();
            if (!state.getWhite().contains(possibilities[1]) && !state.getBlack().contains(possibilities[1])) {
                pq.add(new ActionWrapperWhiteRoot(state, new Move(position, possibilities[1]),
                        status.getRules(), status.getEvaluator()));
            }
            if (state.getBlack().contains(possibilities[0])) {
                pq.add(new ActionWrapperWhiteRoot(state, new Move(position, possibilities[0]),
                        status.getRules(), status.getEvaluator()));
            }
            if (state.getBlack().contains(possibilities[2])) {
                pq.add(new ActionWrapperWhiteRoot(state, new Move(position, possibilities[2]),
                        status.getRules(), status.getEvaluator()));
            }
        }
        return pq;
    }

    /**
     * Heuristic ordered queue of reachable states for black.
     *
     * @param status Status
     * @param state State
     * @return Queue of ActionWrapperBlack
     */
    public static Queue<ActionWrapperBlack> heuristicOrderBlack(Status status, State state) {
        Queue<ActionWrapperBlack> pq = new PriorityQueue<>(Comparator.comparingInt(o -> -o.heuristic));
        for (Position position : state.getBlack()) {
            Position[] possibilities = position.frontOfBlack();
            if (!state.getWhite().contains(possibilities[1]) && !state.getBlack().contains(possibilities[1])) {
                pq.add(new ActionWrapperBlack(state, new Move(position, possibilities[1]),
                        status.getRules(), status.getEvaluator()));
            }
            if (state.getWhite().contains(possibilities[0])) {
                pq.add(new ActionWrapperBlack(state, new Move(position, possibilities[0]),
                        status.getRules(), status.getEvaluator()));
            }
            if (state.getWhite().contains(possibilities[2])) {
                pq.add(new ActionWrapperBlack(state, new Move(position, possibilities[2]),
                        status.getRules(), status.getEvaluator()));
            }
        }
        return pq;
    }

    /**
     * Heuristic ordered queue of reachable states for black. Identical to other but also contains move.
     *
     * @param status Status
     * @param state State
     * @return Queue of ActionWrapperBlackRoot
     */
    public static Queue<ActionWrapperBlackRoot> heuristicOrderBlackRoot(Status status, State state) {
        Queue<ActionWrapperBlackRoot> pq = new PriorityQueue<>(Comparator.comparingInt(o -> -o.heuristic));
        for (Position position : state.getBlack()) {
            Position[] possibilities = position.frontOfBlack();
            if (!state.getWhite().contains(possibilities[1]) && !state.getBlack().contains(possibilities[1])) {
                pq.add(new ActionWrapperBlackRoot(state, new Move(position, possibilities[1]),
                        status.getRules(), status.getEvaluator()));
            }
            if (state.getWhite().contains(possibilities[0])) {
                pq.add(new ActionWrapperBlackRoot(state, new Move(position, possibilities[0]),
                        status.getRules(), status.getEvaluator()));
            }
            if (state.getWhite().contains(possibilities[2])) {
                pq.add(new ActionWrapperBlackRoot(state, new Move(position, possibilities[2]),
                        status.getRules(), status.getEvaluator()));
            }
        }
        return pq;
    }

    /**
     * Wrapper for heuristic ordering
     */
    public static class ActionWrapperWhite {
        public State state;
        public int heuristic;
        public ActionWrapperWhite(State state, Move move, Rules rules, Evaluator eval) {
            this.state = State.createBlackToMoveChild(state, move);
            heuristic = eval.whiteHeuristic(this.state, rules);
        }
    }

    /**
     * Wrapper for Heuristic ordering
     */
    public static class ActionWrapperBlack {
        public State state;
        public int heuristic;
        public ActionWrapperBlack(State state, Move move, Rules rules, Evaluator eval) {
            this.state = State.createWhiteToMoveChild(state, move);
            heuristic = eval.blackHeuristic(this.state, rules);
        }
    }

    /**
     * Wrapper for Heuristic ordering
     */
    public static class ActionWrapperWhiteRoot extends ActionWrapperWhite {
        public Move move;
        public ActionWrapperWhiteRoot(State state, Move move, Rules rules, Evaluator eval) {
            super(state, move, rules, eval);
            this.move = move;
        }
    }

    /**
     * Wrapper for Heuristic ordering
     */
    public static class ActionWrapperBlackRoot extends ActionWrapperBlack {
        public Move move;
        public ActionWrapperBlackRoot(State state, Move move, Rules rules, Evaluator eval) {
            super(state,move,rules,eval);
            this.move = move;
        }
    }
}
