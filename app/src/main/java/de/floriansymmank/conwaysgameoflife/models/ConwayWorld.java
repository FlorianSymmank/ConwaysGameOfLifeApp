package de.floriansymmank.conwaysgameoflife.models;

import android.util.Log;

import com.google.gson.Gson;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class ConwayWorld {

    public final static String RESURRECTION_SCORE = "RESURRECTION_SCORE";
    public final static String DEATH_SCORE = "DEATH_SCORE";
    public final static String GENERATION_SCORE = "GENERATION_SCORE";
    public static final String ISUNIQUE = "isUnique";
    private final int rows;
    private final int columns;
    private final Cell[][] grid;

    private final ScoreHolder scores = new ScoreHolder();
    private final HashSet<String> states = new HashSet<>();

    private final List<PropertyChangeListener> listener = new LinkedList<>();

    private boolean isUnique = true;

    public ConwayWorld(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        grid = new Cell[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
    }

    public ConwayWorld(int rows, int columns, String seed) {
        this.rows = rows;
        this.columns = columns;
        grid = new Cell[rows][columns];

        //TODO: do something with seed
    }

    /**
     * Returns cell of given coordinate
     *
     * @param row
     * @param column
     * @return
     */
    public Cell getCell(int row, int column) {
        return grid[column][row];
    }

    /**
     * Computes next generation, all births and deaths occur simultaneously.
     */
    public void tickGeneration() {
        Log.println(Log.DEBUG, "tickGeneration", "new Gen");

        LinkedList<Cell> aliveCells = new LinkedList<Cell>();
        LinkedList<Cell> deadCells = new LinkedList<Cell>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell c = getCell(i, j);
                int neighbours = countAliveNeighboursOf(i, j);

                // Any cell with three live neighbours is alive.
                if (neighbours == 3) {
                    aliveCells.add(c);
                    continue;
                }

                // Any live cell with two live neighbours survives.
                if (neighbours == 2 && c.isAlive()) {
                    aliveCells.add(c);
                    continue;
                }

                // All other live cells die in the next generation. Similarly, all other dead cells stay dead.
                deadCells.add(c);
            }
        }

        int deadToAliveCount = 0;
        int aliveToDeadCount = 0;

        for (Cell c : aliveCells) {
            if (!c.isAlive()) {
                c.resurrect();
                deadToAliveCount++;
            }
        }

        for (Cell c : deadCells) {
            if (c.isAlive()) {
                c.die();
                aliveToDeadCount++;
            }
        }

        score(deadToAliveCount, aliveToDeadCount);
    }

    private void score(int deadToAliveCount, int aliveToDeadCount) {

        if (isStateUnique()) {
            scores.addScore(ConwayWorld.DEATH_SCORE, aliveToDeadCount);
            scores.addScore(ConwayWorld.RESURRECTION_SCORE, deadToAliveCount);
            scores.addScore(ConwayWorld.GENERATION_SCORE, 1);
        } else if (isUnique)
            notifyListeners(this, ISUNIQUE, this.isUnique, this.isUnique = false);
    }

    /**
     * Counts all alive neighbours(top-left, top, top-right, right, left, bottom-left, bottom, bottom-right) of given cell.
     *
     * @param row
     * @param column
     * @return
     */
    public int countAliveNeighboursOf(int row, int column) {
        int count = 0;

        for (int i = row - 1; i <= row + 1; i++)
            for (int j = column - 1; j <= column + 1; j++) {
                if (i == row && j == column) continue;
                count += isCellAlive(i, j) ? 1 : 0;
            }

        return count;
    }

    /**
     * Returns the cell alive value, if the cell does not exit false will be returned.
     *
     * @param row
     * @param column
     * @return
     */
    private boolean isCellAlive(int row, int column) {
        try {
            return getCell(row, column).isAlive();
        } catch (ArrayIndexOutOfBoundsException ex) {
            return false;
        }
    }

    private boolean isStateUnique() {
        return states.add(stateToBase64());
    }

    private String stateToBase64() {
        String json = new Gson().toJson(grid);
        return Base64.getEncoder().encodeToString(json.getBytes());
    }

    private Cell[][] base64toState(String base64) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        String decodedString = new String(decodedBytes);
        return new Gson().fromJson(decodedString, Cell[][].class);
    }

    private void notifyListeners(Object object, String property, boolean oldValue, boolean newValue) {
        for (PropertyChangeListener name : listener) {
            name.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listener.add(newListener);
    }

    public boolean isUnique() {
        return isUnique;
    }

    public ScoreHolder getScores() {
        return scores;
    }
}
