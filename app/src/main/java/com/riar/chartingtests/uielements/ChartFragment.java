package com.riar.chartingtests.uielements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.riar.chartingtests.R;
import com.riar.chartingtests.models.ChartViewModel;
import com.riar.charts.BasePlot;
import com.riar.charts.ChartView;
import com.riar.outlets.Outlet;

import java.util.ArrayList;
import java.util.List;

public abstract class ChartFragment<PlotDataType, ModelType extends ChartViewModel<PlotDataType>, PlotType extends BasePlot> extends Fragment implements Updatable {

    // region private
    private ModelType model;
    private final List<PlotType> plots  = new ArrayList<>();

    private void on_is_loading_changed(Boolean loading) {
        this.hook_OnIsLoadingChanged(loading == null ? false : loading);
    }
    private void on_data_changed(List<PlotDataType> data) {
        this.hook_OnDataChanged(data == null ? new ArrayList<>() : data);
    }
    private void on_reload_action(View view) {
        this.hook_OnReload();
    }
    // endregion private

    // region package private
    final List<PlotType> getPlots() {
        return this.plots;
    }
    // endregion package private

    // region protected
    protected final Outlet<ChartView> chartView             = new Outlet<>();
    protected final Outlet<ProgressBar> loadingProgressBar  = new Outlet<>();
    protected final Outlet<View> reloadActionView           = new Outlet<>();

    @NonNull
    protected abstract List<PlotType> hook_InstancePlots();

    protected abstract Class<ModelType> hook_GetModelClass();

    @IdRes
    protected abstract int hook_LoadingProgressBarId();

    @IdRes
    protected abstract int hook_ReloadActionViewId();

    @IdRes
    protected abstract int hook_ChartViewId();

    @LayoutRes
    protected abstract int hook_LayoutId();

    protected abstract void hook_OnDataChanged(@NonNull List<PlotDataType> data);

    protected void hook_OnIsLoadingChanged(@NonNull Boolean loading) {
        if (loading) {
            final Animation animation   = AnimationUtils.loadAnimation(this.getContext(), R.anim.indefinite_right_rotation);
            this.reloadActionView.execute(view -> {
                view.setAnimation(animation);
                view.setAlpha(0.350f);
            });
            this.loadingProgressBar.setVisibility(View.VISIBLE);
        }
        else {
            this.reloadActionView.execute(view -> {
                view.clearAnimation();
                view.setAlpha(1.0f);
            });
            this.loadingProgressBar.setVisibility(View.GONE);
        }
    }

    protected void hook_OnReload() {
        this.getModel().update(true);
    }

    @CallSuper
    protected void hook_SetupChart(@NonNull ChartView chart) {
        final List<PlotType> plots  = this.getPlots();
        for (PlotType plot: plots) {
            chart.addPlot(plot);
        }
    }

    @NonNull
    protected final PlotType getPlot(int index) {
        return this.plots.get(index);
    }
    protected final int getPlotCount() {
        return this.plots.size();
    }

    @NonNull
    protected final ModelType getModel() {
        return this.model;
    }
    // endregion protected

    // region public
    public final void update(boolean force) {
        this.getModel().update(force);
    }

    public ChartFragment() {
        super();

        this.chartView.setUIElementId(this.hook_ChartViewId());
        this.loadingProgressBar.setUIElementId(this.hook_LoadingProgressBarId());
        this.reloadActionView.setUIElementId(this.hook_ReloadActionViewId());
    }
    // endregion public

    // region overrides
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getPlots().addAll(this.hook_InstancePlots());

        this.model  = ViewModelProviders.of(this).get(this.hook_GetModelClass());
        this.model.getIsLoading().observe(this, ChartFragment.this::on_is_loading_changed);
        this.model.getPlotData().observe(this, ChartFragment.this::on_data_changed);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.model.getIsLoading().removeObservers(this);
        this.model.getPlotData().removeObservers(this);

        this.getPlots().clear();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(this.hook_LayoutId(), container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.chartView.load(view).execute(ChartFragment.this::hook_SetupChart);
        this.reloadActionView.load(view).execute(v -> v.setOnClickListener(ChartFragment.this::on_reload_action));
        this.loadingProgressBar.load(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        this.chartView.execute(ChartView::clearPlots).unload();
        this.reloadActionView.unload();
        this.loadingProgressBar.unload();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getModel().update();
    }
    // endregion overrides

}
