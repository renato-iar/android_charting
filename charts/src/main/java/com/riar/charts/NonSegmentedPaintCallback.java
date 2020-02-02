package com.riar.charts;

import androidx.annotation.Nullable;

import java.util.List;

public interface NonSegmentedPaintCallback {
    @Nullable
    List<Paint> getPaint(int count, @Nullable BasePlot.DrawContext context);
}
