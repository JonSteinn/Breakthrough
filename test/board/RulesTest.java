package board;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jonni on 2/24/2017.
 */
public class RulesTest {

    private Rules rules;

    @Before
    public void setUp() throws Exception {
        this.rules = new Rules(5, 10, 5);
    }

    @Test
    public void timerTest() {
        assertEquals("Time", 5 * 1000 - Rules.TIME_OFFSET, this.rules.timeLimit);
        assertEquals("Width", 5, this.rules.width);
        assertEquals("Height", 10, this.rules.height);
    }

}