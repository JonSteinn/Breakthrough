package agents;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jonni on 2/28/2017.
 */
public class HeuristicValuesTest {

    private HeuristicValues hVal;

    @Before
    public void setUp() throws Exception {
        this.hVal = new HeuristicValues(1,2,3,4,5);
    }

    @Test
    public void testGetters() {
        assertEquals(1, this.hVal.getCountValue());
        assertEquals(2, this.hVal.getFurthestValue());
        assertEquals(3, this.hVal.getMovableValue());
        assertEquals(4, this.hVal.getUnhinderedValue());
        assertEquals(5, this.hVal.getLaneControlValue());
    }

    @Test
    public void testSetters() {
        this.hVal.setCountValue(this.hVal.getCountValue() + 1);
        this.hVal.setFurthestValue(this.hVal.getFurthestValue() + 1);
        this.hVal.setMovableValue(this.hVal.getMovableValue() + 1);
        this.hVal.setUnhinderedValue(this.hVal.getUnhinderedValue() + 1);
        this.hVal.setLaneControlValue(this.hVal.getLaneControlValue() + 1);
        assertEquals(1+1, this.hVal.getCountValue());
        assertEquals(2+1, this.hVal.getFurthestValue());
        assertEquals(3+1, this.hVal.getMovableValue());
        assertEquals(4+1, this.hVal.getUnhinderedValue());
        assertEquals(5+1, this.hVal.getLaneControlValue());
    }

    @Test
    public void testCopyConstructor() {
        HeuristicValues copy = new HeuristicValues(this.hVal);
        copy.setCountValue(20);
        assertEquals(20, copy.getCountValue());
        assertEquals(2, copy.getFurthestValue());
        assertEquals(3, copy.getMovableValue());
        assertEquals(4, copy.getUnhinderedValue());
        assertEquals(5, copy.getLaneControlValue());
        assertNotEquals(20, this.hVal.getCountValue());
    }

}