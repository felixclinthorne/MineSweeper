package com.company;

public class Cell {
    private boolean isExposed; //if true, cell shows its mineCount
    private boolean isMine; //if true, cell has a mine
    private boolean isFlagged; //if true, cell at (r, c) is flagged
    private int flaggedCount = 0; //amount of flagged cells
    private int exposedCount = 0; //amount of exposed cells
    public int mines; //amount of adjacent cells with a mine

    public Cell(boolean exposed, boolean mine) {
        isExposed = exposed;
        isMine = mine;
    }

    public boolean isExposed() { return isExposed; }

    public void setExposed(boolean exposed) {
        isExposed = exposed;
        if (exposed) exposedCount++;
        else exposedCount--;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        this.isMine = mine;
    }

    public int getMines() {
        return mines;
    }

    public void setFlagged(boolean mine) {
        isFlagged = mine;
        if (mine) flaggedCount++;
        else flaggedCount--;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public int getFlaggedCount(){ return flaggedCount; }

    public int getExposedCount() { return exposedCount; }
}
