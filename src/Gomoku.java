import cs251.lab4.*;

import java.util.ArrayList;
import java.util.Random;


public class Gomoku implements GomokuModel {

    /**
     * Book keeping global variables
     */
    public boolean playTurn = true;
    public boolean computerPlayer = false;
    public Square playerSym = Square.CROSS;
    public Square compSym = Square.RING;
    public Square[][] moveArray;
    public int longestRun = 0;
    public Direction longestDirection = Direction.HORIZONTAL;
    public int nextCPUCol = 0;
    public int nextCPURow = 0;
    public int turnsPlayed = 0;

    public int[] longestPoint = new int[2];

    enum Direction {
        HORIZONTAL,
        VERTICAL,
        ASCENDING,
        DESCENDING
    }

    public static void main(String[] args) {
        Gomoku game = new Gomoku();
        if (args.length > 0) {
            game.setComputerPlayer(args[0]);
        }
        GomokuGUI.showGUI(game);
    }


    public int longestAscending(){
        int longestAscending = 0;
        for (int col = 0; col < DEFAULT_NUM_COLS; col++) {
            for (int row = 0; row < DEFAULT_NUM_ROWS; row++) {
                int run = getAscendingDiagonal(Square.CROSS, row, col);
                if (run >= longestAscending ) {
                    longestAscending = run;
                    longestPoint[0] = row;
                    longestPoint[1] = col;
                    longestDirection = Direction.DESCENDING;
                }
            }
        }
        return longestAscending;
    }

    public int longestDescending(){
        int longestDescending = 0;
        for (int col = 0; col < DEFAULT_NUM_COLS; col++) {
            for (int row = 0; row < DEFAULT_NUM_ROWS; row++) {
                int run = getDescendingDiagonal(Square.CROSS, row, col);
                if (run >= longestDescending ) {
                    longestDescending = run;
                    longestPoint[0] = row;
                    longestPoint[1] = col;
                    longestDirection = Direction.DESCENDING;
                }
            }
        }
        return longestDescending;
    }
    public int longestVertical(){
        int longestVertical = 0;
        for (int col = 0; col < DEFAULT_NUM_COLS; col++) {
            for (int row = 0; row < DEFAULT_NUM_ROWS; row++) {
                int run = getVerticalRun(Square.CROSS, row, col);
                if (run >= longestVertical ) {
                    longestVertical = run;
                    longestPoint[0] = row;
                    longestPoint[1] = col;
                    longestDirection = Direction.VERTICAL;
                }
            }
        }
        return longestVertical;
    }

    public int longestHorizontal(){
        int longestHorizontal = 0;
        for (int col = 0; col < DEFAULT_NUM_COLS; col++) {
            for (int row = 0; row < DEFAULT_NUM_ROWS; row++) {
                int run = getHorizontalRun(Square.CROSS, row, col);
                if (run >= longestHorizontal ) {
                    longestHorizontal = run;
                    longestPoint[0] = row;
                    longestPoint[1] = col;
                    longestDirection = Direction.HORIZONTAL;
                }
            }
        }
        return longestHorizontal;
    }

    public int[] point(int row, int col){
        return new int[]{row, col};
    }

    @Override
    public int getNumberOfCols() {
        return DEFAULT_NUM_COLS;
    }

    @Override
    public int getNumberOfRows() {
        return DEFAULT_NUM_ROWS;
    }

    @Override
    public int getNumberInLineForWin() {
        return SQUARES_IN_LINE_FOR_WIN;
    }


    /**
     * This method calls the wim method to determine which player
     * if either have won the game.
     *
     * @return Outcome determined by testing methods
     */
    public Outcome findOutcome() {
        if (!isEmptyBoard())
            return Outcome.DRAW;

        if (isWin(playerSym))
            return Outcome.CROSS_WINS;

        if (isWin(compSym))
            return Outcome.RING_WINS;

        return Outcome.GAME_NOT_OVER;
    }

