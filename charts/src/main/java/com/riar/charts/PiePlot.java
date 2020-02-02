package com.riar.charts;

import android.graphics.Path;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PiePlot<SegmentType extends PiePlotSegmentValue> extends SegmentedPlot<SegmentType> {

    private static final String TAG = PiePlot.class.getSimpleName();

    private float minimum_value = 0;
    private float maximum_value = 0;
    private float average_value = 0;
    private float total_value   = 0;

    @Nullable
    private PieSegmentPropertiesCallback properties_callback  = null;

    public final float getMinimumValue() {
        return this.minimum_value;
    }
    public final float getMaximumValue() {
        return this.maximum_value;
    }
    public final float getAverageValue() {
        return this.average_value;
    }
    public final float getTotalValue() {
        return this.total_value;
    }

    @Nullable
    protected PiePlotSegmentPropertiesCallbackInput hook_GetPropertiesInputForSegmentAtIndex(int index, int count) {
        return new PiePlotSegmentPropertiesCallbackInput(index, count, this.getSegment(index).getSliceValue(), this.getTotalValue());
    }

    @Nullable
    public final PieSegmentPropertiesCallback getPropertiesCallback() {
        return this.properties_callback;
    }
    public final void setPropertiesCallback(@Nullable PieSegmentPropertiesCallback callback) {
        this.properties_callback    = callback;
        this.invalidate();
    }

    @Override
    protected void hook_OnSegmentsDidChange(@NonNull List<SegmentType> segments) {
        super.hook_OnSegmentsDidChange(segments);

        final int n     = segments.size();
        float min       = 0;
        float max       = 0;
        float total     = 0;

        for (int i = 0; i < n; i++) {
            final float segment = segments.get(i).getSliceValue();
            if (i == 0) {
                min             = segment;
                max             = segment;
            }
            else {
                min             = Math.min(min, segment);
                max             = Math.max(max, segment);
            }

            total               += segment;
        }

        this.minimum_value      = min;
        this.maximum_value      = max;
        this.total_value        = total;
        this.average_value      = total / Math.max(1, (float) n);
    }

    @Nullable
    @Override
    protected Path hook_DrawPathForSegmentAtIndex(@NonNull SegmentType segment, int index, @NonNull DrawContext context) {
        final ChartView chart                             = context.getChart();
        final PieSegmentPropertiesCallback callback       = this.getPropertiesCallback();
        final float hole_radius_percent;
        final float outer_radius_percent;

        if (callback != null) {
            PiePlotSegmentPropertiesCallbackInput input   = this.hook_GetPropertiesInputForSegmentAtIndex(index, this.getSegmentsSize());
            PiePlotSegmentProperties properties           = callback.getSegmentProperties(input);
            if (properties != null) {
                hole_radius_percent                         = properties.holeRadiusPercent == null ? 0.0f : properties.holeRadiusPercent;
                outer_radius_percent                        = properties.radiusPercent == null ? 1.0f : properties.radiusPercent;
            }
            else {
                hole_radius_percent                         = 0.0f;
                outer_radius_percent                        = 1.0f;
            }
        }
        else {
            hole_radius_percent                             = 0.0f;
            outer_radius_percent                            = 1.0f;
        }

        final float cx                                      = chart.transformToXViewCoordinates(chart.getXAxis().center);
        final float cy                                      = chart.transformToYViewCoordinates(chart.getYAxis().center);
        final float left                                    = chart.transformToXViewCoordinates(chart.getXAxis().origin);
        final float right                                   = chart.transformToXViewCoordinates(chart.getXAxis().end);
        final float top                                     = chart.transformToYViewCoordinates(chart.getYAxis().origin);
        final float bottom                                  = chart.transformToYViewCoordinates(chart.getYAxis().end);
        final float rect_top                                = bottom;
        final float rect_bottom                             = top;
        final float width                                   = right - left;
        final float height                                  = rect_bottom - rect_top;
        final float radius                                  = Math.min(width, height) * 0.50f;
        final Path path                                     = new Path();

        float start_angle                                   = 0;
        final float k                                       = 360.0f / this.getTotalValue();

        for (int i = 0; i < index; i++) {
            start_angle         += this.getSegment(i).getSliceValue() * k;
        }

        // NOTE: chart coordinates are bottom-up, while view coordinates are top-down,
        //       hence the need to reverse the top/bottom coordinates, otherwise the arc's
        //       bounding rectangle becomes squashed (i.e. top={actual view bottom})
        //path.addArc(left, rect_top, right, rect_bottom, start_angle, segment.getSliceValue() * k);
        final float outer_radius                            = radius * outer_radius_percent;
        final float value                                   = segment.getSliceValue();

        path.addArc(cx - outer_radius, cy - outer_radius, cx + outer_radius, cy + outer_radius, start_angle, value * k);
        if (hole_radius_percent > 0.0f) {
            final float hole_radius                         = radius * hole_radius_percent;
            path.arcTo(cx - hole_radius, cy - hole_radius, cx + hole_radius, cy + hole_radius, start_angle + value * k, -value * k, false);
        }
        else {
            path.lineTo(cx, cy);
        }
        path.close();

        return path;
    }

}
