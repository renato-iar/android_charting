package com.riar.charts;

import android.graphics.Path;
import android.graphics.PointF;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements line plots.
 *
 * @apiNote {@link LinePlot}s may be closed in order to become area plots (see {@link #setIsClosed(boolean)})
 * @apiNote {@link LinePlot}s may be smooth (see {@link #setIsSmooth(boolean)})
 *
 * @see LinePlotPointValue
 * */
public class LinePlot<PointType extends LinePlotPointValue> extends NonSegmentedPlot<PointType> {

    // region private
    // region instance constants
    private final List<PointF> interpolated_points1 = new ArrayList<>();
    private final List<PointF> interpolated_points2 = new ArrayList<>();
    // endregion instance constants

    // region instance variables
    private boolean is_smooth   = false;
    private boolean is_closed   = false;
    // endregion instance variables
    // endregion private

    // region public
    // region instance methods
    public final boolean isSmooth() {
        return this.is_smooth;
    }
    public final void setIsSmooth(boolean smooth) {
        this.is_smooth  = smooth;
        this.invalidate();
    }

    public final boolean isClosed() {
        return this.is_closed;
    }
    public final void setIsClosed(boolean closed) {
        this.is_closed  = closed;
        this.invalidate();
    }

    public final boolean isOpen() {
        return !this.isClosed();
    }
    public final void setIsOpen(boolean open) {
        this.setIsClosed(!open);
    }
    // endregion instance methods

    // region constructors
    public LinePlot() {
        this(false, false);
    }
    public LinePlot(boolean smooth, boolean closed) {
        super();

        this.is_smooth  = smooth;
        this.is_closed  = closed;
    }
    // endregion constructors
    // endregion public

    // region overrides
    // region NonSegmentedPlot
    @Override
    protected void hook_OnPointsDidChange(@NonNull List<PointType> points) {
        super.hook_OnPointsDidChange(points);

        this.interpolated_points1.clear();
        this.interpolated_points2.clear();

        if (points.isEmpty()) {
            return;
        }

        PointF prev = points.get(0).getLinePoint();

        for (int i = 1; i < points.size(); i++) {
            final PointF act    = points.get(i).getLinePoint();
            this.interpolated_points1.add(new PointF((act.x + prev.x) * 0.50f, prev.y));
            this.interpolated_points2.add(new PointF((act.x + prev.x) * 0.50f, act.y));
            prev                = act;
        }
    }

    @Override
    protected void hook_Draw(@NonNull Path path, @NonNull List<PointType> points, @NonNull DrawContext context) {
        if (points.isEmpty()) {
            return;
        }

        final int n             = points.size();
        final ChartView chart = context.getChart();
        final float ox          = chart.transformToXViewCoordinates(chart.getXAxis().origin);
        final float oy          = chart.transformToYViewCoordinates(chart.getYAxis().origin);
        final PointF pt0        = points.get(0).getLinePoint();
        final float x0          = chart.transformToXViewCoordinates(pt0.x);
        final float y0          = chart.transformToYViewCoordinates(pt0.y);

        if (this.isClosed()) {
            path.moveTo(ox, oy);
            path.lineTo(x0, y0);
        }
        else {
            path.moveTo(x0, y0);
        }

        if (this.isSmooth()) {
            for (int i = 1; i < n; i++) {
                final PointF pi     = points.get(i).getLinePoint();
                final PointF pprev  = this.interpolated_points1.get(i-1);
                final PointF pnext  = this.interpolated_points2.get(i-1);

                final float pprevx  = chart.transformToXViewCoordinates(pprev.x);
                final float pprevy  = chart.transformToYViewCoordinates(pprev.y);
                final float pnextx  = chart.transformToXViewCoordinates(pnext.x);
                final float pnexty  = chart.transformToYViewCoordinates(pnext.y);
                final float pix     = chart.transformToXViewCoordinates(pi.x);
                final float piy     = chart.transformToYViewCoordinates(pi.y);

                path.cubicTo(pprevx, pprevy, pnextx, pnexty, pix, piy);
            }
        }
        else {
            for (int i = 1; i < n; i++) {
                final PointF pt     = points.get(i).getLinePoint();
                final float x       = chart.transformToXViewCoordinates(pt.x);
                final float y       = chart.transformToYViewCoordinates(pt.y);

                path.lineTo(x, y);
            }
        }

        if (this.isClosed()) {
            final PointF ptn    = points.get(n - 1).getLinePoint();
            final float xn      = chart.transformToXViewCoordinates(ptn.x);
            path.lineTo(xn, oy);
            path.close();
        }
    }
    // endregion NonSegmentedPlot
    // endregion
}

