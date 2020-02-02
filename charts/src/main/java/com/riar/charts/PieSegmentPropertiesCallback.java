package com.riar.charts;

import androidx.annotation.Nullable;

public interface PieSegmentPropertiesCallback {

    @Nullable
    PiePlotSegmentProperties getSegmentProperties(@Nullable PiePlotSegmentPropertiesCallbackInput input);

}
