package model;

import javax.swing.*;

/**
 * Author: Thijs Harleman
 * Created at 11:55 on 26 May 2023
 * Purpose:
 */
public class GameFrame extends JFrame {
    public GameFrame() {
        this.add(new GamePanel());
        this.setTitle("Conway's Game Of Life");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
