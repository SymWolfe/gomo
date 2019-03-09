import cs251.lab4.GomokuModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GomokuTest {
    Gomoku gomoku;

    @BeforeEach
    public void setupGomoku() {
        gomoku = new Gomoku();
        gomoku.initializeBoard();
        gomoku.initializeVars();
    }

    @Test
    public void testOccupied() {
        gomoku.playMove(0, 0, GomokuModel.Square.EMPTY);
        assertFalse(gomoku.occupied(0, 0));
        gomoku.playMove(0, 0, GomokuModel.Square.CROSS);
        assertTrue(gomoku.occupied(0, 0));
        gomoku.playMove(0, 0, GomokuModel.Square.RING);
        assertTrue(gomoku.occupied(0, 0));
    }

    @Test
    public void testOutOfBounds() {
        assertTrue(gomoku.outOfBounds(-1, 9));
        assertTrue(gomoku.outOfBounds(0, -1));
        assertTrue(gomoku.outOfBounds(55, 9));
        assertTrue(gomoku.outOfBounds(1, 55));
        assertFalse(gomoku.outOfBounds(1, 9));
        assertFalse(gomoku.outOfBounds(0, 0));
    }

    @Test
    public void testIsLegalMove() {
        assertFalse(gomoku.isLegalMove(-1, 9));
        assertFalse(gomoku.isLegalMove(0, -1));
        assertFalse(gomoku.isLegalMove(55, 9));
        assertFalse(gomoku.isLegalMove(1, 55));
        assertTrue(gomoku.isLegalMove(1, 9));
        assertTrue(gomoku.isLegalMove(0, 0));
        gomoku.playMove(0, 0, GomokuModel.Square.CROSS);
        gomoku.playMove(1, 9, GomokuModel.Square.CROSS);
        assertFalse(gomoku.isLegalMove(1, 9));
        assertFalse(gomoku.isLegalMove(0, 0));
    }

    @Test
    public void testHandleFirstBadClick() {
        assertEquals(gomoku.handleClickAtLocation(-1, 0),
                GomokuModel.Outcome.GAME_NOT_OVER);
        assertTrue(gomoku.playTurn);
    }

    @Test
    public void testHandleClickOccupied() {
        gomoku.moveArray[0][0] = GomokuModel.Square.RING;

        assertEquals(gomoku.handleClickAtLocation(0, 0),
                GomokuModel.Outcome.GAME_NOT_OVER);
        assertEquals(gomoku.moveArray[0][0], GomokuModel.Square.RING);
        assertTrue(gomoku.playTurn);

        assertEquals(gomoku.handleClickAtLocation(0, 0),
                GomokuModel.Outcome.GAME_NOT_OVER);
        assertEquals(gomoku.moveArray[0][0], GomokuModel.Square.RING);
        assertTrue(gomoku.playTurn);

        gomoku.initializeBoard();
        gomoku.moveArray[0][0] = GomokuModel.Square.CROSS;

        assertEquals(gomoku.handleClickAtLocation(0, 0),
                GomokuModel.Outcome.GAME_NOT_OVER);
        assertEquals(gomoku.moveArray[0][0], GomokuModel.Square.CROSS);
        assertTrue(gomoku.playTurn);

        assertEquals(gomoku.handleClickAtLocation(0, 0),
                GomokuModel.Outcome.GAME_NOT_OVER);
        assertEquals(gomoku.moveArray[0][0], GomokuModel.Square.CROSS);
        assertTrue(gomoku.playTurn);

    }

    @Test
    public void testSetComputerPlayer() {
        gomoku.setComputerPlayer("computer");
        assertTrue(gomoku.computerPlayer);

        gomoku.initializeVars();
        gomoku.setComputerPlayer("computesr");
        assertFalse(gomoku.computerPlayer);

        gomoku.initializeVars();
        gomoku.setComputerPlayer("computerIS THE VERY BEST");
        assertFalse(gomoku.computerPlayer);
    }


    @Test
    public void testHandleFirstGoodClick() {
        assertEquals(gomoku.handleClickAtLocation(0, 0),
                GomokuModel.Outcome.GAME_NOT_OVER);
        assertEquals(gomoku.moveArray[0][0], GomokuModel.Square.CROSS);
    }

    @Test
    public void testTwoMovesIfComputerPlayer() {
        gomoku.setComputerPlayer("computer");
        gomoku.handleClickAtLocation(0, 0);
        assertEquals(gomoku.turnsPlayed, 2);
        gomoku.handleClickAtLocation(5, 5);
        assertEquals(gomoku.turnsPlayed, 4);
    }

    @Test
    public void testOneMoveIfMultiPlayer() {
        gomoku.setComputerPlayer("player");

        assertEquals(gomoku.turnsPlayed, 0);

        gomoku.handleClickAtLocation(0, 0);
        assertEquals(gomoku.turnsPlayed, 1);

        gomoku.handleClickAtLocation(5, 5);
        assertEquals(gomoku.turnsPlayed, 2);
    }

    @Test
    public void testHorizontalWin() {
        gomoku.moveArray[0][0] = GomokuModel.Square.RING;
        gomoku.moveArray[0][1] = GomokuModel.Square.RING;
        gomoku.moveArray[0][2] = GomokuModel.Square.RING;
        gomoku.moveArray[0][3] = GomokuModel.Square.RING;
        gomoku.moveArray[0][4] = GomokuModel.Square.RING;
        assertEquals(gomoku.findOutcome(), GomokuModel.Outcome.RING_WINS);

        gomoku.moveArray[0][0] = GomokuModel.Square.CROSS;
        gomoku.moveArray[0][1] = GomokuModel.Square.CROSS;
        gomoku.moveArray[0][2] = GomokuModel.Square.CROSS;
        gomoku.moveArray[0][3] = GomokuModel.Square.CROSS;
        gomoku.moveArray[0][4] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.findOutcome(), GomokuModel.Outcome.CROSS_WINS);
    }

    @Test
    public void testVerticalWin() {
        gomoku.moveArray[0][0] = GomokuModel.Square.RING;
        gomoku.moveArray[1][0] = GomokuModel.Square.RING;
        gomoku.moveArray[2][0] = GomokuModel.Square.RING;
        gomoku.moveArray[3][0] = GomokuModel.Square.RING;
        gomoku.moveArray[4][0] = GomokuModel.Square.RING;
        assertEquals(gomoku.findOutcome(), GomokuModel.Outcome.RING_WINS);

        gomoku.moveArray[0][0] = GomokuModel.Square.CROSS;
        gomoku.moveArray[1][0] = GomokuModel.Square.CROSS;
        gomoku.moveArray[2][0] = GomokuModel.Square.CROSS;
        gomoku.moveArray[3][0] = GomokuModel.Square.CROSS;
        gomoku.moveArray[4][0] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.findOutcome(), GomokuModel.Outcome.CROSS_WINS);
    }

    @Test
    public void testDiagonalAscWin() {
        gomoku.moveArray[5][5] = GomokuModel.Square.RING;
        gomoku.moveArray[4][4] = GomokuModel.Square.RING;
        gomoku.moveArray[3][3] = GomokuModel.Square.RING;
        gomoku.moveArray[2][2] = GomokuModel.Square.RING;
        gomoku.moveArray[1][1] = GomokuModel.Square.RING;
        assertEquals(gomoku.findOutcome(), GomokuModel.Outcome.RING_WINS);

        gomoku.moveArray[5][5] = GomokuModel.Square.CROSS;
        gomoku.moveArray[4][4] = GomokuModel.Square.CROSS;
        gomoku.moveArray[3][3] = GomokuModel.Square.CROSS;
        gomoku.moveArray[2][2] = GomokuModel.Square.CROSS;
        gomoku.moveArray[1][1] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.findOutcome(), GomokuModel.Outcome.CROSS_WINS);
    }

    @Test
    public void testDiagonalDscWin() {
        gomoku.moveArray[5][5] = GomokuModel.Square.RING;
        gomoku.moveArray[4][6] = GomokuModel.Square.RING;
        gomoku.moveArray[3][7] = GomokuModel.Square.RING;
        gomoku.moveArray[2][8] = GomokuModel.Square.RING;
        gomoku.moveArray[1][9] = GomokuModel.Square.RING;
        assertEquals(gomoku.findOutcome(), GomokuModel.Outcome.RING_WINS);

        gomoku.moveArray[5][5] = GomokuModel.Square.CROSS;
        gomoku.moveArray[4][6] = GomokuModel.Square.CROSS;
        gomoku.moveArray[3][7] = GomokuModel.Square.CROSS;
        gomoku.moveArray[2][8] = GomokuModel.Square.CROSS;
        gomoku.moveArray[1][9] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.findOutcome(), GomokuModel.Outcome.CROSS_WINS);
    }

    @Test
    public void testHandleClickWin() {
        gomoku.moveArray[5][5] = GomokuModel.Square.CROSS;
        gomoku.moveArray[4][6] = GomokuModel.Square.CROSS;
        gomoku.moveArray[3][7] = GomokuModel.Square.CROSS;
        gomoku.moveArray[2][8] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.handleClickAtLocation(1, 9), GomokuModel.Outcome.CROSS_WINS);

    }

    @Test
    public void testPoint() {
        int[] expectedPoint = new int[]{0, 1};
        assertEquals(gomoku.point(0, 1)[0], expectedPoint[0]);
        assertEquals(gomoku.point(0, 1)[1], expectedPoint[1]);
    }

    @Test
    public void testLongestHorizontal() {
        int[] actualPoint = new int[]{0, 0};
        gomoku.moveArray[0][0] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestHorizontal(), 1);
        assertEquals(gomoku.longestPoint[0], actualPoint[0]);
        assertEquals(gomoku.longestPoint[1], actualPoint[1]);

        gomoku.moveArray[0][1] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestHorizontal(), 2);
        assertEquals(gomoku.longestPoint[0], actualPoint[0], "row doesn't " +
                "match");
        assertEquals(gomoku.longestPoint[1], actualPoint[1], "col doesn't " +
                "match");

        gomoku.moveArray[0][2] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestHorizontal(), 3);
        assertEquals(gomoku.longestPoint[0], actualPoint[0], "row doesn't " +
                "match");
        assertEquals(gomoku.longestPoint[1], actualPoint[1], "col doesn't " +
                "match");

        gomoku.initializeBoard();
        gomoku.initializeVars();

        actualPoint = new int[]{0, 29};
        gomoku.moveArray[0][29] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestHorizontal(), 1);
        assertEquals(gomoku.longestPoint[0], actualPoint[0]);
        assertEquals(gomoku.longestPoint[1], actualPoint[1]);

        actualPoint = new int[]{0, 28};
        gomoku.moveArray[0][28] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestHorizontal(), 2);
        assertEquals(gomoku.longestPoint[0], actualPoint[0], "row doesn't " +
                "match");
        assertEquals(gomoku.longestPoint[1], actualPoint[1], "col doesn't " +
                "match");

        actualPoint = new int[]{0, 27};
        gomoku.moveArray[0][27] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestHorizontal(), 3);
        assertEquals(gomoku.longestPoint[0], actualPoint[0], "row doesn't " +
                "match");
        assertEquals(gomoku.longestPoint[1], actualPoint[1], "col doesn't " +
                "match");
    }

    @Test
    public void longestVertical() {

        int[] actualPoint = new int[]{29, 0};
        gomoku.moveArray[29][0] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestVertical(), 1);
        assertEquals(gomoku.longestPoint[0], actualPoint[0]);
        assertEquals(gomoku.longestPoint[1], actualPoint[1]);

        actualPoint = new int[]{28, 0};
        gomoku.moveArray[28][0] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestVertical(), 2);
        assertEquals(gomoku.longestPoint[0], actualPoint[0], "row doesn't " +
                "match");
        assertEquals(gomoku.longestPoint[1], actualPoint[1], "col doesn't " +
                "match");

        actualPoint = new int[]{27, 0};
        gomoku.moveArray[27][0] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestVertical(), 3);
        assertEquals(gomoku.longestPoint[0], actualPoint[0], "row doesn't " +
                "match");
        assertEquals(gomoku.longestPoint[1], actualPoint[1], "col doesn't " +
                "match");
    }

    @Test
    public void longestDescending() {
        // Descending -> '/'
        int[] actualPoint = new int[]{29, 0};
        gomoku.moveArray[29][0] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestDescending(), 1);
        assertEquals(gomoku.longestPoint[0], actualPoint[0]);
        assertEquals(gomoku.longestPoint[1], actualPoint[1]);

        gomoku.moveArray[28][1] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestDescending(), 2);
        assertEquals(gomoku.longestPoint[0], actualPoint[0], "row doesn't " +
                "match");
        assertEquals(gomoku.longestPoint[1], actualPoint[1], "col doesn't " +
                "match");

        gomoku.moveArray[27][2] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestDescending(), 3);
        assertEquals(gomoku.longestPoint[0], actualPoint[0], "row doesn't " +
                "match");
        assertEquals(gomoku.longestPoint[1], actualPoint[1], "col doesn't " +
                "match");

        gomoku.initializeBoard();
        gomoku.initializeVars();

        actualPoint = new int[]{0, 29};
        gomoku.moveArray[0][29] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestDescending(), 1);
        assertEquals(gomoku.longestPoint[0], actualPoint[0]);
        assertEquals(gomoku.longestPoint[1], actualPoint[1]);

        actualPoint = new int[]{1, 28};
        gomoku.moveArray[1][28] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestDescending(), 2);
        assertEquals(gomoku.longestPoint[0], actualPoint[0], "row doesn't " +
                "match");
        assertEquals(gomoku.longestPoint[1], actualPoint[1], "col doesn't " +
                "match");

        actualPoint = new int[]{2, 27};
        gomoku.moveArray[2][27] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestDescending(), 3);
        assertEquals(gomoku.longestPoint[0], actualPoint[0], "row doesn't " +
                "match");
        assertEquals(gomoku.longestPoint[1], actualPoint[1], "col doesn't " +
                "match");
    }

    @Test
    public void longestAscending() {
        // Ascending -> '\'
        int[] actualPoint = new int[]{29, 29};
        gomoku.moveArray[29][29] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestAscending(), 1);
        assertEquals(gomoku.longestPoint[0], actualPoint[0]);
        assertEquals(gomoku.longestPoint[1], actualPoint[1]);

        gomoku.moveArray[28][28] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestAscending(), 2);
        assertEquals(gomoku.longestPoint[0], actualPoint[0], "row doesn't " +
                "match");
        assertEquals(gomoku.longestPoint[1], actualPoint[1], "col doesn't " +
                "match");

        gomoku.moveArray[27][27] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestAscending(), 3);
        assertEquals(gomoku.longestPoint[0], actualPoint[0], "row doesn't " +
                "match");
        assertEquals(gomoku.longestPoint[1], actualPoint[1], "col doesn't " +
                "match");

        gomoku.initializeBoard();
        gomoku.initializeVars();

        actualPoint = new int[]{0, 29};
        gomoku.moveArray[0][29] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestDescending(), 1);
        assertEquals(gomoku.longestPoint[0], actualPoint[0]);
        assertEquals(gomoku.longestPoint[1], actualPoint[1]);

        actualPoint = new int[]{1, 28};
        gomoku.moveArray[1][28] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestDescending(), 2);
        assertEquals(gomoku.longestPoint[0], actualPoint[0], "row doesn't " +
                "match");
        assertEquals(gomoku.longestPoint[1], actualPoint[1], "col doesn't " +
                "match");

        actualPoint = new int[]{2, 27};
        gomoku.moveArray[2][27] = GomokuModel.Square.CROSS;
        assertEquals(gomoku.longestDescending(), 3);
        assertEquals(gomoku.longestPoint[0], actualPoint[0], "row doesn't " +
                "match");
        assertEquals(gomoku.longestPoint[1], actualPoint[1], "col doesn't " +
                "match");
    }

}