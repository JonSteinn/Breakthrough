package agents;

/**
 * Created by Jonni on 2/28/2017.
 *
 * A class holding the values for the evaluation.
 */
public class HeuristicValues {
    // This was set by hand after running experiments, the agent uses this one
    public static final HeuristicValues best
            = new HeuristicValues(1,1,1,3,1);
            //= new HeuristicValues(80,185,142,56,124);

    private int countValue;
    private int furthestValue;
    private int movableValue;
    private int unhinderedValue;
    private int laneControlValue;

    /**
     * Constructor for values.
     *
     * @param countValue int (how many pawns left)
     * @param furthestValue int (how far is the furthest pawn)
     * @param movableValue int (how many pawns can move)
     * @param unhinderedValue int (how many pawns have a clear path to goal)
     * @param laneControlValue int (how many columns do we have pawns on)
     */
    public HeuristicValues(int countValue,
                           int furthestValue,
                           int movableValue,
                           int unhinderedValue,
                           int laneControlValue) {
        this.countValue = countValue;
        this.furthestValue = furthestValue;
        this.movableValue = movableValue;
        this.unhinderedValue = unhinderedValue;
        this.laneControlValue = laneControlValue;
    }

    /**
     * Copy constructor
     *
     * @param heuristicValues HeuristicValues
     */
    public HeuristicValues(HeuristicValues heuristicValues) {
        this(
                heuristicValues.getCountValue(),
                heuristicValues.getFurthestValue(),
                heuristicValues.getMovableValue(),
                heuristicValues.getUnhinderedValue(),
                heuristicValues.getLaneControlValue()
        );
    }

    /**
     * Getter for count value
     *
     * @return int
     */
    public int getCountValue() {
        return countValue;
    }

    /**
     * Setter for count value
     *
     * @param countValue int
     */
    public void setCountValue(int countValue) {
        this.countValue = countValue;
    }

    /**
     * Getter for furthest value
     *
     * @return int
     */
    public int getFurthestValue() {
        return furthestValue;
    }

    /**
     * Setter for furthest value
     *
     * @param furthestValue int
     */
    public void setFurthestValue(int furthestValue) {
        this.furthestValue = furthestValue;
    }

    /**
     * Getter for movable value
     *
     * @return int
     */
    public int getMovableValue() {
        return movableValue;
    }

    /**
     * Setter for movable value
     *
     * @param movableValue int
     */
    public void setMovableValue(int movableValue) {
        this.movableValue = movableValue;
    }

    /**
     * Getter for unhindered value
     *
     * @return int
     */
    public int getUnhinderedValue() {
        return unhinderedValue;
    }

    /**
     * Setter for unhindered value
     *
     * @param unhinderedValue int
     */
    public void setUnhinderedValue(int unhinderedValue) {
        this.unhinderedValue = unhinderedValue;
    }

    /**
     * Getter for lane control value
     *
     * @return int
     */
    public int getLaneControlValue() {
        return laneControlValue;
    }

    /**
     * Setter for lane control value
     *
     * @param laneControlValue int
     */
    public void setLaneControlValue(int laneControlValue) {
        this.laneControlValue = laneControlValue;
    }
}
