package de.floriansymmank.conwaysgameoflife.models;

import android.util.Log;

public class Cell {
    private final int x;
    private final int y;
    private boolean alive = false;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void die() {
        alive = false;
    }

    public void resurrect() {
        alive = true;
    }

    public void invert() {
        Log.println(Log.DEBUG, "resurrect", "inverting x:" + x + " y:" + y);
        alive = !alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
