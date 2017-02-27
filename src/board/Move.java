package board;

/**
 * Created by Jonni on 2/18/2017.
 *
 * A class representing a single move in Breakthrough.
 */
public class Move {
    private Position from;
    private Position to;

    /**
     * A constructor for two positions.
     *
     * @param from Position
     * @param to Position
     */
    public Move(Position from, Position to) {
        this.from = from;
        this.to = to;
    }

    /**
     * A constructor for array of coordinates.
     *
     * @param move int[] (x1,y1,x2,y2)
     */
    public Move(int[] move) {
        this.from = new Position(move[0], move[1]);
        this.to = new Position(move[2], move[3]);
    }

    /**
     * Getter for source.
     *
     * @return Position
     */
    public Position getFrom() {
        return this.from;
    }

    /**
     * Getter for destination.
     *
     * @return Position
     */
    public Position getTo() {
        return this.to;
    }

    @Override
    public String toString() {
        return "(move " + this.from.getX() + " " + this.from.getY() + " " + this.to.getX() + " " + this.to.getY() + ")";
    }

    @Override
    public int hashCode() {
        int hash = this.from.hashCode();
        return (hash << 5) - hash + this.to.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        Move other = (Move)o;
        return this.from.equals(other.from) && this.to.equals(other.to);
    }
}
