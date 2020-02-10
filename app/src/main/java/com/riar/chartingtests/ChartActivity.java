package com.riar.chartingtests;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class ChartActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.chart_activity);
    }

    public void onReload(View sender) {
        final Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.chart_fragment);
        if (fragment instanceof ChartsFragment) {
            ((ChartsFragment) fragment).onReload();
        }
    }

}
