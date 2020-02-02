package com.riar.charts;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Implements a bubble plot where all points may have different paints and sizes
 *
 * @see ScatterPlot
 * @see BubblePlotSegmentValue
 * */
public class BubblePlot<SegmentType extends BubblePlotSegmentValue> extends SegmentedPlot<SegmentType> {

    // region private
    // region class constants
    private static final String TAG = BubblePlot.class.getSimpleName();
    // endregion class constants
    // endregion private

    // region overrides
    // region SegmentedPlot
    @Nullable
    @Override
    protected Path hook_DrawPathForSegmentAtIndex(@NonNull SegmentType segment, int index, @NonNull DrawContext context) {

        final PointF pt                     = segment.getBubblePoint();

        if (pt == null) {
            return null;
        }

        final Path path                     = new Path();
        final float sz                      = segment.getBubbleSize();
        final float cx                      = context.getChart().transformToXViewCoordinates(pt.x);
        final float cy                      = context.getChart().transformToYViewCoordinates(pt.y);
        final RectF oval                    = new RectF(cx - sz, cy - sz, cx + sz, cy + sz);

        path.addOval(oval, Path.Direction.CW);

        return path;

    }
    // endregion SegmentedPlot
    // endregion overrides

}
