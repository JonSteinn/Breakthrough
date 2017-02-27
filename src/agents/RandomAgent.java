package agents;

import board.Actions;
import board.Move;

import java.util.ArrayList;
import java.util.Random;

public class RandomAgent implements Agent {

	private Random random = new Random();
	private Status status;

	public void init(String role, int width, int height, int playClock) {
		this.status = new Status(width, height, playClock, role);
	}

	public String nextAction(int[] lastMove) {
		// First turn
		if (lastMove != null) this.status.updateState(lastMove);

		// Switch who's turn (initially set as black).
		this.status.nextPlayer();

		// If agent's turn, a random Move from the possible moves of his color.
		if (this.status.isMyTurn()) {
			ArrayList<Move> options = this.status.isWhite() ?
					Actions.getWhiteActions(this.status.getCurrentState()) :
					Actions.getBlackActions(this.status.getCurrentState());
			return options.get(this.random.nextInt(options.size())).toString();
		} else {
			return Status.NO_MOVE;
		}
	}

	@Override
	public void cleanup() {	}
}
