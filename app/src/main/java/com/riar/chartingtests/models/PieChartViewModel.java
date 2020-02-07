package com.riar.chartingtests.models;

import androidx.annotation.NonNull;

import com.riar.chartingtests.data.PiePlotEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PieChartViewModel extends ChartViewModel<PiePlotEntry> {

    @NonNull
    @Override
    protected List<PiePlotEntry> hook_InstancePlotData() {
        final Random rnd                = new Random();
        final List<PiePlotEntry> points = new ArrayList<>();
        final int n                     = 2 + rnd.nextInt(18);

        for (int i = 0; i < n; i++) {
            points.add(new PiePlotEntry(rnd.nextFloat() * 20.0f));
        }

        return points;
    }

}
