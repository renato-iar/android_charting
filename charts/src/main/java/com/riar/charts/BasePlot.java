package com.riar.charts;

import android.graphics.Canvas;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BasePlot {

    // region private
    // region class constants
    private static int random_key   = 0;
    // endregion class constants

    // region class methods
    private static int next_random_key() {
        return ++BasePlot.random_key;
    }
    // endregion class methods

    // region instance variables
    @Nullable
    private ChartView chart;
    // endregion instance variables
    // endregion private

    // region package private
    // region instance methods
    void setChart(@Nullable ChartView chartView) {
        if (chartView != this.chart) {
            final ChartView old   = this.chart;

            if (old != null) {
                this.hook_WillRemoveChartView(old);
            }

            if (chartView != null) {
                this.hook_WillMoveToChartView(chartView);
            }

            this.chart              = chartView;

            if (old != null) {
                this.hook_DidRemoveFromChartView(old);
            }

            if (chartView != null) {
                this.hook_DidMoveToChartView(chartView);
            }
        }
    }

    void onDraw(@NonNull Canvas canvas, @NonNull BasePlot.DrawContext context) {

    }
    // endregion instance methods
    // endregion package private

    // region protected
    // region hooks
    @CallSuper
    protected void hook_WillMoveToChartView(@NonNull ChartView chartView) {
        // This method is a stub
    }
    @CallSuper
    protected void hook_DidMoveToChartView(@NonNull ChartView chartView) {
        // This method is a stub
    }
    @CallSuper
    protected void hook_WillRemoveChartView(@NonNull ChartView chartView) {
        // This method is a stub
    }
    @CallSuper
    protected void hook_DidRemoveFromChartView(@NonNull ChartView chartView) {
        // This method is a stub
    }

    @CallSuper
    protected void hook_Invalidate() {
        // This method is (currently) a stub
    }
    // endregion hooks
    // endregion protected

    // region public
    // region types
    public interface DrawContext {
        @NonNull
        ChartView getChart();
    }
    // endregion types

    // region instance variables
    public int id;
    // endregion instance variables

    // region instance methods
    @Nullable
    public final ChartView getChart() {
        return this.chart;
    }

    @NonNull
    public BasePlot invalidate() {
        this.hook_Invalidate();
        if (this.chart != null) {
            this.chart.invalidate();
        }
        return this;
    }
    // endregion instance methods

    // region constructors
    public BasePlot(int id) {
        this.id = id;
    }
    public BasePlot() {
        this(BasePlot.next_random_key());
    }
    // endregion constructors
    // endregion public

}
