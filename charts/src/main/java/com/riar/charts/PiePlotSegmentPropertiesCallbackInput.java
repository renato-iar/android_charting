package com.riar.charts;

public class PiePlotSegmentPropertiesCallbackInput {
    public final int index;
    public final int count;
    public final float total;
    public final float value;

    public PiePlotSegmentPropertiesCallbackInput(int index, int count, float value, float total) {
        this.index  = index;
        this.count  = count;
        this.value  = value;
        this.total  = total;
    }
}
