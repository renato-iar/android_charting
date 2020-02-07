package com.riar.chartingtests.data;

import com.riar.charts.PiePlotSegmentValue;

public class PiePlotEntry implements PiePlotSegmentValue {

    public final float value;

    public PiePlotEntry(float value) {
        this.value  = value;
    }

    @Override
    public float getSliceValue() {
        return this.value;
    }
}
