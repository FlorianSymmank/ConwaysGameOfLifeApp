package de.floriansymmank.conwaysgameoflife.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import de.floriansymmank.conwaysgameoflife.models.Cell;
import de.floriansymmank.conwaysgameoflife.models.ConwayWorld;

public class GameOfLifeView extends SurfaceView implements Runnable {

    private final int aliveColor = Color.WHITE;
    private final int deadColor = Color.BLACK;
    private final int dragColor = Color.YELLOW;
    private final int cols = 50;
    private final int rows = 50;
    private final Rect rect = new Rect();
    private final Paint cellPaint = new Paint();
    private final Paint dragPaint = new Paint();
    private Point startPoint = new Point();
    private int cellWidth = -1;
    private int cellHeight = -1;
    private Thread thread;
    private boolean isRunning = false;
    private boolean isDragging = false;
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
            } catch (InterruptedException ignored) {
            }

            Canvas canvas = getHolder().lockCanvas();
            world.tickGeneration();
            drawCells(canvas);
            getHolder().unlockCanvasAndPost(canvas);
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
        dragPaint.setColor(dragColor);
        dragPaint.setAlpha(150);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        cellWidth = point.x / cols;
        cellHeight = point.y / rows;
        world = new ConwayWorld(rows, cols);

        world.addChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                postToastMessage("Welt ist nicht mehr einzigartig Gen: " + world.getScores());
            }
        });

        // Log.println(Log.DEBUG, "initWorld", "CellWidth: " + cellWidth + " CellHeight: " + cellHeight + " rows: " + rows + " cols: " + cols);
    }

    public void postToastMessage(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                Log.println(Log.DEBUG, "postToastMessage", message);
            }
        });
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
                rect.set(left, top, right, bottom);
                //Log.println(Log.DEBUG, "drawCells", "Drawing now:" + left + " " + top + " " + right + " " + bottom);
                cellPaint.setColor(cell.isAlive() ? aliveColor : deadColor);
                canvas.drawRect(rect, cellPaint);
            }
        }
    }

    private void drawSelection(Canvas canvas, Point p1, Point p2) {
        Log.println(Log.DEBUG, "drawSelection", "Drawing now");
        rect.set(p1.x, p1.y, p2.x, p2.y);
        canvas.drawRect(rect, dragPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                Log.println(Log.DEBUG, "ACTION_MOVE", "Moving ...");
                isDragging = true;
                return true;

            case MotionEvent.ACTION_CANCEL:
                Log.println(Log.DEBUG, "ACTION_CANCEL", "Cancel ...");
                isDragging = false;
                return true;

            case MotionEvent.ACTION_DOWN:
                Log.println(Log.DEBUG, "ACTION_DOWN", "down ...");
                startPoint = new Point((int) event.getX(), (int) event.getY());
                return true;

            case MotionEvent.ACTION_UP:
                Log.println(Log.DEBUG, "ACTION_UP", "up ...");
                Canvas canvas = getHolder().lockCanvas();

                if (!isDragging) {
                    Log.println(Log.DEBUG, "ACTION_UP", "cell ...");
                    int row = (int) (event.getY() / cellHeight);
                    int col = (int) (event.getX() / cellWidth);
                    world.getCell(row, col).invert();
                }

                drawCells(canvas);

                if (isDragging) {
                    Log.println(Log.DEBUG, "ACTION_UP", "drag ...");
                    Point endPoint = new Point((int) event.getX(), (int) event.getY());
                    drawSelection(canvas, startPoint, endPoint);
                }

                getHolder().unlockCanvasAndPost(canvas);
                invalidate();

                isDragging = false;
                return true;
        }

        return super.onTouchEvent(event);
    }

    public boolean isRunning() {
        return isRunning;
    }
}