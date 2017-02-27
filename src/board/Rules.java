package board;

/**
 * Created by Jonni on 2/24/2017.
 *
 * Holds various data for a game, such as sizes and time.
 */
public class Rules {

    public static final int TIME_OFFSET = 35;

    public final int width;
    public final int height;
    public final long timeLimit;

    /**
     * Constructor for game data. It handles setting leeway for time.
     *
     * @param width int
     * @param height int
     * @param timeLimit int (s)
     */
    public Rules(int width, int height, int timeLimit) {
        this.width = width;
        this.height = height;
        this.timeLimit = 1000 * timeLimit - Rules.TIME_OFFSET;
    }
}
