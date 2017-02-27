package agents;

import board.Evaluator;
import search.AlphaBeta;

/**
 * Created by Jonni on 2/24/2017.
 *
 * An alpha beta pruning agent.
 */
public class MyAgent implements Agent {

    private Status status;

    @Override
    public void init(String role, int width, int height, int playClock) {
        // Some test were run to get these values.
        // A nice improvement would be to update them as you play more games.
        this.status = new Status(width, height, playClock, role,
                417,148,58,58,82);
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