    /**
     * @param outcome Outcome describing game state
     * @return True if game is won or draw, otherwise false
     */
    private boolean gameOutcomeEnds(Outcome outcome) {
        return !outcome.equals(Outcome.GAME_NOT_OVER);
    }

    /**
     * @param row    player's row choice
     * @param col    player's col choice
     * @param symbol player's symbol (RING, CROSS)
     * @return the outcome of their move
     */
    private Outcome playerMove(int row, int col, Square symbol) {
        playMove(row, col, symbol);
        return findOutcome();
    }

    /**
     * @return the outcomme of the computer's move
     */
    private Outcome computerMove() {
        compMove();
        return findOutcome();
    }

    private Square getCurrentSymbol() {
        return playTurn ? playerSym : compSym;
    }

    private void nextTurn() {
        playTurn = !playTurn;
    }

    @Override
    public Outcome handleClickAtLocation(int row, int col) {
        Outcome outcome = Outcome.GAME_NOT_OVER;
        if (!isLegalMove(row, col)) {
            return outcome;
        }

        if (computerPlayer) {
            outcome = playerMove(row, col, playerSym);
            return gameOutcomeEnds(outcome) ? outcome : computerMove();
        }

        Square symbol = getCurrentSymbol();
        nextTurn();
        return playerMove(row, col, symbol);
    }

    @Override
    public void startNewGame() {
        initializeBoard();
        initializeVars();
    }

    /**
     * initalizing an empty game board as well as resetting
     * all book keeping variables
     */
    public void initializeBoard() {
        moveArray = new GomokuModel.Square[DEFAULT_NUM_ROWS][DEFAULT_NUM_COLS];
        for (int row = 0; row < DEFAULT_NUM_ROWS; row++) {
            for (int col = 0; col < DEFAULT_NUM_COLS; col++) {
                moveArray[row][col] = Square.EMPTY;
            }
        }

    }

    public void initializeVars() {
        playTurn = true;
        playerSym = Square.CROSS;
        compSym = Square.RING;
        longestRun = 0;
        longestPoint = null;
        longestDirection = Direction.HORIZONTAL;
        nextCPUCol = 0;
        nextCPURow = 0;
    }

    /**
     * initalizing an empty game board
     *
     * @return A string representation of the board
     */
    @Override
    public String getBoardAsString() {
        StringBuilder board = new StringBuilder();
        for (int row = 0; row < DEFAULT_NUM_ROWS; row++) {
            for (int col = 0; col < DEFAULT_NUM_COLS; col++) {
                board.append(moveArray[row][col].toChar());
            }
            board.append('\n');
        }
        return board.toString();
    }

    /**
     * This method determines whether or not we are playing against
     * a computer AI ot another human.
     *
     * @param s - "computer" or "none"
     */
    @Override
    public void setComputerPlayer(String s) {
        if (s.equals("computer")) {
            computerPlayer = true;
        } else {
            computerPlayer = false;
        }
    }

    /**
     * This method goes through the board checking for a isWin
     * after every move has been made.
     *
     * @param player - symbol of current player
     * @return boolean - true if current player has won
     * false otherwise
     */
    public boolean isWin(Square player) {
        // Checking for horizontal isWin of current player
        //left to right
        for (int j = 0; j < DEFAULT_NUM_COLS ; j++) {
            for (int i = 0; i < DEFAULT_NUM_ROWS; i++) {
                int run = getHorizontalRun(player, i, j);
                if (isRunWin(run)) {
                    return true;
                }
            }
        }

        // Checking for vertical isWin of current player
        // top to bottom
        for (int i = 0; i < DEFAULT_NUM_ROWS ; i++) {
            for (int j = 0; j < DEFAULT_NUM_COLS; j++) {
                int run = getVerticalRun(player, i, j);
                if (isRunWin(run)) {
                    return true;
                }
            }
        }

        // Checking for diagonal isWin of current player
        // We have to check in 2 directions:
        // right to left, top to bottom
        for (int i = 0; i < DEFAULT_NUM_ROWS; i++) {
            for (int j = 0; j < DEFAULT_NUM_COLS ; j++) {
                int run = getDescendingDiagonal(player, i, j);
                if (isRunWin(run)) {
                    return true;
                }
            }
        }

        // right to left, bottom to top
        for (int i = 0; i < DEFAULT_NUM_ROWS; i++)
            for (int j = 0; j < DEFAULT_NUM_COLS; j++) {
                int run = getAscendingDiagonal(player, i, j);
                if (isRunWin(run)) {
                    return true;
                }
            }

        return false;
    }

