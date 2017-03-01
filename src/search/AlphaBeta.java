package search;

import agents.Status;
import board.Actions;
import board.Evaluator;
import board.Move;
import board.State;
import experiments.LaTeX;
import experiments.Statistics;

import java.util.ArrayList;
import java.util.Queue;

/* This has become very messy from all the experiments. I didn't want to remove
 * any of the commented out code if I wanted to re-run some of them. Feel free
 * to remove all this code in comments about transposition tables and other
 * orderings (or 'non-ordering') if this is to much of a mess to read through.
 */

/**
 * Created by Jonni on 2/24/2017.
 *
 * Game tree search that uses Negamax Alpha Beta pruning with ordering.
 */

public class AlphaBeta {
    private static final int P_INF = +0x7FFFFFFF; // All but the left most bit set
    private static final int N_INF = -0x7FFFFFFF;

    private long time;
    private Status status;
    private Move bestMove;
    private Statistics statistics;
    //private TranspositionTable table;

    /**
     * Constructor that performs the search and sets its bestMove variable to
     * the best move it found.
     *
     * @param status Status
     */
    public AlphaBeta(Status status   /*, TranspositionTable table  */   ) {
        this.time = System.currentTimeMillis();
        this.status = status;
        this.bestMove = null;
        this.statistics = new Statistics();
        int lastDepth = 0;
        //this.table = table;


        //LaTeX tex = new LaTeX(); // LaTeX output for report, ignore

        // IDDFS
        try {
            for (int depth = 1; ; depth++) {

                statistics.reset(depth);
                // Set best move
                root(status.getCurrentState(), status.isWhite(), N_INF, P_INF, depth);
                statistics.calculateTime();
                //tex.addRow(statistics); // LaTeX output
                System.out.println(statistics); // Uncomment for statistics in stdout

                // If this occurs, we have reached a terminal node on
                // all fronts so no need to continue searching.
                if (lastDepth >= statistics.getDepth()) {
                    System.out.println("Total time: " + (System.currentTimeMillis() - this.time));
                    break;
                }
                lastDepth = statistics.getDepth();
            }
        } catch (SearchTimeOutException ex) {
            System.out.println("Total time: time limit");
        }
        //tex.createTable("caption", "label"); // LaTeX output
    }

    /**
     * Getter for best move as string.
     *
     * @return String
     */
    public String getBestMove() {
        // This should never happen, but is still logically possible
        // Will happen if:
        //      1. TimeLimit is set as 0
        //      2. Is called for a terminal state
        //      3. Your hardware is from the 1850s
        if (this.bestMove == null) {
            ArrayList<Move> moves = status.isWhite() ?
                    Actions.getWhiteActions(status.getCurrentState()) :
                    Actions.getBlackActions(status.getCurrentState());
            return moves.size() == 0 ? Status.NO_MOVE : moves.get(0).toString();
        }
        return this.bestMove.toString();
    }

    /**
     * Root search that stores move. Works similar to the non root version a part form not
     * pruning since we never prune at root level without using TT.
     *
     * @param state State
     * @param whiteToPlay boolean
     * @param alpha int
     * @param beta int
     * @param depth int
     * @throws SearchTimeOutException
     */
    private void root(State state, boolean whiteToPlay, int alpha, int beta, int depth) throws SearchTimeOutException {

        this.statistics.addExpansion();

        if (whiteToPlay) { // If agent is white

            Queue<Actions.ActionWrapperWhiteRoot> pq = Actions.heuristicOrderWhiteRoot(status, state);  // Ordered by heuristics
            //Queue<Move> pq = Actions.getOrderedByFurthestWhiteActions(state);                         // Ordered by y-positions
            //ArrayList<Move> mv = Actions.getBlackActions(state);                                      // No ordering

            // for (Move next : mv) {                               // no order
            while (!pq.isEmpty()) {

                //Move nextMove = pq.poll();                        // order by y-pos
                Actions.ActionWrapperWhiteRoot next = pq.poll();    // order by heuristic

                int value = -blackAlphaBeta(
                        //State.createBlackToMoveChild(state, nextMove),        // Order by y-pos
                        //State.createBlackToMoveChild(state, next),            // No order
                        next.state,                                             // Order by heuristic
                        -beta, -alpha, depth - 1
                );

                if (value > alpha) {
                    alpha = value;

                    //this.bestMove = next;         // No order
                    //this.bestMove = nextMove;     // order by y-pos
                    this.bestMove = next.move;      // order by heuristics

                }
            }

            ///////////////////// Transposition Table ////////////////////////
            // this.table.addWhiteState(state, alpha, statistics.getTotalDepth());

        } else { // If agent is black

            // ArrayList<Move> mv = Actions.getBlackActions(state);                                     // No order
            //Queue<Move> pq = Actions.getOrderedByFurthestBlackActions(state);                         // y-pos order
            Queue<Actions.ActionWrapperBlackRoot> pq = Actions.heuristicOrderBlackRoot(status, state);  // Ordered by heuristics

            //for (Move next : mv) {                                    // No order
            while (!pq.isEmpty()) {

                //Move nextMove = pq.poll();                            // Y-pos order
                Actions.ActionWrapperBlackRoot next = pq.poll();        // Heuristic order

                int value = -whiteAlphaBeta(
                        //State.createWhiteToMoveChild(state, next),          // No order
                        //State.createWhiteToMoveChild(state, nextMove),      // y-pos order
                        next.state,                                           // Heuristic order
                        -beta, -alpha, depth - 1);
                if (value > alpha) {
                    alpha = value;
                    //this.bestMove = next;             // no order
                    //this.bestMove = nextMove;         // y-pos order
                    this.bestMove = next.move;          // heuristic order
                }
            }

            ///////////////////// Transposition Table ////////////////////////
            // this.table.addBlackState(state, alpha, statistics.getTotalDepth());

        }
    }

