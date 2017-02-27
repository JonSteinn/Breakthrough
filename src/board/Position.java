package board;

/**
 * Created by Jonni on 2/18/2017.
 *
 * A class for positions in the game.
 */
public class Position {

    private int x;
    private int y;

    /**
     * Constructor for (x,y) coordinates.
     *
     * @param x int
     * @param y int
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get row position.
     *
     * @return int
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get column position.
     *
     * @return int
     */
    public int getY() {
        return this.y;
    }

    // {left,center,right]

    /**
     * Returns the 3 positions in front of (+y) white's current position.
     *
     * @return Position[]
     */
    public Position[] frontOfWhite() {
        return new Position[] {
                new Position(this.x - 1, this.y + 1),
                new Position(this.x, this.y + 1),
                new Position(this.x + 1, this.y + 1)
        };
    }

    /**
     * Returns the 3 positions in front of (-y) black's current position.
     *
     * @return Position[]
     */
    public Position[] frontOfBlack() {
        return new Position[] {
                new Position(this.x - 1, this.y - 1),
                new Position(this.x, this.y - 1),
                new Position(this.x + 1, this.y - 1)
        };
    }

    @Override
    public int hashCode() {
        return 31 * this.x + this.y;
    }

    @Override
    public boolean equals(Object o) {
        Position other = (Position)o;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }
}