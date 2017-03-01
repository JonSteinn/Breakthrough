package experiments;

/**
 * Created by Jonni on 2/25/2017.
 *
 * Records statistics for game tree.
 */
public class Statistics {

    /**
     * Only used for LaTeX tables to store unique entries.
     *
     * @param toCopy Statistics
     * @return Statistics
     */
    public static Statistics copy(Statistics toCopy) {
        Statistics cp = new Statistics();
        cp.time = toCopy.getTime();
        cp.expansionCounter = toCopy.expansionCounter;
        cp.depth = toCopy.getDepth();
        return cp;
    }

    private int expansionCounter;
    private int depth;
    private long time;
    private int totalDepth;

    /**
     * Sets depth and time as well as other values as zero.
     *
     * @param totalDepth int
     */
    public void reset(int totalDepth) {
        this.totalDepth = totalDepth;
        this.expansionCounter = 0;
        this.depth = 0;
        this.time = System.currentTimeMillis();
    }

    public int getTotalDepth() {
        return this.totalDepth;
    }

    /**
     * Increment expansion counter.
     */
    public void addExpansion() {
        this.expansionCounter++;
    }

    /**
     * Updates max depth if larger found.
     *
     * @param depth int
     */
    public void updateDepth(int depth) {
        this.depth = Math.max(this.depth, totalDepth - depth);
    }

    /**
     * Calculates time from last reset.
     */
    public void calculateTime() {
        this.time = System.currentTimeMillis() - this.time;
    }

    /**
     * Getter for max depth.
     *
     * @return int
     */
    public int getDepth() {
        return this.depth;
    }

    /**
     * Getter for expansion counter.
     *
     * @return int
     */
    public int getExpansionCounter() {
        return this.expansionCounter;
    }

    /**
     * Getter for time.
     *
     * @return long
     */
    public long getTime() {
        return this.time;
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append("Expansions: ");
        str.append(this.expansionCounter);
        str.append('\n');
        str.append("Max depth: ");
        str.append(this.depth);
        str.append('\n');
        str.append("Time: ");
        str.append(this.time);
        return str.toString();
    }
}
