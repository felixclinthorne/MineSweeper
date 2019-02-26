package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MineSweeperPanel extends JPanel {

    private JButton[][] board;
    private JButton quitButton;
    private JButton showMines;

    private JLabel winLoss;
    private JLabel exposed;

    private Cell iCell;

    private int sizeGame; // Player sets game size
    private int setMines; // Player sets mine count
    private int losses = 0;
    private int wins = 0;

    private GridBagConstraints gbc = new GridBagConstraints();

    private MineSweeperGame game;

    public MineSweeperPanel() {

        setLayout(new GridBagLayout());

        // Prompt for entering Game Size at start

        String size = JOptionPane.showInputDialog(null, "Enter number of rows/columns:");
        sizeGame = Integer.parseInt(size);
        if (sizeGame > 10) sizeGame = 10;
        if (sizeGame < 0) sizeGame = 2;

        // Prompt for entering Mine Count at start

        String mines = JOptionPane.showInputDialog(null, "Enter number of mines:");
        setMines = Integer.parseInt(mines);
        if (setMines > 20) setMines = 20;
        if (setMines < 1) setMines = 1;

        // Game Building - Creating Listeners

        ButtonListener listener = new ButtonListener();

        // Following code creates Mouse Listener for flagging mines (Right-click)

        MouseListener mouse = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {

                    for (int r = 0; r < sizeGame; r++)

                        for (int c = 0; c < sizeGame; c++)

                            if (board[r][c] == e.getSource()) {

                                Cell cell = game.getCell(r, c);

                                if (!cell.isFlagged()) {

                                    board[r][c].setText("B");
                                    cell.setFlagged(true);

                                } else {
                                    if (!cell.isMine()) board[r][c].setText(" ");

                                    else {
                                        board[r][c].setText("!");
                                    }

                                    cell.setFlagged(false);

                                }
                            }
                }
            }

            // Following are needed to implement MouseListener
            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };

        // Begin creating game objects...

        game = new MineSweeperGame();
        game.layMines(setMines, sizeGame);

        this.quitButton = new JButton("X");
        this.quitButton.addActionListener(listener);

        // Creates a game board with size determined by player at launch

        board = new JButton[sizeGame][sizeGame];

        // Placing cells...

        for (int row = 0; row < sizeGame; row++)
            for (int col = 0; col < sizeGame; col++) {
                board[row][col] = new JButton("");
                board[row][col].addActionListener(listener);
                board[row][col].addMouseListener(mouse);
                gbc.gridx = row;
                gbc.gridy = col;
                add(board[row][col], gbc);
            }

        displayBoard(sizeGame, sizeGame);

        // Adding remaining elements to the frame...

        gbc.gridx = sizeGame / 2; // Halfway point of game board
        gbc.gridy = sizeGame + 1; // One space below game board

        add(new JLabel("MineSweeper"), gbc);

        gbc.gridy = sizeGame + 2; // Two spaces below game board

        winLoss = new JLabel("Wins: " + wins + " Losses: " + losses);
        add(winLoss, gbc);
        gbc.gridy = sizeGame + 3;

        gbc.gridy = sizeGame + 4; // Three spaces below game board

        add(quitButton, gbc);
        quitButton.setBackground(Color.red);

        // Done creating game objects

    }

    /******************************************************************
     * A method that uses a nested for loop to show the board and cell
     * contents.  Mines are displayed for the purpose of demoing.
     * The text is initialized as blank, and if it contains a mine, an
     * exclamation point is displayed.  The cell is disabled if it is
     * already exposed, and otherwise it is enabled.
     *
     * @param row the row the "this" cell is in
     * @param col the column the "this" cell is in
     *****************************************************************/
    private void displayBoard(int row, int col) {

        for (int r = 0; r < row; r++)
            for (int c = 0; c < col; c++) {
                iCell = this.game.getCell(r, c);
                board[r][c].setText(" "); // Setting blank cell text

                if (iCell.isMine())
                    board[r][c].setText("!"); // Setting mine text

                if (iCell.isExposed()) {
                    board[r][c].setEnabled(false);
                }
                else { board[r][c].setEnabled(true); }
            }

    }

    /******************************************************************
     * A private method that implements ActionListener and adds
     * necessary operations to each button and label.  If the game
     * is lost, a message is displayed informing the user, losses are
     * incremented, and the game is reset to the user's specifications.
     * A similar process is done with a win scenario, but evidently,
     * a different message is displayed.  Wins are then incremented and
     * the game is reset.  If the user chooses to quit, a confirmation
     * window pops up and if "Yes" is selected, the game will quit.
     *****************************************************************/
    private class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            for (int r = 0; r < sizeGame; r++)
                for (int c = 0; c < sizeGame; c++)
                    if (board[r][c] == e.getSource()) {
                        game.select(r, c);
                    }

            displayBoard(sizeGame, sizeGame);

            // Adding functionality to actions and buttons...

            if (game.getGameStatus() == GameStatus.Lost) {
                displayBoard(sizeGame, sizeGame);
                JOptionPane.showMessageDialog(null, "You Lose \n The game will reset");
                losses++;
                winLoss.setText("Wins: " + wins + " Losses: " + losses);
                game.reset();
                displayBoard(sizeGame, sizeGame);
            }

            if (game.getGameStatus() == GameStatus.Won) {
                JOptionPane.showMessageDialog(null, "You Win: all mines have been found!\n The game will reset");
                wins++;
                winLoss.setText("Wins: " + wins + " Losses: " + losses);
                game.reset();
                displayBoard(sizeGame, sizeGame);
            }

            if (quitButton == e.getSource()) {
                int quit = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?");
                if (quit == 0) System.exit(0);
                else {
                    //Do Nothing
                }
            }
        }

    }

}