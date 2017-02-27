package experiments;

import board.Position;
import board.Rules;
import board.State;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jonni on 2/27/2017.
 */
public class StatisticsTest {

    private Statistics stats;

    @Before
    public void setUp() throws Exception {
        this.stats = new Statistics();
    }

    @Test
    public void latexTableTest() {
        LaTeX latex = new LaTeX();
        for (int i = 1; i < 10; i++) {
            this.stats.reset(i);
            for (int j = 0; j < (int)(Math.random() * 10) + 5; j++) {
                this.stats.addExpansion();
            }
            for (int j = 0; j < 10; j++) {
                this.stats.updateDepth((int)(Math.random() * i) + 1);
            }
            try {
                Thread.sleep((int)(Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.stats.calculateTime();
            latex.addRow(this.stats);
        }
        latex.createTable("Test table", "tsttab");
    }

    @Test
    public void latexStatePictureTest() {
        Rules r = new Rules(10,10,5);
        State s  = new State(r);
        LaTeX.drawState(s, r, "Test Figure", "tstfig", 0.5);
    }

    @Test // Not really a test, just avoiding using the main method.
    public void latexEvaluationPictures1() {
        Rules r = new Rules(6,6,5);
        State s = new State(r);
        s.getWhite().clear();
        s.getBlack().clear();

        s.getBlack().add(new Position(4,2));
        s.getBlack().add(new Position(5,2));
        s.getWhite().add(new Position(2,2));
        s.getWhite().add(new Position(1,4));
        s.getWhite().add(new Position(3,3));

        LaTeX.drawState(s, r, "Count value evaluation", "evalCnt", 0.5);
    }

    @Test // Not really a test, just avoiding using the main method.
    public void latexEvaluationPictures2() {
        Rules r = new Rules(6,6,5);
        State s = new State(r);
        s.getWhite().clear();
        s.getBlack().clear();

        s.getWhite().add(new Position(4,4));
        s.getWhite().add(new Position(4,2));
        s.getBlack().add(new Position(5,2));

        LaTeX.drawState(s, r, "Furthest value evaluation", "evalFar", 0.5);
    }

    @Test // Not really a test, just avoiding using the main method.
    public void latexEvaluationPictures3() {
        Rules r = new Rules(6,6,5);
        State s = new State(r);
        s.getWhite().clear();
        s.getBlack().clear();


        s.getWhite().add(new Position(4,4));
        s.getBlack().add(new Position(4,5));

        s.getBlack().add(new Position(1,6));
        s.getWhite().add(new Position(2, 5));

        s.getWhite().add(new Position(3,3));

        LaTeX.drawState(s, r, "Can move value evaluation", "evalCnMv", 0.5);
    }

    @Test // Not really a test, just avoiding using the main method.
    public void latexEvaluationPictures4() {
        Rules r = new Rules(6,6,5);
        State s = new State(r);
        s.getWhite().clear();
        s.getBlack().clear();

        s.getBlack().add(new Position(1,5));
        s.getWhite().add(new Position(2,1));

        s.getBlack().add(new Position(4,3));
        s.getWhite().add(new Position(4,4));

        s.getWhite().add(new Position(6,1));

        LaTeX.drawState(s, r, "Unhindered value evaluation", "evalUnH", 0.5);
    }

    @Test // Not really a test, just avoiding using the main method.
    public void latexEvaluationPictures5() {
        Rules r = new Rules(6,6,5);
        State s = new State(r);
        s.getWhite().clear();
        s.getBlack().clear();

        for (int i = 0; i < 6; i++) {
            Position p = new Position((int)(Math.random() * 6) + 1,(int)(Math.random() * 5) + 2);
            while (s.getBlack().contains(p)) {
                p = new Position((int)(Math.random() * 6) + 1,(int)(Math.random() * 5) + 2);
            }
            s.getBlack().add(p);
        }

        for (int i = 0; i < 6; i++) {
            Position p = new Position((int)(Math.random() * 6) + 1,(int)(Math.random() * 5) + 1);
            while (s.getWhite().contains(p)) {
                p = new Position((int)(Math.random() * 6) + 1,(int)(Math.random() * 5) + 1);
            }
            s.getWhite().add(p);
        }

        LaTeX.drawState(s, r, "Lane control value evaluation", "evalLaCtrl", 0.5);
    }

    @Test // Not really a test, just avoiding using the main method.
    public void latexEvaluationPictures6() {
        Rules r = new Rules(6,6,5);
        State s = new State(r);
        s.getWhite().clear();
        s.getBlack().clear();

        s.getBlack().add(new Position(1,6));
        s.getWhite().add(new Position(6,4));
        s.getWhite().add(new Position(2,1));
        s.getWhite().add(new Position(3,1));
        s.getWhite().add(new Position(4,1));
        s.getBlack().add(new Position(2,2));
        s.getBlack().add(new Position(4,2));

        LaTeX.drawState(s, r, "Win next special scenario", "winNxtSpec", 0.5);
    }

    @Test // Not really a test, just avoiding using the main method.
    public void latexEvaluationPictures7() {
        Rules r = new Rules(6,6,5);
        State s = new State(r);
        s.getWhite().clear();
        s.getBlack().clear();

        s.getBlack().add(new Position(1,6));
        s.getBlack().add(new Position(3,6));
        s.getWhite().add(new Position(2,5));
        s.getWhite().add(new Position(4,5));
        s.getBlack().add(new Position(4,2));

        LaTeX.drawState(s, r, "Win next for both players", "winNxt", 0.5);
    }
}