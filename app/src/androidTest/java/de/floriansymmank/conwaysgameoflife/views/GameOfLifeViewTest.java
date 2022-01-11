package de.floriansymmank.conwaysgameoflife.views;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import ConwayGameEngine.ConwayGame;
import ConwayGameEngine.ConwayGameImpl;

@RunWith(AndroidJUnit4ClassRunner.class)
public class GameOfLifeViewTest {

    @Test
    public void onTouchEvent() {
        GameOfLifeView view = new GameOfLifeView(ApplicationProvider.getApplicationContext());
        ConwayGame game = new ConwayGameImpl("Hannes", 0, 50, 50);
        view.initWorld(game);

        WindowManager wm = (WindowManager) ApplicationProvider.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        // Cell Top Left
        assertFalse(game.getCell(0, 0).isAlive());

        MotionEvent e = MotionEvent.obtain(1, 1, MotionEvent.ACTION_UP, 0, 0, 0);
        view.onTouchEvent(e);
        assertTrue(game.getCell(0, 0).isAlive());

        e = MotionEvent.obtain(1, 1, MotionEvent.ACTION_UP, 0, 0, 0);
        view.onTouchEvent(e);
        assertFalse(game.getCell(0, 0).isAlive());


        // Cell Bottom Right
        assertFalse(game.getCell(49, 49).isAlive());

        int x = (point.x / 50 * 49) + (point.x / 50 / 2); // last cell x + cell width/2
        int y = (point.y / 50 * 49) + (point.y / 50 / 2); // last cell y + cell height/2

        e = MotionEvent.obtain(1, 1, MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(e);
        assertTrue(game.getCell(49, 49).isAlive());

        e = MotionEvent.obtain(1, 1, MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(e);
        assertFalse(game.getCell(49, 49).isAlive());
    }
}