    /**
     * This method simply checks if there are any empty spaces
     * left on the board.
     *
     * @return boolean true if there are empty spaces
     * false if there are no empty spaces
     */
    public boolean isEmptyBoard() {

        for (int row = 0; row < DEFAULT_NUM_ROWS; row++) {
            for (int col = 0; col < DEFAULT_NUM_COLS; col++) {
                if (moveArray[row][col] == Square.EMPTY) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * This method places the computer piece on the board
     * once a suitable location has been found.
     */
    public void compMove() {
        findCPUMove();
        playMove(nextCPURow, nextCPUCol, compSym);
    }

    /**
     * This method checks if the run of same player pieces is
     * a isWin (5 in a row).
     *
     * @param run count of player pieces in a row
     * @return boolean true if run equal to 5
     * false if run is less than 5
     */
    public boolean isRunWin(int run) {
        return run == 5;
    }

    /**
     * This method checks all the columns for the longest run of
     * current players pieces.
     *
     * @param currentPlayer player symbol
     * @param i             row index
     * @param j             col index
     * @return count of longest consecutive player pieces
     */
    public int getVerticalRun(Square currentPlayer, int i, int j) {
        int run = 0;
        for (int di = 0; di < 5; di++) {
            // This is the important part
            run += count(currentPlayer, di, 0, i, j);
        }
        return run;
    }

    /**
     * This method checks all the rows for the longest run of
     * current players pieces.
     *
     * @param currentPlayer player symbol
     * @param row             col index
     * @param col             row index
     * @return count of longest consecutive player pieces
     */
    public int getHorizontalRun(Square currentPlayer, int row, int col) {
        int run = 0;
        for (int dcol = 0; dcol < 5; dcol++) {
            run += count(currentPlayer, 0, dcol, row, col);
        }
        return run;
    }

    /**
     * This method checks the ascending diagonals (right to left,
     * bottom to top) for the longest run of current players pieces.
     *
     * @param currentPlayer player symbol
     * @param i             row index
     * @param j             col index
     * @return count of longest consecutive player pieces
     */
    public int getAscendingDiagonal(Square currentPlayer, int i, int j) {
        int run = 0;
        for (int dv = 0; dv < 5; dv++) {
            run += count(currentPlayer, -dv, -dv, i, j);
        }
        return run;
    }

    /**
     * This method checks the descending diagonals (right to left,
     * top to bottom) for the longest run of current players pieces.
     *
     * @param currentPlayer player symbol
     * @param i             row index
     * @param j             col index
     * @return count of longest consecutive player pieces
     */
    public int getDescendingDiagonal(Square currentPlayer, int i, int j) {
        int run = 0;
        for (int dv = 0; dv < 5; dv++) {
            run += count(currentPlayer, -dv, dv, i, j);
        }
        return run;
    }

    /**
     * This method counts a run of consecutive player pieces
     *
     * @param currentPlayer player symbol
     * @param drow          row change
     * @param dcol          column change
     * @param row           rows
     * @param col           column
     * @return count of consecutive player pieces
     */
    public int count(Square currentPlayer, int drow, int dcol, int row, int col) {
        if (outOfBounds(row + drow, col + dcol)){
            return 0;
        }
        if (moveArray[row + drow][col + dcol] == currentPlayer) {
            return 1;
        }
        return 0;
    }

    public int findLongestRun() {
        int newLongest = 0;
        int[] point = new int[2];

        int longest = longestHorizontal();
        point[0] = longestPoint[0];
        point[1] = longestPoint[1];
        Direction dir = Direction.HORIZONTAL;

        newLongest = longestVertical();
        if (newLongest > longest){
            longest = newLongest;
            point[0] = longestPoint[0];
            point[1] = longestPoint[1];
            dir = Direction.VERTICAL;
        }

        newLongest = longestDescending();
        if (newLongest > longest){
            longest = newLongest;
            point[0] = longestPoint[0];
            point[1] = longestPoint[1];
            dir = Direction.DESCENDING;
        }

        newLongest = longestAscending();
        if (newLongest > longest){
            longest = newLongest;
            point[0] = longestPoint[0];
            point[1] = longestPoint[1];
            dir = Direction.ASCENDING;
        }

        longestDirection = dir;
        longestPoint[0] = point[0];
        longestPoint[1] = point[1];
        return longest;
    }

    /**
     * This method finds a suitable move for the computer AI
     * to make base on the length of consecutive human player pieces
     * and the location before and after the run.
     */
    public void findCPUMove() {
        int row = longestPoint[0], col = longestPoint[1];
        int prevRow = 0, prevCol = 0; //before run
        int nextRow = 0, nextCol = 0; //after run

        boolean prevLocation;
        boolean nextLocation;

        if (longestDirection == Direction.HORIZONTAL) {
            prevRow = row - longestRun;
            prevCol = col;
            nextRow = row + 1;
            nextCol = col;
        } else if (longestDirection == Direction.VERTICAL) {
            prevRow = row;
            prevCol = col - longestRun;
            nextRow = row;
            nextCol = col + 1;
        } else if (longestDirection == Direction.DESCENDING) {
            prevRow = row - longestRun;
            prevCol = col + longestRun;
            nextRow = row + 1;
            nextCol = col - 1;
        } else if (longestDirection == Direction.ASCENDING) {
            prevRow = row - longestRun;
            prevCol = col - longestRun;
            nextRow = row + 1;
            nextCol = col + 1;
        }

        // if true, location is in bounds and unoccupied
        prevLocation = !outOfBounds(prevRow, prevCol) && !occupied(prevRow, prevCol);

        // if true, location is in bounds and unoccupied
        nextLocation = !outOfBounds(nextRow, nextCol) && !occupied(nextRow, nextCol);

        if (prevLocation && nextLocation) {
            nextCPUCol = nextCol;
            nextCPURow = nextRow;
        } else if (prevLocation) {
            nextCPUCol = prevCol;
            nextCPURow = prevRow;
        } else if (nextLocation) {
            nextCPUCol = nextCol;
            nextCPURow = nextRow;
        } else {

            do {
                Random randomRowNumber = new Random();
                Random randomColNumber = new Random();

                int xrow = randomRowNumber.nextInt(DEFAULT_NUM_ROWS);
                int xcol = randomColNumber.nextInt(DEFAULT_NUM_COLS);

                nextCPURow = xrow;
                nextCPUCol = xcol;
            }
            while (moveArray[nextCPURow][nextCPUCol] == Square.EMPTY);
        }


    }


    public boolean isLegalMove(int row, int col) {
        return !outOfBounds(row, col) && !occupied(row, col);
    }

    /**
     * This method determines whether we are out of bounds while
     * checking for a computer AI move
     *
     * @param row row
     * @param col column
     * @return true if we are out of bounds
     * false if we are still in bounds
     */
    public boolean outOfBounds(int row, int col) {
        return
                row < 0 || row >= DEFAULT_NUM_ROWS || col < 0 || col >= DEFAULT_NUM_COLS;
    }

    /**
     * This method determines whether a specific location is occupied.
     *
     * @param row row
     * @param col column
     * @return true if space if occupied
     * false if space is empty
     */
    public boolean occupied(int row, int col) {
        return moveArray[row][col] != Square.EMPTY;
    }


    public void playMove(int row, int col, Square symbol) {
        turnsPlayed += 1;
        moveArray[row][col] = symbol;
    }
}
