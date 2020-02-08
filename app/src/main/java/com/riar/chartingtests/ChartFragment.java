package com.riar.chartingtests;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.riar.chartingtests.data.BarPlotEntry;
import com.riar.chartingtests.data.LineScatterPlotEntry;
import com.riar.chartingtests.data.PiePlotEntry;
import com.riar.chartingtests.models.BarChartViewModel;
import com.riar.chartingtests.models.LineScatterChartViewModel;
import com.riar.chartingtests.models.PieChartViewModel;
import com.riar.charts.BasePlot;
import com.riar.charts.ChartView;
import com.riar.charts.LinePlot;
import com.riar.charts.NonSegmentedPaintCallback;
import com.riar.charts.Paint;
import com.riar.charts.PaintCallback;
import com.riar.charts.PiePlot;
import com.riar.charts.ScatterPlot;
import com.riar.charts.SegmentedBarPlot;
import com.riar.outlets.Outlet;

import java.util.ArrayList;
import java.util.List;

public class ChartFragment extends Fragment implements LifecycleObserver {

    public static final String TAG  = ChartFragment.class.getSimpleName();

    private Observer<List<BarPlotEntry>> bar_data_observer  = (entries) -> ChartFragment.this.on_bar_chart_data_reload(entries);
    private Observer<Boolean> is_loading_bar_data_observer  = (Boolean loading) -> ChartFragment.this.on_bar_chart_data_is_reloading_changed(loading);
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

    private Observer<List<PiePlotEntry>> pie_data_observer  = (entries) -> ChartFragment.this.on_pie_chart_data_changed(entries);
    private Observer<Boolean> is_loading_pie_data_observer  = (Boolean loading) -> ChartFragment.this.on_pie_chart_data_is_reloading_changed(loading);
    private PaintCallback pie_chart_paint_callback          = (int index, int count, @Nullable BasePlot.DrawContext context) -> {
        final int gray              = (int)(255.0f * index / count);
        final List<Paint> paints    = new ArrayList<>();
        final Paint fill            = new Paint.FillPaint(gray, gray, gray, 255);
        final Paint stroke          = new Paint.StrokePaint(255, 255, 255, 255, 5.0f);

        paints.add(fill);
        paints.add(stroke);

        return paints;
    };

    private Observer<List<LineScatterPlotEntry>> line_scatter_data_observer = (entries) -> ChartFragment.this.on_line_scatter_chart_data_changed(entries);
    private Observer<Boolean> is_loading_line_scatter_data_observer         = (Boolean loading) -> ChartFragment.this.on_line_scatter_chart_data_is_reloading_changed(loading);
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

    private final Outlet<ChartView> bar_chart_view                      = new Outlet<>(R.id.bar_chart);
    private final Outlet<ProgressBar> bar_chart_progressBar             = new Outlet<>(R.id.bar_chart_progressBar);
    private final Outlet<ImageView> bar_chart_reload_imageView          = new Outlet<>(R.id.reload_bar_chart);

    private final Outlet<ChartView> pie_chart_view                      = new Outlet<>(R.id.pie_chart);
    private final Outlet<ProgressBar> pie_chart_progressBar             = new Outlet<>(R.id.pie_chart_progressBar);
    private final Outlet<ImageView> pie_chart_reload_imageView          = new Outlet<>(R.id.reload_pie_chart);

    private final Outlet<ChartView> line_scatter_chart_view             = new Outlet<>(R.id.line_scatter_chart);
    private final Outlet<ProgressBar> line_scatter_chart_progressBar    = new Outlet<>(R.id.line_scatter_chart_progressBar);
    private final Outlet<ImageView> line_scatter_reload_imageView       = new Outlet<>(R.id.reload_line_scatter_chart);

