package com.riar.chartingtests.uielements;

import android.graphics.PointF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.riar.chartingtests.R;
import com.riar.chartingtests.data.LineScatterPlotEntry;
import com.riar.chartingtests.models.LineScatterChartViewModel;
import com.riar.charts.BasePlot;
import com.riar.charts.ChartView;
import com.riar.charts.LinePlot;
import com.riar.charts.NonSegmentedPaintCallback;
import com.riar.charts.NonSegmentedPlot;
import com.riar.charts.Paint;
import com.riar.charts.ScatterPlot;

import java.util.ArrayList;
import java.util.List;

public class LineScatterChartFragment extends ChartFragment<LineScatterPlotEntry, LineScatterChartViewModel, NonSegmentedPlot<LineScatterPlotEntry>> {

    public static final String TAG                                          = LineScatterChartFragment.class.getSimpleName();

    private NonSegmentedPaintCallback line_plot_paint_callback              = (int count, @Nullable BasePlot.DrawContext context) -> {
        final int gray              = 128;//(int)(255.0f * context. / count);
        final List<Paint> paints    = new ArrayList<>();
        final Paint fill            = new Paint.StrokePaint(gray, gray, gray, 255, 4.0f);

        paints.add(fill);

        return paints;
    };
    private NonSegmentedPaintCallback scatter_plot_paint_callback           = (int count, @Nullable BasePlot.DrawContext context) -> {
        final int gray              = 128;//(int)(255.0f * context. / count);
        final List<Paint> paints    = new ArrayList<>();
        final Paint fill            = new Paint.FillPaint(255, 255, 255, 255);
        final Paint stroke          = new Paint.StrokePaint(gray, gray, gray, 255, 4.0f);

        paints.add(fill);
        paints.add(stroke);

        return paints;
    };

    private final LinePlot<LineScatterPlotEntry> linePlot;
    private final ScatterPlot<LineScatterPlotEntry> scatterPlot;

    public LineScatterChartFragment() {
        super();

        this.linePlot       = new LinePlot<>();
        this.scatterPlot    = new ScatterPlot<>();

        this.linePlot.setPaintCallback(this.line_plot_paint_callback);
        this.scatterPlot.setPaintCallback(this.scatter_plot_paint_callback);
    }

    @Override
    protected void hook_SetupChart(@NonNull ChartView chart) {
        super.hook_SetupChart(chart);

        chart.setInsets(20.0f);
    }

    @NonNull
    @Override
    protected List<NonSegmentedPlot<LineScatterPlotEntry>> hook_InstancePlots() {
        final List<NonSegmentedPlot<LineScatterPlotEntry>> plots    = new ArrayList<>();
        plots.add(this.linePlot);
        plots.add(this.scatterPlot);

        return plots;
    }

    @Override
    protected Class<LineScatterChartViewModel> hook_GetModelClass() {
        return LineScatterChartViewModel.class;
    }

    @Override
    protected int hook_LoadingProgressBarId() {
        return R.id.line_scatter_chart_progressBar;
    }

    @Override
    protected int hook_ReloadActionViewId() {
        return R.id.reload_line_scatter_chart;
    }

    @Override
    protected int hook_ChartViewId() {
        return R.id.line_scatter_chart;
    }

    @Override
    protected int hook_LayoutId() {
        return R.layout.line_scatter_chart_layout;
    }

    @Override
    protected void hook_OnDataChanged(@NonNull List<LineScatterPlotEntry> entries) {
        float max           = 0.0f;
        for (LineScatterPlotEntry e: entries) {
            final PointF pt = e.getScatterPoint();
            if (pt != null) {
                max             = Math.max(max, pt.y);
            }
        }
        final float mmax        = max;

        this.chartView.execute(chart -> {
            chart.setXAxis(0.0f, Math.max(1, entries.size() - 1));
            chart.setYAxis(0.0f, mmax);
        });

        this.linePlot.setPoints(entries);
        this.scatterPlot.setPoints(entries);
    }

}
