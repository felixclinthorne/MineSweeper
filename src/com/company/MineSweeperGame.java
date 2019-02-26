package com.company;

import javax.swing.*;
import java.util.Random;

public class MineSweeperGame {

    private Cell[][] board;
    private Cell cell;
    private GameStatus status;
    private int size, mines;
    private int clicked = 0;
    private int wins, losses;

    public MineSweeperGame() {

        status = GameStatus.NotOverYet;
        board = new Cell[10][10];
        setEmpty();

    }

    private void setEmpty() {

        for (int r = 0; r < 10; r++)
            for (int c = 0; c < 10; c++)
                board[r][c] = new Cell(false, false);

    }

    public Cell getCell(int row, int col) {

        return board[row][col];

    }

    /******************************************************************
     * * A method that is called if an adjacent cell holds a mine.
     * After confirming the r and c coordinates are valid (smaller than
     * "this" size) and that they do not contain a mine, it increments
     * its mine count.
     *
     * @param r the row value of the selected cell
     * @param c the column value of the selected cell
     *****************************************************************/
    private void CellCount(int r, int c) {

        if (r >= 0 && r < this.size && c >= 0 && c < this.size) {
            if ((!getCell(r, c).isMine())) this.board[r][c].mines++;
        }
    }

    /******************************************************************
     * A method that is called when the cell at board[r][c] is a mine,
     * and thus CellCount must be called for each possible coordinate
     * surrounding the base cell.
     *
     * @param r represents the row of the base cell and uses it as a
     *          reference
     * @param c represents the column of the base cell and uses it as
     *          a reference
     *****************************************************************/
    public void CheckNeighbors(int r, int c) {

        this.CellCount(r, c - 1);
        this.CellCount(r - 1, c);
        this.CellCount(r - 1, c - 1);
        this.CellCount(r - 1, c + 1);
        this.CellCount(r + 1, c - 1);
        this.CellCount(r, c + 1);
        this.CellCount(r + 1, c);
        this.CellCount(r + 1, c + 1);

    }

    /******************************************************************
     * If the cell specified at (r, c) is on the board, exposed, and
     * no surrounding cell has a mine, it returns true.  Otherwise, 
     * exposed is false.
     * 
     * @param r the row value
     * @param c the column value
     * @return exposed, whether the cell is valid or not
     *****************************************************************/
    private boolean ExposedInterior(int r, int c) {
        boolean exposed = false;
        if (r < this.size && r >= 0 && c < this.size && c >= 0) {
            if (this.getCell(r, c).isExposed() &&
                    this.getCell(r, c).mines == 0)
                exposed = true;
        }
        return exposed;
    }

    /******************************************************************
     * A method that checks the condition of ExposedInterior for each
     * cell around the cell at (r, c) to determine if any of the 8
     * are an exposed cell not on the boundaries of the board.  If so,
     * it returns true.  Otherwise, it returns false.
     *****************************************************************/
    private boolean ExposedNeighbor(int r, int c) {

        if (this.ExposedInterior(r, c - 1)) {
            return true;
        }
        if (this.ExposedInterior(r - 1, c)) {
            return true;
        }
        if (this.ExposedInterior(r - 1, c - 1)) {
            return true;
        }
        if (this.ExposedInterior(r - 1, c + 1)) {
            return true;
        }
        if (this.ExposedInterior(r + 1, c + 1)) {
            return true;
        }
        if (this.ExposedInterior(r - 1, c + 1)) {
            return true;
        }
        if (this.ExposedInterior(r, c + 1)) {
            return true;
        }
        if (this.ExposedInterior(r + 1, c)) {
            return true;
        } else {
            return false;
        }
    }

    /******************************************************************
     * A method that reveals an empty cell that is not exposed
     * but is next to an exposed cell not on the edge of the board.
     * If exposedAdjacent is true, meaning the cell is not flagged and 
     * does not contain a mine, ExposedNeighbor is called and the 
     * condition remains true.  If it is unable to change a cell, false
     * is returned.
     *****************************************************************/
    private boolean ExposeThisAdjacent(int r, int c) {
        boolean exposeAdjacent = false;
        if (!this.board[r][c].isFlagged() && !this.board[r][c].isMine()) {
            if (!this.board[r][c].isExposed() && this.ExposedNeighbor(r, c)) {
                this.board[r][c].setExposed(true);
                exposeAdjacent = true;
            }
        } else {
            return false;
        }
        return exposeAdjacent;
    }

