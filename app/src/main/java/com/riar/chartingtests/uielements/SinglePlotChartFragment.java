package com.riar.chartingtests.uielements;

import androidx.annotation.NonNull;

import com.riar.chartingtests.models.ChartViewModel;
import com.riar.charts.BasePlot;

import java.util.ArrayList;
import java.util.List;

public abstract class SinglePlotChartFragment<PlotDataType, ModelType extends ChartViewModel<PlotDataType>, PlotType extends BasePlot> extends ChartFragment<PlotDataType, ModelType, PlotType> {

    @NonNull
    private PlotType plot;

    @NonNull
    protected abstract PlotType hook_InstancePlot();

    @NonNull
    public final PlotType getPlot() {
        return this.plot;
    }

    public SinglePlotChartFragment() {
        super();
        this.plot   = this.hook_InstancePlot();
    }

    @NonNull
    @Override
    protected final List<PlotType> hook_InstancePlots() {
        final List<PlotType> plots  = new ArrayList<>();
        plots.add(this.plot);

        return plots;
    }

}