    private final SegmentedBarPlot<BarPlotEntry> barPlot                = new SegmentedBarPlot<>();
    private final PiePlot<PiePlotEntry> piePlot                         = new PiePlot<>();
    private final LinePlot<LineScatterPlotEntry> linePlot               = new LinePlot<>();
    private final ScatterPlot<LineScatterPlotEntry> scatterPlot         = new ScatterPlot<>();

    private BarChartViewModel bar_chart_viewModel;
    private PieChartViewModel pie_chart_viewModel;
    private LineScatterChartViewModel line_scatter_chart_viewModel;

    private void on_reloading_changed(@NonNull Outlet<ImageView> outlet, Boolean reloading) {
        if (reloading != null && reloading) {
            outlet.execute(imgv -> {
                imgv.setAnimation(
                        AnimationUtils.loadAnimation(ChartFragment.this.getContext(), R.anim.indefinite_right_rotation)
                );
                imgv.setAlpha(0.250f);
            });
        }
        else {
            outlet.execute(imgv -> {
                imgv.clearAnimation();
                imgv.setAlpha(1.0f);
            });
        }
    }

    private void on_bar_chart_data_reload(@NonNull List<BarPlotEntry> entries) {
        float max           = 0.0f;
        for (BarPlotEntry e: entries) {
            max             = Math.max(max, e.value);
        }

        final float mmax    = max;
        this.bar_chart_view.execute(chart -> {
            chart.setXAxis(0.0f, entries.size());
            chart.setYAxis(0.0f, mmax);
        });
        this.barPlot.setSegments(entries);
    }
    private void on_reload_bar_chart_data(View sender) {
        final Boolean reloading = this.bar_chart_viewModel.getIsLoading().getValue();
        if (reloading != null && !reloading) {
            this.bar_chart_viewModel.update(true);
        }
    }
    private void on_bar_chart_data_is_reloading_changed(Boolean reloading) {
        this.bar_chart_progressBar.setVisibility(reloading ? View.VISIBLE : View.GONE);
        this.on_reloading_changed(this.bar_chart_reload_imageView, reloading);
    }

    private void on_pie_chart_data_changed(@NonNull List<PiePlotEntry> entries) {
        final Boolean reloading = this.pie_chart_viewModel.getIsLoading().getValue();
        if (reloading != null && !reloading) {
            this.piePlot.setSegments(entries);
        }
    }
    private void on_reload_pie_chart_data(View sender) {
        this.pie_chart_viewModel.update(true);
    }
    private void on_pie_chart_data_is_reloading_changed(Boolean reloading) {
        this.pie_chart_progressBar.setVisibility(reloading ? View.VISIBLE : View.GONE);
        this.on_reloading_changed(this.pie_chart_reload_imageView, reloading);
    }

    private void on_line_scatter_chart_data_changed(@NonNull List<LineScatterPlotEntry> entries) {
        float max           = 0.0f;
        for (LineScatterPlotEntry e: entries) {
            final PointF pt = e.getScatterPoint();
            if (pt != null) {
                max             = Math.max(max, pt.y);
            }
        }
        final float mmax        = max;

        this.line_scatter_chart_view.execute(chart -> {
            chart.setXAxis(0.0f, Math.max(1, entries.size() - 1));
            chart.setYAxis(0.0f, mmax);
        });
        this.linePlot.setPoints(entries);
        this.scatterPlot.setPoints(entries);
    }
    private void on_reload_line_scatter_chart_data(View sender) {
        final Boolean reloading = this.line_scatter_chart_viewModel.getIsLoading().getValue();
        if (reloading != null && !reloading) {
            this.line_scatter_chart_viewModel.update(true);
        }
    }
    private void on_line_scatter_chart_data_is_reloading_changed(Boolean reloading) {
        this.line_scatter_chart_progressBar.setVisibility(reloading ? View.VISIBLE : View.GONE);
        this.on_reloading_changed(this.line_scatter_reload_imageView, reloading);
    }

    public final void onReload() {
        this.bar_chart_viewModel.update();
        this.pie_chart_viewModel.update();
        this.line_scatter_chart_viewModel.update();
    }

