package com.riar.chartingtests.models;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ChartViewModel<PlotDataType> extends ViewModel {

    private MutableLiveData<Boolean> is_loading             = new MutableLiveData<>(false);
    private MutableLiveData<List<PlotDataType>> plot_data   = new MutableLiveData<>(null);

    @NonNull
    public final LiveData<List<PlotDataType>> getPlotData() {
        return this.plot_data;
    }

    public final void update() {
        final List<PlotDataType> data   = this.plot_data.getValue();
        if (data == null) {

        }
        else {
            this.plot_data.notifyAll();
        }
    }

}
