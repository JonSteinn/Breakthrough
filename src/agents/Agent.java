package agents;

public interface Agent {

    /**
     * Run before game starts. Used to set values.
     *
     * @param role String
     * @param width int
     * @param height int
     * @param playClock int (s)
     */
    void init(String role, int width, int height, int playClock);

    /**
     * Performs the next action in the game.
     *
     * @param lastMove int[]
     * @return String
     */
    String nextAction(int[] lastMove);

    /**
     * Cleans up resources after the game
     */
    void cleanup();
}
