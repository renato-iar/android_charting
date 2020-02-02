package com.riar.charts;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Implements a scatter plot, where all points share the same paint properties, but may indeed have different sizes
 *
 * @see BubblePlot
 * @see ScatterPlotPointValue
 * */
public class ScatterPlot<PointType extends ScatterPlotPointValue> extends NonSegmentedPlot<PointType> {

    @Override
    protected void hook_Draw(@NonNull Path path, @NonNull List<PointType> points, @NonNull DrawContext context) {
        final int n = points.size();
        for (int i = 0; i < n; i++) {
            final PointType point   = points.get(i);
            final PointF pt         = point.getScatterPoint();

            if (pt == null) {
                return;
            }

            final float cx          = context.getChart().transformToXViewCoordinates(pt.x);
            final float cy          = context.getChart().transformToYViewCoordinates(pt.y);
            final float sz          = point.getScatterPointSize();
            final RectF r           = new RectF(cx - sz, cy - sz, cx + sz, cy + sz);

            path.addOval(r, Path.Direction.CW);
        }
    }
}