    public ChartFragment() {
        super();
        final Paint fill_paint_01           = new Paint.FillPaint(75, 175, 255, 255);
        final List<Paint> fill_paints_01    = new ArrayList<>();
        fill_paints_01.add(fill_paint_01);

        this.barPlot.setPaintCallback(this.bar_chart_paint_callback);
        this.piePlot.setPaintCallback(this.pie_chart_paint_callback);
        this.linePlot.setPaintCallback(this.line_plot_paint_callback);
        this.linePlot.setIsSmooth(true);
        this.scatterPlot.setPaintCallback(this.scatter_plot_paint_callback);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ViewModelProvider vmp         = ViewModelProviders.of(this);
        this.bar_chart_viewModel            = vmp.get(BarChartViewModel.class);
        this.pie_chart_viewModel            = vmp.get(PieChartViewModel.class);
        this.line_scatter_chart_viewModel   = vmp.get(LineScatterChartViewModel.class);

        this.bar_chart_viewModel.getPlotData().observe(this, this.bar_data_observer);
        this.bar_chart_viewModel.getIsLoading().observe(this, this.is_loading_bar_data_observer);
        this.pie_chart_viewModel.getPlotData().observe(this, this.pie_data_observer);
        this.pie_chart_viewModel.getIsLoading().observe(this, this.is_loading_pie_data_observer);
        this.line_scatter_chart_viewModel.getPlotData().observe(this, this.line_scatter_data_observer);
        this.line_scatter_chart_viewModel.getIsLoading().observe(this, this.is_loading_line_scatter_data_observer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.bar_chart_viewModel.getPlotData().removeObservers(this);
        this.bar_chart_viewModel.getIsLoading().removeObservers(this);
        this.pie_chart_viewModel.getPlotData().removeObservers(this);
        this.pie_chart_viewModel.getIsLoading().removeObservers(this);
        this.line_scatter_chart_viewModel.getPlotData().removeObservers(this);
        this.line_scatter_chart_viewModel.getIsLoading().removeObservers(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chart_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.bar_chart_view.load(view).execute(v -> v.addPlot(this.barPlot));
        this.bar_chart_progressBar.load(view);
        this.pie_chart_view.load(view).execute(v -> v.addPlot(this.piePlot));
        this.pie_chart_progressBar.load(view);
        this.line_scatter_chart_view.load(view).execute(v -> {
            v.setInsets(20.0f, 20.0f, 20.0f, 20.0f);
            v.addPlot(ChartFragment.this.linePlot);
            v.addPlot(ChartFragment.this.scatterPlot);
        });
        this.line_scatter_chart_progressBar.load(view);

        this.bar_chart_reload_imageView.load(view).execute(v -> v.setOnClickListener(ChartFragment.this::on_reload_bar_chart_data));
        this.pie_chart_reload_imageView.load(view).execute(v -> v.setOnClickListener(ChartFragment.this::on_reload_pie_chart_data));
        this.line_scatter_reload_imageView.load(view).execute(v -> v.setOnClickListener(ChartFragment.this::on_reload_line_scatter_chart_data));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        this.bar_chart_view.execute(ChartView::clearPlots).unload();
        this.bar_chart_progressBar.unload();
        this.pie_chart_view.execute(ChartView::clearPlots).unload();
        this.pie_chart_progressBar.unload();
        this.line_scatter_chart_view.execute(ChartView::clearPlots).unload();
        this.line_scatter_chart_progressBar.unload();

        this.bar_chart_reload_imageView.unload();
        this.pie_chart_reload_imageView.unload();
        this.line_scatter_reload_imageView.unload();
    }

    @Override
    public void onResume() {
        super.onResume();

        this.bar_chart_viewModel.update(false);
        this.pie_chart_viewModel.update(false);
        this.line_scatter_chart_viewModel.update(false);
    }

}
