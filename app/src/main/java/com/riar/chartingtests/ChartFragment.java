package com.riar.chartingtests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.riar.chartingtests.models.BarChartViewModel;
import com.riar.chartingtests.models.LineScatterChartViewModel;
import com.riar.chartingtests.models.PieChartViewModel;
import com.riar.charts.ChartView;
import com.riar.outlets.Outlet;

public class ChartFragment extends Fragment implements LifecycleObserver {

    public static final String TAG  = ChartFragment.class.getSimpleName();

    private final Outlet<ChartView> bar_chart_view          = new Outlet<>(R.id.bar_chart);
    private final Outlet<ChartView> pie_chart_view          = new Outlet<>(R.id.pie_chart);
    private final Outlet<ChartView> line_scatter_chart_view = new Outlet<>(R.id.line_scatter_chart);

    private BarChartViewModel bar_chart_viewModel;
    private PieChartViewModel pie_chart_viewModel;
    private LineScatterChartViewModel line_scatter_chart_viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ViewModelProvider vmp         = ViewModelProviders.of(this);
        this.bar_chart_viewModel            = vmp.get(BarChartViewModel.class);
        this.pie_chart_viewModel            = vmp.get(PieChartViewModel.class);
        this.line_scatter_chart_viewModel   = vmp.get(LineScatterChartViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chart_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.bar_chart_view.load(view);
        this.pie_chart_view.load(view);
        this.line_scatter_chart_view.load(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        this.bar_chart_view.unload();
        this.pie_chart_view.unload();
        this.line_scatter_chart_view.unload();
    }

    @Override
    public void onResume() {
        super.onResume();


    }

}
