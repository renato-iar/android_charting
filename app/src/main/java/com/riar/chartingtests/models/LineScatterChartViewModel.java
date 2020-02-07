package com.riar.chartingtests.models;

import androidx.annotation.NonNull;

import com.riar.chartingtests.data.LineScatterPlotEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LineScatterChartViewModel extends ChartViewModel<LineScatterPlotEntry> {

    @NonNull
    @Override
    protected List<LineScatterPlotEntry> hook_InstancePlotData() {
        final Random rnd                        = new Random();
        final int n                             = 5 + rnd.nextInt(25);
        final List<LineScatterPlotEntry> points = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            final float y   = rnd.nextFloat() * 20.0f;
            final float sz  = 10.0f + rnd.nextFloat() * 10.0f;
            final LineScatterPlotEntry entry    = new LineScatterPlotEntry((float) i, y, sz);
            points.add(entry);
        }

        return points;
    }
}
