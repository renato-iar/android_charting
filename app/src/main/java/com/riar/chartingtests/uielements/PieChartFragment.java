package com.riar.chartingtests.uielements;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.riar.chartingtests.R;
import com.riar.chartingtests.data.PiePlotEntry;
import com.riar.chartingtests.models.PieChartViewModel;
import com.riar.charts.BasePlot;
import com.riar.charts.ChartView;
import com.riar.charts.Paint;
import com.riar.charts.PaintCallback;
import com.riar.charts.PiePlot;
import com.riar.charts.PiePlotSegmentProperties;
import com.riar.charts.PiePlotSegmentPropertiesCallbackInput;
import com.riar.charts.PieSegmentPropertiesCallback;
import com.riar.outlets.Outlet;

import java.util.ArrayList;
import java.util.List;

public class PieChartFragment extends SinglePlotChartFragment<PiePlotEntry, PieChartViewModel, PiePlot<PiePlotEntry>> {

    public static final String TAG                                          = PieChartFragment.class.getSimpleName();

    private Outlet<LinearLayout> labels_linearLayout                        = new Outlet<>(R.id.pie_chart_labels_linearLayout);
    private PaintCallback pie_chart_paint_callback                          = (int index, int count, @Nullable BasePlot.DrawContext context) -> {
        final int gray              = (int)(255.0f * index / count);
        final List<Paint> paints    = new ArrayList<>();
        final Paint fill            = new Paint.FillPaint(gray, gray, gray, 255);
        final Paint stroke          = new Paint.StrokePaint(255, 255, 255, 255, 5.0f);

        paints.add(fill);
        paints.add(stroke);

        return paints;
    };
    private PieSegmentPropertiesCallback pie_segment_properties_callback    = (@Nullable PiePlotSegmentPropertiesCallbackInput input) -> new PiePlotSegmentProperties(null, 0.750f);

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

    @Override
    protected void hook_SetupChart(@NonNull ChartView chart) {
        super.hook_SetupChart(chart);

        this.getPlot().setPaintCallback(this.pie_chart_paint_callback);
        this.getPlot().setPropertiesCallback(this.pie_segment_properties_callback);
    }

    @NonNull
    @Override
    protected PiePlot<PiePlotEntry> hook_InstancePlot() {
        return new PiePlot<>();
    }

    @Override
    protected Class<PieChartViewModel> hook_GetModelClass() {
        return PieChartViewModel.class;
    }

    @Override
    protected int hook_LoadingProgressBarId() {
        return R.id.pie_chart_progressBar;
    }

    @Override
    protected int hook_ReloadActionViewId() {
        return R.id.reload_pie_chart;
    }

    @Override
    protected int hook_ChartViewId() {
        return R.id.pie_chart;
    }

    @Override
    protected int hook_LayoutId() {
        return R.layout.pie_chart_layout;
    }

    @Override
    protected void hook_OnDataChanged(@NonNull List<PiePlotEntry> entries) {
        final Boolean reloading = this.getModel().getIsLoading().getValue();
        if (reloading != null && !reloading) {
            this.getPlot().setSegments(entries);
        }

        this.labels_linearLayout.execute(ll -> {
            ll.removeAllViewsInLayout();
            final Context ctx   = this.getContext();
            final int n         = entries.size();

            for (int i = 0; i < n; i++) {
                final PiePlotEntry e                            = entries.get(i);
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

}
