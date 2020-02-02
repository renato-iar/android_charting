package com.riar.charts;

public abstract class Axis {
    public final float origin;
    public final float length;
    public final float center;
    public final float end;

    protected Axis(float origin, float length) {
        this.origin = origin;
        this.length = length;
        this.center = origin + length * 0.5f;
        this.end    = origin + length;
    }
}
