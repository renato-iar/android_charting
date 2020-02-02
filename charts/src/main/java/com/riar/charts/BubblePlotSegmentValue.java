package com.riar.charts;

import android.graphics.PointF;

import androidx.annotation.Nullable;

public interface BubblePlotSegmentValue {

    @Nullable
    PointF getBubblePoint();

    float getBubbleSize();

}
