package com.riar.chartingtests.models;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public abstract class ChartViewModel<PlotDataType> extends ViewModel {

    private final MutableLiveData<Boolean> is_loading           = new MutableLiveData<>(false);
    private final MutableLiveData<List<PlotDataType>> plot_data = new MutableLiveData<>(new ArrayList<>());
    private final Random random                                 = new Random();
    private Timer update_timer                                  = null;
    private TimerTask update_task                               = null;

    private void cancel_pending_updates() {
        if (this.update_timer != null) {
            this.update_timer.purge();
            this.update_timer   = null;
        }
    }
    private void schedule_update(long interval) {
        this.cancel_pending_updates();

        this.is_loading.postValue(true);
        this.update_task    = new TimerTask() {
            @Override
            public void run() {
                ChartViewModel.this.cancel_pending_updates();
                ChartViewModel.this.is_loading.postValue(false);
                ChartViewModel.this.on_update_fired();
            }
        };
        this.update_timer   = new Timer();
        this.update_timer.schedule(this.update_task, interval);
    }
    private void on_update_fired() {
        this.plot_data.postValue(this.hook_InstancePlotData());
    }

    protected long hook_UpdateTimerInterval() {
        return 1000L + this.random.nextInt(2500);
    }
    @NonNull
    protected abstract List<PlotDataType> hook_InstancePlotData();

    @NonNull
    public final LiveData<List<PlotDataType>> getPlotData() {
        return this.plot_data;
    }

    @NonNull
    public final LiveData<Boolean> getIsLoading() {
        return this.is_loading;
    }

    public final void update(boolean force) {
        final List<PlotDataType> data   = this.plot_data.getValue();
        if (force || data == null || data.isEmpty()) {
            this.schedule_update(this.hook_UpdateTimerInterval());
        }
        else {
            this.plot_data.postValue(this.plot_data.getValue());
        }
    }
    public final void update() {
        this.update(true);
    }

}
