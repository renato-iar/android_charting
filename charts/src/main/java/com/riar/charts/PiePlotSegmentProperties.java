package com.riar.charts;

import androidx.annotation.Nullable;

public class PiePlotSegmentProperties {
    @Nullable
    public final Float radiusPercent;
    @Nullable
    public final Float holeRadiusPercent;

    public PiePlotSegmentProperties(@Nullable Float radiusPercent, @Nullable Float holeRadiusPercent) {
        this.radiusPercent      = radiusPercent;
        this.holeRadiusPercent  = holeRadiusPercent;
    }
}
