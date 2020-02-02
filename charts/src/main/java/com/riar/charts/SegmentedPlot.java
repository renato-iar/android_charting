package com.riar.charts;

import android.graphics.Canvas;
import android.graphics.Path;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class SegmentedPlot<SegmentType> extends BasePlot {

    // region private
    // region instance constants
    private final List<SegmentType> segments    = new ArrayList<>();
    private PaintCallback paint_callback      = null;
    // endregion instance constants

    // region instance methods
    private Set<DidChangeCallback<SegmentType>> did_change_callback = new HashSet<>();
    // endregion instance methods
    // endregion private

    // region protected
    // region hooks
    @Nullable
    protected abstract Path hook_DrawPathForSegmentAtIndex(@NonNull SegmentType segment, int index, @NonNull BasePlot.DrawContext context);

    @CallSuper
    protected void hook_OnSegmentsDidChange(@NonNull List<SegmentType> segments) {
        for (DidChangeCallback<SegmentType> callback: this.did_change_callback) {
            callback.onSegmentsDidChange(segments);
        }
    }
    // endregion hooks
    // endregion protected

    // region public
    // region types
    public interface DidChangeCallback<SegmentType> {
        void onSegmentsDidChange(@NonNull List<SegmentType> segments);
    }
    // endregion types

    // region instance methods
    public final void registerDidChangeCallback(@NonNull DidChangeCallback<SegmentType> callback) {
        this.did_change_callback.add(callback);
    }
    public final boolean unRegisterDidChangeCallback(@NonNull DidChangeCallback<SegmentType> callback) {
        return this.did_change_callback.remove(callback);
    }

    @Nullable
    public final PaintCallback getPaintCallback() {
        return this.paint_callback;
    }
    public final void setPaintCallback(@Nullable PaintCallback callback) {
        this.paint_callback = callback;
        this.invalidate();
    }

    public final int getSegmentsSize() {
        return this.segments.size();
    }
    @NonNull
    public final SegmentType getSegment(int index) {
        return this.segments.get(index);
    }

    public final void setSegments(@Nullable Collection<SegmentType> segments) {
        this.segments.clear();
        if (segments != null) {
            this.segments.addAll(segments);
        }

        this.hook_OnSegmentsDidChange(this.segments);
        this.invalidate();
    }
    // endregion instance methods
    // endregion public

    // region package private
    // region BasePlot
    @Override
    void onDraw(@NonNull Canvas canvas, @NonNull DrawContext context) {
        super.onDraw(canvas, context);

        final PaintCallback callback  = this.getPaintCallback();
        if (callback == null) {
            return;
        }

        final int n = this.segments.size();
        for (int i = 0; i < n; i++) {
            final SegmentType segment       = this.segments.get(i);
            final Path path                 = this.hook_DrawPathForSegmentAtIndex(segment, i, context);

            if (path != null) {
                final List<Paint> paints  = callback.paintForItemAtIndex(i, n, context);
                if (paints != null) {
                    for (Paint paint: paints) {
                    paint.onPaint(canvas, path, context);
                    }
                }
            }
        }
    }
    // endregion BasePlot
    // endregion package private
}
