package com.company;

import javax.swing.*;

public class MineSweeperGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("MineSweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MineSweeperPanel panel = new MineSweeperPanel();
        frame.getContentPane().add(panel);
        frame.setSize(800, 400);
        frame.setVisible(true);
    }
}

