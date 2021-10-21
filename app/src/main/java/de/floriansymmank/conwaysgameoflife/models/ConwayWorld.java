package de.floriansymmank.conwaysgameoflife.models;

import android.util.Log;

import java.util.LinkedList;

public class ConwayWorld {
    private final int rows;
    private final int columns;
    private Cell[][] grid;

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

        for (Cell c : aliveCells)
            c.resurrect();

        for (Cell c : deadCells)
            c.die();

        Log.println(Log.DEBUG, "tickGeneration", "aliveCells: " + aliveCells.size() + " deadCells: " + deadCells.size());

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
}
