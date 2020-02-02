package com.riar.charts;

import android.graphics.Path;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SegmentedBarPlot<SegmentType extends BarPlotSegmentValue> extends SegmentedPlot<SegmentType> {

    private float min   = 0.0f;
    private float max   = 0.0f;
    private float avg   = 0.0f;
    private float total = 0.0f;

    public final float getMin() {
        return this.min;
    }
    public final float getMax() {
        return this.max;
    }
    public final float getAverage() {
        return this.avg;
    }
    public final float getTotal() {
        return this.total;
    }

    @Override
    protected void hook_OnSegmentsDidChange(@NonNull List<SegmentType> segments) {
        super.hook_OnSegmentsDidChange(segments);

        float min       = 0.0f,
                max     = 0.0f,
                total   = 0.0f;
        final int n     = segments.size();

        for (int i = 0; i < n; i++) {
            float value = segments.get(i).getBarPlotSegmentValue();
            if (i == 0) {
                min = value;
                max = value;
            }
            else {
                min = Math.min(value, min);
                max = Math.max(value, max);
            }

            total   += value;
        }

        this.min    = min;
        this.max    = max;
        this.total  = total;
        this.avg    = total / Math.max(1, n);
    }

    @Nullable
    @Override
    protected Path hook_DrawPathForSegmentAtIndex(@NonNull SegmentType segment, int index, @NonNull DrawContext context) {
        final ChartView chart = context.getChart();
        final Path path         = new Path();
        final float w           = chart.transformToXViewCoordinates(chart.getXAxis().length / Math.max(1, this.getSegmentsSize()));
        final float x           = w * index;
        final float y           = chart.transformToYViewCoordinates(segment.getBarPlotSegmentValue());
        final float y0          = chart.transformToYViewCoordinates(chart.getYAxis().origin);
        final RectF r           = new RectF(x + segment.getLeftInset(), y, x + w - segment.getRightInset(), y0);
        if (y > y0) {
            r.top       = y0;
            r.bottom    = y;
        }

        path.addRect(r, Path.Direction.CW);

        return path;
    }

}
