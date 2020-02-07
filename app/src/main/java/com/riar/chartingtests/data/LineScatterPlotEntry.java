package com.riar.chartingtests.data;

import android.graphics.PointF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.riar.charts.LinePlotPointValue;
import com.riar.charts.ScatterPlotPointValue;

public class LineScatterPlotEntry implements LinePlotPointValue, ScatterPlotPointValue {

    public final float x;
    public final float y;
    public final float size;

    public LineScatterPlotEntry(float x, float y, float size) {
        this.x      = x;
        this.y      = y;
        this.size   = size;
    }

    @NonNull
    @Override
    public PointF getLinePoint() {
        return new PointF(this.x, this.y);
    }

    @Nullable
    @Override
    public PointF getScatterPoint() {
        return new PointF(this.x, this.y);
    }

    @Override
    public float getScatterPointSize() {
        return this.size;
    }
}
