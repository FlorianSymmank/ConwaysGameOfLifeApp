package de.floriansymmank.conwaysgameoflife.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;

import de.floriansymmank.conwaysgameoflife.models.Cell;
import de.floriansymmank.conwaysgameoflife.models.ConwayWorld;

public class GameOfLifeView extends SurfaceView implements Runnable {

    private final int aliveColor = Color.WHITE;
    private final int deadColor = Color.BLACK;
    private int cols = 200;
    private int rows = 200;
    private int cellWidth = -1;
    private int cellHeight = -1;
    private Rect r = new Rect();
    private Paint p = new Paint();
    private Thread thread;
    private boolean isRunning = false;
    private ConwayWorld world;

    public GameOfLifeView(Context context) {
        super(context);
        initWorld();
    }

    public GameOfLifeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWorld();
    }

    @Override
    public void run() {

        while (isRunning) {
            if (!getHolder().getSurface().isValid())
                continue;

            try {
                Thread.sleep(300);

            } catch (InterruptedException e) {
            }

            Canvas canvas = getHolder().lockCanvas();
            world.tickGeneration();
            drawCells(canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        isRunning = false;
        while (true) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
            break;
        }

    }

    private void initWorld() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        cellWidth = point.x / cols;
        cellHeight = point.y / rows;
        world = new ConwayWorld(rows, cols);

        Log.println(Log.DEBUG, "initWorld", "CellWidth: " + cellWidth + " CellHeight: " + cellHeight + " rows: " + rows + " cols: " + cols);
    }

    private void drawCells(Canvas canvas) {
        Log.println(Log.DEBUG, "drawCells", "Drawing now");
        for (int i = 0; i < rows; i++) {
            int top = i * cellHeight;
            int bottom = (i + 1) * cellHeight;
            for (int j = 0; j < cols; j++) {
                Cell cell = world.getCell(i, j);
                int left = j * cellWidth;
                int right = (j + 1) * cellWidth;
                r.set(left, top, right, bottom);
                //Log.println(Log.DEBUG, "drawCells", "Drawing now:" + left + " " + top + " " + right + " " + bottom);
                p.setColor(cell.isAlive() ? aliveColor : deadColor);
                canvas.drawRect(r, p);
            }
        }
    }

    // We let the user to interact with the Cells of the World
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // we get the coordinates of the touch and we convert it in coordinates for the board
            int row = (int) (event.getY() / cellHeight);
            int col = (int) (event.getX() / cellWidth);

            Log.println(Log.DEBUG, "onTouchEvent", "Touch at row: " + row + " col:" + col);

            // we get the cell associated to these positions
            Cell cell = world.getCell(row, col);
            // we call the invert method of the cell got to change its state
            cell.invert();

            Canvas canvas = getHolder().lockCanvas();
            drawCells(canvas);
            getHolder().unlockCanvasAndPost(canvas);

            invalidate();
        }
        return super.onTouchEvent(event);
    }
}