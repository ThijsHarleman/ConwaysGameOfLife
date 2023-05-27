package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Author: Thijs Harleman
 * Created at 11:55 on 26 May 2023
 * Purpose:
 */
public class GamePanel extends JPanel implements ActionListener {
    // Display settings
    private static final int SCREEN_WIDTH = 1080;
    private static final int SCREEN_HEIGHT = 720;
    private static final int UNIT_SIZE = 5;
    private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

    // Colour settings
    private static final Color BACKGROUND_COLOR = Color.black;
    private static final Color CELL_COLOR = Color.lightGray;

    // Game settings
    private static final int TIMER_DELAY = 200;
    private static final int CHANCE_TO_SPAWN = 15;
    private static final int NEIGHBORS_NUMBER_TO_REMAIN_OR_REVIVE = 3;
    private static final int NEIGHBORS_NUMBER_TO_REMAIN = 2;

    // Setup variables
    private final Random RANDOM;
    private boolean[] cells = new boolean[GAME_UNITS];

    GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(BACKGROUND_COLOR);
        this.setFocusable(true);
        RANDOM = new Random();
        startGame();
    }

    public void startGame() {
        cells = fillRandomBoard();
        new Timer(TIMER_DELAY, this).start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics) {
        graphics.setColor(CELL_COLOR);

        for (int cell = 0; cell < GAME_UNITS; cell++) {
            if (cells[cell]) {
                int yCoordinate = ((cell * UNIT_SIZE) / SCREEN_WIDTH) * UNIT_SIZE;
                int xCoordinate = (cell * UNIT_SIZE) % SCREEN_WIDTH;

                graphics.fillRect(xCoordinate, yCoordinate, UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    public boolean[] fillRandomBoard() {
        for (int cell = 0; cell < GAME_UNITS; cell++) {
            cells[cell] = spawnAliveCell();
        }
        return cells;
    }

    public boolean spawnAliveCell() {
        return RANDOM.nextInt(100) < CHANCE_TO_SPAWN;
    }

    public boolean[] determineNewBoard() {
        boolean[] newBoard = new boolean[GAME_UNITS];

        for (int cell = 0; cell < GAME_UNITS; cell++) {
            newBoard[cell] = determineCellState(cell);
        }

        return newBoard;
    }

    public boolean determineCellState(int cell) {
        int numberOfAliveNeighbours = getNumberOfAliveNeighbors(getNeighbors(cell));

        if (numberOfAliveNeighbours == NEIGHBORS_NUMBER_TO_REMAIN_OR_REVIVE) {
            return true;
        } else return cells[cell] && numberOfAliveNeighbours == NEIGHBORS_NUMBER_TO_REMAIN;
    }

    public int[] getNeighbors(int cell) {
        int[] neighbors = new int[8];

        for (int neighbor = 0; neighbor < 3; neighbor++) {
            neighbors[neighbor] = cell - 1 + neighbor - (SCREEN_WIDTH / UNIT_SIZE);
            neighbors[neighbor + 5] = cell - 1 + neighbor + (SCREEN_WIDTH / UNIT_SIZE);
        }
        neighbors[3] = cell - 1;
        neighbors[4] = cell + 1;

        if (cell < SCREEN_WIDTH / UNIT_SIZE) {
            for (int neighbor = 0; neighbor < 3; neighbor++) {
                neighbors[neighbor] += GAME_UNITS;
            }
        } else if (cell >= GAME_UNITS - (SCREEN_WIDTH / UNIT_SIZE)) {
            for (int neighbor = 5; neighbor < 8; neighbor++) {
                neighbors[neighbor] -= GAME_UNITS;
            }
        }

        if (cell % (SCREEN_WIDTH / UNIT_SIZE) == 0) {
            neighbors[0] += (SCREEN_WIDTH / UNIT_SIZE);
            neighbors[3] += (SCREEN_WIDTH / UNIT_SIZE);
            neighbors[5] += (SCREEN_WIDTH / UNIT_SIZE);
        } else if (cell % (SCREEN_WIDTH / UNIT_SIZE) == (SCREEN_WIDTH / UNIT_SIZE) - 1) {
            neighbors[2] -= (SCREEN_WIDTH / UNIT_SIZE);
            neighbors[4] -= (SCREEN_WIDTH / UNIT_SIZE);
            neighbors[7] -= (SCREEN_WIDTH / UNIT_SIZE);
        }
        return neighbors;
    }

    public int getNumberOfAliveNeighbors(int[] neighbors) {
        int numberOfAliveNeighbors = 0;

        for (int neighbor : neighbors) {
            if (cells[neighbor]) {
                numberOfAliveNeighbors++;
            }
        }

        return numberOfAliveNeighbors;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        cells = determineNewBoard();
        repaint();
    }
}