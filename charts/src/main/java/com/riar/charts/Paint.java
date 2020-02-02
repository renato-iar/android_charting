package com.riar.charts;

import android.graphics.Canvas;
import android.graphics.Path;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class Paint {

    private static final class Internal_PaintCallback implements PaintCallback {
        private final List<Paint> paints  = new ArrayList<>();

        @Nullable
        @Override
        public List<Paint> paintForItemAtIndex(int index, int count, @Nullable BasePlot.DrawContext context) {
            return this.paints;
        }

        Internal_PaintCallback(@NonNull Paint paint) {
            this.paints.add(paint);
        }
    }

    /*package private*/final android.graphics.Paint paint   = new android.graphics.Paint();

    public final void onPaint(@NonNull Canvas canvas, @NonNull Path path, @Nullable BasePlot.DrawContext context) {
        canvas.drawPath(path, this.paint);
    }

    @NonNull
    public final PaintCallback asCallback() {
        return new Internal_PaintCallback(this);
    }

    protected Paint() {
        this.paint.setAntiAlias(true);
    }

    public static final class FillPaint extends Paint {
        public FillPaint(int red, int green, int blue, int alpha) {
            super();

            this.paint.setARGB(alpha, red, green, blue);
            this.paint.setStyle(android.graphics.Paint.Style.FILL);
        }
        public FillPaint(@ColorInt int color) {
            this.paint.setColor(color);
            this.paint.setStyle(android.graphics.Paint.Style.FILL);
        }
    }

    public static final class StrokePaint extends Paint {
        public StrokePaint(int red, int green, int blue, int alpha, float width) {
            super();

            this.paint.setARGB(alpha, red, green, blue);
            this.paint.setStyle(android.graphics.Paint.Style.STROKE);
            this.paint.setStrokeWidth(width);
        }
    }

}
