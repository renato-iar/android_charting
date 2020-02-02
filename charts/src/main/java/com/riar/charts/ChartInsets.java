package com.riar.charts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class ChartInsets {

    public final float left;
    public final float right;
    public final float top;
    public final float bottom;

    public ChartInsets(float left, float right, float top, float bottom) {
        this.left   = left;
        this.right  = right;
        this.top    = top;
        this.bottom = bottom;
    }
    public ChartInsets() {
        this(0, 0, 0, 0);
    }

    @NonNull
    public static ChartInsets zero() {
        return new ChartInsets();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof ChartInsets) {
            final ChartInsets insets  = (ChartInsets) obj;
            return insets.left == this.left && insets.right == this.right && insets.top == this.top && insets.bottom == this.bottom;
        }

        return false;
    }

}
