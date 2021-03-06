package com.riar.charts;

import androidx.annotation.Nullable;

import java.util.List;

public interface PaintCallback {

    @Nullable
    List<Paint> paintForItemAtIndex(int index, int count, @Nullable BasePlot.DrawContext context);

}
