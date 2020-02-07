package com.riar.chartingtests.models;

import androidx.annotation.NonNull;

import com.riar.chartingtests.data.BarPlotEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BarChartViewModel extends ChartViewModel<BarPlotEntry> {

    @NonNull
    @Override
    protected List<BarPlotEntry> hook_InstancePlotData() {
        final Random rnd                = new Random();
        final int length                = 5 + rnd.nextInt(20);
        final List<BarPlotEntry> points = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            points.add(new BarPlotEntry(1.0f + rnd.nextFloat() * 20.0f, 5.0f, 5.0f));
        }

        return points;
    }

}
