package com.riar.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChartView extends View {

    // region private
    // region class constants
    private static final String TAG = ChartView.class.getSimpleName();
    // endregion class constants

    // region instance constants
    private final List<BasePlot> plots    = new ArrayList<>();
    // endregion instance constants

    // region instance variables
    private float x_axis_scale              = 1.0f;
    private float y_axis_scale              = 1.0f;

    private ECXAxis x_axis                  = new ECXAxis(0.0f, 1.0f);
    private ECYAxis y_axis                  = new ECYAxis(0.0f, 1.0f);
    private ChartInsets insets            = ChartInsets.zero();
    // endregion instance variables

    // region instance methods
    private void re_set_metrics() {
        final Rect r    = new Rect();
        this.getDrawingRect(r);

        final float width   = r.width();
        final float height  = r.height();

        this.x_axis_scale   = (width - this.insets.left - this.insets.right) / this.x_axis.length;
        this.y_axis_scale   = (height - this.insets.top - this.insets.bottom) / this.y_axis.length;
    }
    private void re_set_draw() {
        this.invalidate();
    }
    private void re_set_graphic_state() {
        for (BasePlot plot: this.plots) {
            plot.invalidate();
        }

        this.re_set_metrics();
        this.re_set_draw();
    }

    private void add_plot(@NonNull BasePlot plot, boolean refresh) {
        if (!this.plots.contains(plot)) {
            this.plots.add(plot);
            plot.setChart(this);

            if (refresh) {
                this.re_set_draw();
            }
        }
    }
    private void add_plots(@Nullable Collection<BasePlot> plots, boolean refresh) {
        if (plots != null && !plots.isEmpty()) {
            for (BasePlot plot: plots) {
                this.add_plot(plot, false);
            }

            if (refresh) {
                this.re_set_draw();
            }
        }
    }
    // endregion instance methods
    // endregion private

    // region protected
    // region types
    protected class ChartDrawContext implements BasePlot.DrawContext {
        @NonNull
        @Override
        public ChartView getChart() {
            return ChartView.this;
        }

        protected ChartDrawContext() {
        }
    }
    // endregion types

    // region hooks
    @NonNull
    protected BasePlot.DrawContext hook_GetDrawContext() {
        return new ChartDrawContext();
    }
    // endregion hooks
    // endregion protected

    // region public
    // region instance methods
    public final float transformToXViewCoordinates(float x) {
        return (x - this.x_axis.origin) * this.x_axis_scale + this.insets.left;
    }
    public final float transformToYViewCoordinates(float y) {
        final float h   = this.getHeight();
        final float ty  = (y - this.y_axis.origin) * this.y_axis_scale + this.insets.top;
        //final float out = h - ty;

        return h - ty;
    }

    public final int getPlotCount() {
        return this.plots.size();
    }
    public final BasePlot getPlot(int index) {
        return this.plots.get(index);
    }

    @NonNull
    public final ECXAxis getXAxis() {
        return this.x_axis;
    }
    @NonNull
    public final ECYAxis getYAxis() {
        return this.y_axis;
    }
    @NonNull
    public final ChartInsets getInsets() {
        return this.insets;
    }

    @NonNull
    public final ChartView setXAxis(@NonNull ECXAxis axis) {
        this.x_axis = axis;
        this.re_set_graphic_state();

        return this;
    }
    @NonNull
    public final ChartView setXAxis(float origin, float length) {
        return this.setXAxis(new ECXAxis(origin, length));
    }

    @NonNull
    public final ChartView setYAxis(@NonNull ECYAxis axis) {
        this.y_axis = axis;
        this.re_set_graphic_state();

        return this;
    }
    @NonNull
    public final ChartView setYAxis(float origin, float length) {
        return this.setYAxis(new ECYAxis(origin, length));
    }

    @NonNull
    public final ChartView setInsets(@NonNull ChartInsets insets) {
        this.insets = insets;
        this.re_set_graphic_state();

        return this;
    }
    @NonNull
    public final ChartView setInsets(float left, float right, float top, float bottom) {
        return this.setInsets(new ChartInsets(left, right, top, bottom));
    }

    public final void addPlot(@NonNull BasePlot plot) {
        this.add_plot(plot, true);
    }
    public final void addPlots(@Nullable Collection<BasePlot> plots) {
        this.add_plots(plots, true);
    }
    public final void removePlot(@NonNull BasePlot plot) {
        if (this.plots.remove(plot)) {
            plot.setChart(null);
            this.re_set_draw();
        }
    }
    public final void removePlots(@Nullable Collection<BasePlot> plots) {
        if (plots != null) {
            int removed = 0;
            for (BasePlot plot : plots) {
                if (this.plots.remove(plot)) {
                    removed++;
                    plot.setChart(null);
                }
            }

            if (removed > 0) {
                this.re_set_draw();
            }
        }
    }
    public final void clearPlots() {
        this.removePlots(new ArrayList<>(this.plots));
    }
    // endregion instance methods

    // region constructors
    public ChartView(Context context) {
        super(context);
    }
    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    // endregion constructors
    // endregion public

    // region overrides
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final BasePlot.DrawContext drawContext    = this.hook_GetDrawContext();
        for (BasePlot plot: this.plots) {
            plot.onDraw(canvas, drawContext);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.re_set_graphic_state();
    }
    // endregion overrides

}
