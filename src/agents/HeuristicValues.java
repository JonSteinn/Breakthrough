package agents;

/**
 * Created by Jonni on 2/28/2017.
 */
public class HeuristicValues {
    public static final HeuristicValues best
            = new HeuristicValues(101,257,432,290,447);


    private int countValue;
    private int furthestValue;
    private int movableValue;
    private int unhinderedValue;
    private int laneControlValue;

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

    public int getCountValue() {
        return countValue;
    }

    public void setCountValue(int countValue) {
        this.countValue = countValue;
    }

    public int getFurthestValue() {
        return furthestValue;
    }

    public void setFurthestValue(int furthestValue) {
        this.furthestValue = furthestValue;
    }

    public int getMovableValue() {
        return movableValue;
    }

    public void setMovableValue(int movableValue) {
        this.movableValue = movableValue;
    }

    public int getUnhinderedValue() {
        return unhinderedValue;
    }

    public void setUnhinderedValue(int unhinderedValue) {
        this.unhinderedValue = unhinderedValue;
    }

    public int getLaneControlValue() {
        return laneControlValue;
    }

    public void setLaneControlValue(int laneControlValue) {
        this.laneControlValue = laneControlValue;
    }
}