    /**
     * Estimates state from white's pov.
     *
     * @param state State
     * @param alpha int
     * @param beta int
     * @param depth int
     * @return int
     * @throws SearchTimeOutException on timeout
     */
    private int whiteAlphaBeta(State state, int alpha, int beta, int depth) throws SearchTimeOutException {
        timeCheck();

        this.statistics.addExpansion();
        this.statistics.updateDepth(depth);

        int terminal = Evaluator.whiteTerminalCheck(state, status.getRules());
        if (terminal == Evaluator.LOST) {
            return Evaluator.LOSE_VALUE;
        }
        if (terminal == Evaluator.DRAW) {
            return Evaluator.DRAW_VALUE;
        }

        if (depth == 0) {
            return status.getEvaluator().whiteHeuristic(state, status.getRules());
        }

        ///////////////////// Transposition Table ////////////////////////
        //TableEntry tEntry;
        //if (((tEntry = this.table.findWhiteState(state)) != null)
        //        && (tEntry.depth >= (statistics.getTotalDepth() - depth))) {
        //    return tEntry.value;
        //}

        //ArrayList<Move> mv = Actions.getWhiteActions(state);                                  // no order
        //Queue<Move> pq = Actions.getOrderedByFurthestWhiteActions(state);                     // y-pos order
        Queue<Actions.ActionWrapperWhite> pq = Actions.heuristicOrderWhite(status, state);      // heuristic order

        //for (Move next : mv) {                                           // no order
        while (!pq.isEmpty()) {
            int value = -blackAlphaBeta(
                    //State.createBlackToMoveChild(state, next),           // no order
                    //State.createBlackToMoveChild(state, pq.poll()),      // y-pos order
                    pq.poll().state,                                       // heuristic order
                    -beta,
                    -alpha,
                    depth - 1);
            if (alpha < value) { // Better value found
                alpha = value;
            }
            if (alpha >= beta) { // Cut
                break;
            }
        }

        ///////////////////// Transposition Table ////////////////////////
        //table.addWhiteState(state, alpha, this.statistics.getTotalDepth() - depth);


        return alpha;
    }

    /**
     * Estimates state from black's pov.
     *
     * @param state State
     * @param alpha int
     * @param beta int
     * @param depth int
     * @return int
     * @throws SearchTimeOutException on timeout
     */
    private int blackAlphaBeta(State state, int alpha, int beta, int depth) throws SearchTimeOutException {
        timeCheck();

        this.statistics.addExpansion();
        this.statistics.updateDepth(depth);

        int terminal = Evaluator.blackTerminalCheck(state, status.getRules());
        if (terminal == Evaluator.LOST) {
            return Evaluator.LOSE_VALUE;
        }
        if (terminal == Evaluator.DRAW) {
            return Evaluator.DRAW_VALUE;
        }

        if (depth == 0) {
            return status.getEvaluator().blackHeuristic(state, status.getRules());
        }

        ///////////////////// Transposition Table ////////////////////////
        //TableEntry tEntry;
        //if (((tEntry = this.table.findBlackState(state)) != null)
        //              && (tEntry.depth >= (statistics.getTotalDepth() - depth))) {
        //    return tEntry.value;
        //}

        //ArrayList<Move> mv = Actions.getBlackActions(state);                                    // no order
        //Queue<Move> pq = Actions.getOrderedByFurthestBlackActions(state);                       // y-pos order
        Queue<Actions.ActionWrapperBlack> pq = Actions.heuristicOrderBlack(status, state);        // heuristic order

        //for (Move next : mv) {                                                // no order
        while (!pq.isEmpty()) {
            int value = -whiteAlphaBeta(
                    //State.createWhiteToMoveChild(state, next)m                // no order
                    //State.createWhiteToMoveChild(state, pq.poll()),           // y-pos order
                    pq.poll().state,                                            // heuristic order
                    -beta,
                    -alpha,
                    depth - 1);
            if (alpha < value) {
                alpha = value;
            }
            if (alpha >= beta) {
                break;
            }
        }

        ///////////////////// Transposition Table ////////////////////////
        //table.addBlackState(state, alpha, this.statistics.getTotalDepth() - depth);

        return alpha;
    }

    /**
     * Checks if we are passed our time limit.
     *
     * @throws SearchTimeOutException if out of time
     */
    private void timeCheck() throws SearchTimeOutException {
        if (System.currentTimeMillis() - this.time >= this.status.getTimeLimit()) {
            throw new SearchTimeOutException();
        }
    }
}
