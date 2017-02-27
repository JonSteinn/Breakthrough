package board;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jonni on 2/18/2017.
 */
public class PositionTest {

    private Position position;

    @Before
    public void setUp() throws Exception {
        position = new Position(4,17);
    }

    @Test
    public void getterTest() {
        assertEquals("x", 4, this.position.getX());
        assertEquals("y", 17, this.position.getY());
    }

    @Test
    public void frontOfTest() {
        assertArrayEquals(
                "White",
                new Position[] { new Position(3, 18), new Position(4, 18), new Position(5, 18) },
                this.position.frontOfWhite()
        );
        assertArrayEquals(
                "White",
                new Position[] { new Position(3, 16), new Position(4, 16), new Position(5, 16) },
                this.position.frontOfBlack()
        );
    }

    @Test
    public void equalsTest() {
        assertTrue(
                "Equals",
                this.position.equals(new Position(this.position.getX(), this.position.getY()))
        );
        assertFalse(
                "Not equals (x)",
                this.position.equals(new Position(this.position.getX() + 1, this.position.getY()))
        );
        assertFalse(
                "Not equals (y)",
                this.position.equals(new Position(this.position.getX(), this.position.getY() + 1))
        );
        assertFalse(
                "Not equals (x,y)",
                this.position.equals(new Position(this.position.getX() + 1, this.position.getY() + 1))
        );
    }

    @Test
    public void hashTest() {
        assertEquals(31 * this.position.getX() + this.position.getY(), this.position.hashCode());
    }

    @Test
    public void toStringTest() {
        assertEquals("(4,17)", this.position.toString());
    }

}