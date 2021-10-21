package de.floriansymmank.conwaysgameoflife.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Random;

public class CV extends View {

    Boolean[][] pattern;
    Rect r = new Rect();
    Paint p = new Paint();
    int cols = 250;
    int rows = 250;
    Random random = new Random();

    public CV(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        generateImg();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                generateImg();
                invalidate();
            }
        });
    }

    private void generateImg() {
        pattern = new Boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                pattern[i][j] = random.nextBoolean();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        double cellWidth = (double) getWidth() / pattern[0].length;
        double cellHeight = (double) getHeight() / pattern.length;

        for (int i = 0; i < pattern.length; i++) {
            double top = i * cellHeight;
            double bottom = (i + 1) * cellHeight;
            for (int j = 0; j < pattern[0].length; j++) {
                double left = j * cellWidth;
                double right = (j + 1) * cellWidth;
                int alpha = pattern[i][j] ? 0 : 255;

                r.set((int) left, (int) top, (int) right, (int) bottom);
                p.setARGB(alpha, 0, 0, 0);
                canvas.drawRect(r, p);
            }
        }
    }
}
