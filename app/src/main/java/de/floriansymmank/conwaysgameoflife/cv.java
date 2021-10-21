package de.floriansymmank.conwaysgameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Random;

public class cv extends View {
    Boolean[][] pattern;

    public cv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        Random random = new Random();
        int cols = 250;
        int rows = 250;
        pattern = new Boolean[cols][rows];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                pattern[i][j] = random.nextBoolean();
            }
        }
    }

    Rect r = new Rect();
    Paint p = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int cellWidth = getWidth() / pattern[0].length;
        int cellHeight = getHeight() / pattern.length;

        for (int i = 0; i < pattern.length; i++) {
            int top = i * cellHeight;
            int bottom = (i + 1) * cellHeight;
            for (int j = 0; j < pattern[0].length; j++) {
                int left = j * cellWidth;
                int right = (j + 1) * cellWidth;
                int alpha = pattern[i][j] ? 0 : 255;

                r.set(left, top, right, bottom);
                p.setARGB(alpha, 0, 0, 0);
                canvas.drawRect(r, p);
            }
        }
    }
}