    /******************************************************************
     * The method creates a boolean expand and sets it to true.  While
     * it is true (while the cells are still expanding), a for loop
     * calls the ExposedThisAdjacent method for each cell and stops
     * once no more expansion can be done.
     *****************************************************************/
    private void ExposeAllAdjacent() {
        boolean expand = true;
        while (expand) {
            for (int r = 0; r < this.size; r++) {
                for (int c = 0; c < this.size; c++) {
                    expand = this.ExposeThisAdjacent(r, c);
                }
            }
        }
    }

    /******************************************************************
     * A method that checks the cell at [row][col] after the used has
     * clicked the JButton.  If the cell is not exposed but flagged,
     * the method will do nothing.  If it is not flagged and not a
     * mine but adjacent to a mined cell, only that cell will be
     * revealed.  If cells within its eight possible directions are
     * unexposed and not a mine, nor next to a mine, it will reveal
     * all of those.  Finally, the GameStatus method is used to
     * find the appropriate status and set.
     *****************************************************************/
    public void select(int row, int col) {

        if (!this.board[row][col].isExposed()) {
            if (this.board[row][col].isFlagged()) {
                return;
            }
            if (!this.board[row][col].isMine() &&
                    this.board[row][col].getMines() > 0) {
                this.board[row][col].setExposed(true);
            }
            if (!this.board[row][col].isMine() &&
                    this.board[row][col].getMines() == 0) {
                this.board[row][col].setExposed(true);
                this.ExposeAllAdjacent();
            }
        }
        status = this.GameStatus(row, col);
    }
    /******************************************************************
     * A method that checks the status of the "this" game and evaluates
     * whether it was been won, lost, or is still continuing.  If the
     * cell clicked at the index contains a mine, the game is
     * automatically lost, reset, and the amount of losses increments.
     * Then a for loop evaluates each cell to determine if it is not
     * yet revealed and not containing a mine.  If it finds any, the
     * game is not over.  However, if all that can be exposed are,
     * the game has been won.
     *
     * @param row represents row value
     * @param col represents column value
     * @return GameStatus.(enum option) depending on the outcome of the
     * game.
     *
     *****************************************************************/
    private GameStatus GameStatus(int row, int col) {

        if (this.board[row][col].isMine()) {
            this.reset();
            this.losses++;
            return GameStatus.Lost;
        }
        for (int r = 0; r < this.size; r++) {
            for (int c = 0; c < this.size; c++) {
                if (!this.board[r][c].isMine() && !this.board[r][c].isExposed()) {
                    return GameStatus.NotOverYet;
                }
            }
        }
        this.reset();
        this.wins++;
        return GameStatus.Won;
    }

    public GameStatus getGameStatus() {

        return status;

    }

    public void reset() {
        //resets game
        status = GameStatus.NotOverYet;
        setEmpty();
        layMines(this.getMines(), this.getSize());

    }

    /******************************************************************
     * A method that sets size equal to sizeGame, mines equal to
     * mineCount, and creates a random cell.  In a for loop, it
     * goes through a random amount of cells less than the
     * preselected mineCount and if the cell does not already
     * contain a mine, it places one.
     *
     * @param mineCount the amount of specified mines by user
     * @param sizeGame the size of the board specified by user
     *****************************************************************/
    public void layMines(int mineCount, int sizeGame) {

        size = sizeGame;
        mines = mineCount;

        Random random = new Random();

        for (int i = 0; i < mineCount; i++) {

            int c = random.nextInt(size);
            int r = random.nextInt(size);

            if (!this.board[r][c].isMine()) {
                this.board[r][c].setMine(true);
                this.CheckNeighbors(r, c);
            }

            board[r][c].setMine(true);

        }
    }

    private int getSize() {

        return size;

    }

    private int getMines() {

        return mines;

    }
}