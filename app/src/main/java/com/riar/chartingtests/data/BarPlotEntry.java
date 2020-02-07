package com.riar.chartingtests.data;

import com.riar.charts.BarPlotSegmentValue;

public class BarPlotEntry implements BarPlotSegmentValue {

    public final float value;
    public final float leftInset;
    public final float rightInset;

    public BarPlotEntry(float value, float leftInset, float rightInset) {
        this.value      = value;
        this.leftInset  = leftInset;
        this.rightInset = rightInset;
    }

    @Override
    public float getBarPlotSegmentValue() {
        return this.value;
    }

    @Override
    public float getLeftInset() {
        return this.leftInset;
    }

    @Override
    public float getRightInset() {
        return this.rightInset;
    }
}
