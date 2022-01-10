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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import ConwayGameEngine.Cell;
import ConwayGameEngine.ConwayGame;

public class GameOfLifeView extends SurfaceView implements Runnable {

    private final int aliveColor = Color.WHITE;
    private final int deadColor = Color.BLACK;

    private final Rect rect = new Rect();
    private final Paint cellPaint = new Paint();
    private int cellWidth = -1;
    private int cellHeight = -1;
    private Thread thread;
    private boolean isRunning = false;
    private ConwayGame game;
    // forces a redraw of all cells once at startup to show existing cells, without the need to have user interaction to redraw
    private SurfaceHolder.Callback oneTimeRedrawCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            drawCells();
            getHolder().removeCallback(this);
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        }
    };

    public GameOfLifeView(Context context) {
        super(context);
    }

    public GameOfLifeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void run() {

        while (isRunning) {
            if (!getHolder().getSurface().isValid())
                continue;

            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            }

            boolean ticked = game.tickGeneration();
            drawCells();

            if (!ticked) {
                stop();
            }
        }
    }

    public void start() {
        if (isRunning)
            return;

        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {

        if (!isRunning) return;

        isRunning = false;
        while (true) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
            break;
        }
    }

    public void reset() {
        game.reset();
        drawCells();
    }

    public void initWorld(ConwayGame game) {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        cellWidth = point.x / game.getColumns();
        cellHeight = point.y / game.getRows();

        this.game = game;

        getHolder().addCallback(oneTimeRedrawCallback);
    }

    private void drawCells() {
        Canvas canvas = getHolder().lockCanvas();

        Log.println(Log.DEBUG, "drawCells", "Drawing now");
        for (int i = 0; i < game.getRows(); i++) {
            int top = i * cellHeight;
            int bottom = (i + 1) * cellHeight;
            for (int j = 0; j < game.getColumns(); j++) {
                Cell cell = game.getCell(i, j);
                int left = j * cellWidth;
                int right = (j + 1) * cellWidth;
                rect.set(left, top, right, bottom);
                //Log.println(Log.DEBUG, "drawCells", "Drawing now:" + left + " " + top + " " + right + " " + bottom);
                cellPaint.setColor(cell.isAlive() ? aliveColor : deadColor);
                canvas.drawRect(rect, cellPaint);
            }
        }

        getHolder().unlockCanvasAndPost(canvas);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.println(Log.DEBUG, "ACTION_DOWN", "down ...");
                return true;

            case MotionEvent.ACTION_UP:
                Log.println(Log.DEBUG, "ACTION_UP", "up ...");

                int row = (int) (event.getY() / cellHeight);
                int col = (int) (event.getX() / cellWidth);
                game.getCell(row, col).invert();
                drawCells();
                return true;
        }

        return super.onTouchEvent(event);
    }

    public boolean isRunning() {
        return isRunning;
    }
}