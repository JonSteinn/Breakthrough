package agents;

import search.AlphaBeta;
import search.TranspositionTable;

/**
 * Created by Jonni on 2/24/2017.
 *
 * An alpha beta pruning agent.
 */
public class MyAgent implements Agent {

    private Status status;
    private TranspositionTable table;

    @Override
    public void init(String role, int width, int height, int playClock) {

        this.status = new Status(width, height, playClock, role, HeuristicValues.best);
                //new HeuristicValues(101,257,432,290,447));
        this.table = new TranspositionTable();
    }

    @Override
    public String nextAction(int[] lastMove) {
        if (lastMove != null) this.status.updateState(lastMove);
        this.status.nextPlayer();

        if (this.status.isMyTurn()) {
            return new AlphaBeta(status).getBestMove();
        } else {
            return Status.NO_MOVE;
        }
    }

    @Override
    public void cleanup() {
        this.table.clean();
    }

    /**
     * Only used for testing. Returns the status.
     *
     * @return Status
     */
    public Status getStatus() {
        return this.status;
    }
}
