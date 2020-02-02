package com.riar.charts;

import android.graphics.PointF;

import androidx.annotation.Nullable;

public interface ScatterPlotPointValue {
    @Nullable
    PointF getScatterPoint();

    float getScatterPointSize();
}
