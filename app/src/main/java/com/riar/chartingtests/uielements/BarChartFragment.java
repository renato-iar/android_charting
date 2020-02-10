package com.riar.chartingtests.uielements;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.riar.chartingtests.R;
import com.riar.chartingtests.data.BarPlotEntry;
import com.riar.chartingtests.models.BarChartViewModel;
import com.riar.charts.BasePlot;
import com.riar.charts.ChartView;
import com.riar.charts.Paint;
import com.riar.charts.PaintCallback;
import com.riar.charts.SegmentedBarPlot;
import com.riar.outlets.Outlet;

import java.util.ArrayList;
import java.util.List;

public class BarChartFragment extends SinglePlotChartFragment<BarPlotEntry, BarChartViewModel, SegmentedBarPlot<BarPlotEntry>> {

    public static final String TAG  = BarChartFragment.class.getSimpleName();

    private Outlet<LinearLayout> labels_linearLayout        = new Outlet<>(R.id.bar_chart_labels_linearLayout);
    private PaintCallback bar_chart_paint_callback          = (int index, int count, @Nullable BasePlot.DrawContext context) -> {
        final int gray              = (int)(255.0f * index / count);
        final int inv               = 255 - gray;
        final List<Paint> paints    = new ArrayList<>();
        final Paint fill            = new Paint.FillPaint(gray, gray, gray, 255);
        final Paint stroke          = new Paint.StrokePaint(inv, inv, inv, 255, 4.0f);

        paints.add(fill);
        paints.add(stroke);
        return paints;
    };

    @Override
    protected void hook_SetupChart(@NonNull ChartView chart) {
        super.hook_SetupChart(chart);

        this.getPlot().setPaintCallback(this.bar_chart_paint_callback);
    }

    @NonNull
    @Override
    protected SegmentedBarPlot<BarPlotEntry> hook_InstancePlot() {
        return new SegmentedBarPlot<>();
    }

    @Override
    protected Class<BarChartViewModel> hook_GetModelClass() {
        return BarChartViewModel.class;
    }

    @Override
    protected int hook_LoadingProgressBarId() {
        return R.id.bar_chart_progressBar;
    }

    @Override
    protected int hook_ReloadActionViewId() {
        return R.id.reload_bar_chart;
    }

    @Override
    protected int hook_ChartViewId() {
        return R.id.bar_chart;
    }

    @Override
    protected int hook_LayoutId() {
        return R.layout.bar_chart_layout;
    }

    @Override
    protected void hook_OnDataChanged(@NonNull List<BarPlotEntry> entries) {
        float max           = 0.0f;
        for (BarPlotEntry e: entries) {
            max             = Math.max(max, e.value);
        }

        final float mmax    = max;
        this.chartView.execute(chart -> {
            chart.setXAxis(0.0f, entries.size());
            chart.setYAxis(0.0f, mmax);
        });
        this.getPlot().setSegments(entries);

        this.labels_linearLayout.execute(ll -> {
            ll.removeAllViewsInLayout();
            final Context ctx   = this.getContext();
            final int n         = entries.size();

            for (int i = 0; i < n; i++) {
                final BarPlotEntry e                            = entries.get(i);
                final PlotLabelView label                       = new PlotLabelView(ctx);
                final int gray                                  = (int)(255.0f * i / n);
                final int color                                 = Color.argb(255, gray, gray, gray);
                final LinearLayout.LayoutParams layoutParams    = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin                          = 10;
                label.setLayoutParams(layoutParams);

                ll.addView(label);
                label.setText("$ " + e.value);
                label.setColorViewColor(color);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.labels_linearLayout.load(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.labels_linearLayout.unload();
    }

}
