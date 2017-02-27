package experiments;

import board.Position;
import board.Rules;
import board.State;

import java.util.ArrayList;

/**
 * Created by Jonni on 2/25/2017.
 *
 * Used to generate LaTeX tables from statistics in
 * experiments and to draw Tikz figures of states.
 */
public class LaTeX {

    /**
     * Converts a state to a LaTeX figure.
     *
     * @param state State
     * @param rules Rules
     * @param caption String
     * @param label String
     * @param scale double
     */
    public static void drawState(State state, Rules rules, String caption, String label, double scale) {
        System.out.println(
                "\\begin{figure}[H]\n" +
                "\t\\begin{center}\n" +
                "\t\t\\begin{tikzpicture}[thick,scale=" + scale + ", every node/.style={scale=" + scale + "}]\n" +
                "\t\t\t\\draw (0,0) grid (" + rules.width + "," + rules.height + ");"
        );
        for (Position pawn : state.getBlack()) {
            System.out.println(
                    "\t\t\t\\fill (" +
                            (pawn.getX() - 0.5) +
                            "," + (pawn.getY() - 0.5) +
                            ") circle (" +
                            (scale / 2) + ");"
            );
        }
        for (Position pawn : state.getWhite()) {
            System.out.println(
                    "\t\t\t\\draw (" +
                            (pawn.getX() - 0.5) +
                            "," + (pawn.getY() - 0.5) +
                            ") circle (" +
                            (scale / 2) + ");"
            );
        }
        System.out.println(
                "\t\t\\end{tikzpicture}\n" +
                "\t\\end{center}\n" +
                "\t\\caption{" + caption + "}\n" +
                "\t\\label{fig:" + label + "}\n" +
                "\\end{figure}"
        );
    }

    private ArrayList<Statistics> table;

    /**
     * Constructor to initialize data structure to store tables
     */
    public LaTeX() {
        this.table = new ArrayList<>();
    }

    /**
     * Adds a row of statistic to table.
     *
     * @param statistics Statistics
     */
    public void addRow(Statistics statistics) {
        this.table.add(Statistics.copy(statistics));
    }

    /**
     * Prints out a LaTeX table with all given statistics.
     *
     * @param caption String
     * @param label String
     */
    public void createTable(String caption, String label) {
        printTableHeader();
        printTableRows();
        printTableFooter(caption, label);
    }

    /**
     * Prints beginning of table.
     */
    private void printTableHeader() {
        System.out.println("\\begin{table}[H]\n\t\\begin{center}\n\t\t\\begin{tabular}{c|c|c|c}");
        System.out.println("\t\t\tIteration & Expansions & Depth & Time \\\\\n\t\t\t\\hline");
    }

    /**
     * Prints table rows.
     */
    private void printTableRows() {
        for (int i = 0; i < this.table.size(); i++) {
            System.out.println("\t\t\t"
                            + i
                            + " & "
                            + this.table.get(i).getExpansionCounter()
                            + " & "
                            + this.table.get(i).getDepth()
                            + " & "
                            + this.table.get(i).getTime()
                            + " \\\\"
            );
        }
    }

    /**
     * Prints end of table.
     *
     * @param caption String
     * @param label String
     */
    private void printTableFooter(String caption, String label) {
        System.out.println(
                "\t\t\\end{tabular}\n\t\\end{center}\n\t\\caption{"
                + caption
                + "}\n\t\\label{table:"
                + label
                + "}\n\\end{table}");
    }
}
