package search;

import agents.Status;
import board.Actions;
import board.Evaluator;
import board.Move;
import board.State;
import experiments.Statistics;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by Jonni on 2/24/2017.
 */
public class AlphaBeta {
    public static final int P_INF = +0x7FFFFFFF; // All but the left most bit set
    public static final int N_INF = -0x7FFFFFFF;

    private long time;
    private Status status;
    private Move bestMove;
    private Statistics statistics;

    /**
     * Constructor that performs the search and sets its bestMove variable to
     * the best move it found.
     *
     * @param status Status
     */
    public AlphaBeta(Status status) {
        this.time = System.currentTimeMillis();
        this.status = status;
        this.bestMove = null;
        this.statistics = new Statistics();
        int lastDepth = 0;

        // IDDFS
        try {
            for (int depth = 1; ; depth++) {

                statistics.reset(depth);

                root(status.getCurrentState(), status.isWhite(), N_INF, P_INF, depth);

                statistics.calculateTime();
                //System.out.println(statistics); // Uncomment for statistics in stdout

                // If this occurs, we have reached a terminal node on
                // all fronts so no need to continue searching.
                if (lastDepth >= statistics.getDepth()) {
                    break;
                }
                lastDepth = statistics.getDepth();
            }
        } catch (SearchTimeOutException ex) { }
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
    public void root(State state, boolean whiteToPlay, int alpha, int beta, int depth) throws SearchTimeOutException {

        // TODO: update if time for TT

        this.statistics.addExpansion();

        if (whiteToPlay) { // If agent is white
            Queue<Move> pq = Actions.getOrderedByFurthestWhiteActions(state); // TODO: try wrapper class and use heuristic, test pq vs sort. Rethink if TT.
            while (!pq.isEmpty()) {
                Move nextMove = pq.poll();
                int value = -blackAlphaBeta(State.createBlackToMoveChild(state, nextMove), -beta, -alpha, depth - 1);
                if (value > alpha) {
                    alpha = value;
                    this.bestMove = nextMove;
                }
            }
        } else { // If agent is black
            Queue<Move> pq = Actions.getOrderedByFurthestBlackActions(state); // TODO: try wrapper class and use heuristic, test pq vs sort. Rethink if TT.
            while (!pq.isEmpty()) {
                Move nextMove = pq.poll();
                int value = -whiteAlphaBeta(State.createWhiteToMoveChild(state, nextMove), -beta, -alpha, depth - 1);
                if (value > alpha) {
                    alpha = value;
                    this.bestMove = nextMove;
                }
            }
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
    public int whiteAlphaBeta(State state, int alpha, int beta, int depth) throws SearchTimeOutException {
        timeCheck();

        this.statistics.addExpansion();
        this.statistics.updateDepth(depth);

        int terminal = Evaluator.whiteTerminalCheck(state, status.getRules());
        /*
        // Removed. (black can't move to a winning pos for white)
        if (terminal == Evaluator.WIN) {
            return Evaluator.WIN_VALUE;
        }
        */
        if (terminal == Evaluator.LOST) {
            return Evaluator.LOSE_VALUE;
        }
        if (terminal == Evaluator.DRAW) {
            return Evaluator.DRAW_VALUE;
        }

        if (depth == 0) {
            return status.getEvaluator().whiteHeuristic(state, status.getRules());
        }

        Queue<Move> pq = Actions.getOrderedByFurthestWhiteActions(state);

        // One liner edition
        // while (!pq.isEmpty() && ((alpha = Math.max(alpha, -blackAlphaBeta(State.createBlackToMoveChild(state, pq.poll()), -beta, -alpha, depth - 1))) < beta)) {}

        while (!pq.isEmpty()) {
            int value = -blackAlphaBeta(
                    State.createBlackToMoveChild(state, pq.poll()),
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
    public int blackAlphaBeta(State state, int alpha, int beta, int depth) throws SearchTimeOutException {
        timeCheck();
        this.statistics.addExpansion();
        this.statistics.updateDepth(depth);

        int terminal = Evaluator.blackTerminalCheck(state, status.getRules());
        /*
        Removed, white can't move into a winning pos for black
        if (terminal == Evaluator.WIN) {
            return Evaluator.WIN_VALUE;
        }
        */
        if (terminal == Evaluator.LOST) {
            return Evaluator.LOSE_VALUE;
        }
        if (terminal == Evaluator.DRAW) {
            return Evaluator.DRAW_VALUE;
        }

        if (depth == 0) {
            return status.getEvaluator().blackHeuristic(state, status.getRules());
        }

        Queue<Move> pq = Actions.getOrderedByFurthestBlackActions(state);

        // One liner edition
        // while (!pq.isEmpty() && ((alpha = Math.max(alpha, -whiteAlphaBeta(State.createWhiteToMoveChild(state, pq.poll()), -beta, -alpha, depth - 1))) < beta)) {}

        while (!pq.isEmpty()) {
            int value = -whiteAlphaBeta(
                    State.createWhiteToMoveChild(state, pq.poll()),
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
