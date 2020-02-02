package com.riar.charts;

import android.graphics.Canvas;
import android.graphics.Path;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class NonSegmentedPlot<PointType> extends BasePlot {

    // region private
    // region instance constants
    private final List<PointType> points                = new ArrayList<>();
    // endregion constants

    // region instance variables
    private NonSegmentedPaintCallback paint_callback  = null;
    // endregion instance variables
    // endregion private

    // region protected
    // region hooks
    @CallSuper
    protected void hook_OnPointsDidChange(@NonNull List<PointType> points) {
        // This method is a stub
    }
    protected abstract void hook_Draw(@NonNull Path path, @NonNull List<PointType> points, @NonNull DrawContext context);
    // endregion hooks
    // endregion protected

    // region public
    // region types
    // region instance methods
    public final int getPointCount() {
        return this.points.size();
    }
    @NonNull
    public final PointType getPoint(int index) {
        return this.points.get(index);
    }
    public final void setPoints(@Nullable Collection<PointType> points) {
        this.points.clear();
        if (points != null) {
            this.points.addAll(points);
        }
        this.hook_OnPointsDidChange(this.points);
        this.invalidate();
    }

    @Nullable
    public final NonSegmentedPaintCallback getPaintCallback() {
        return this.paint_callback;
    }
    public final void setPaintCallback(@Nullable NonSegmentedPaintCallback callback) {
        this.paint_callback = callback;
        this.invalidate();
    }
    // endregion instance methods
    // endregion public

    // region overrides
    // region BasePlot
    @Override
    void onDraw(@NonNull Canvas canvas, @NonNull DrawContext context) {
        super.onDraw(canvas, context);

        final NonSegmentedPaintCallback callback  = this.getPaintCallback();
        if (callback == null) {
            return;
        }

        final int n                                 = this.getPointCount();
        final List<Paint> paints                  = callback.getPaint(n, context);
        if (paints == null || paints.isEmpty()) {
            return;
        }

        final Path path                             = new Path();
        this.hook_Draw(path, this.points, context);

        for (Paint paint: paints) {
            paint.onPaint(canvas, path, context);
        }

    }
    // endregion BasePlot
    // endregion overrides

}
