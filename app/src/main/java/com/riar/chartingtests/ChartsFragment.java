package com.riar.chartingtests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleObserver;

import com.riar.chartingtests.uielements.BarChartFragment;
import com.riar.chartingtests.uielements.LineScatterChartFragment;
import com.riar.chartingtests.uielements.PieChartFragment;
import com.riar.chartingtests.uielements.Updatable;

import java.util.ArrayList;
import java.util.List;

public class ChartsFragment extends Fragment implements LifecycleObserver {

    public static final String TAG  = ChartsFragment.class.getSimpleName();

    private List<Updatable> updatables  = new ArrayList<>();

    protected String[] hook_FragmentTags() {
        return new String[] {
                BarChartFragment.TAG,
                PieChartFragment.TAG,
                LineScatterChartFragment.TAG
        };
    }

    public final void onReload() {
        for (Updatable updatable: this.updatables) {
            updatable.update(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chart_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentManager cfm   = this.getChildFragmentManager();
        for (String tag: this.hook_FragmentTags()) {
            final Fragment fragment = cfm.findFragmentByTag(tag);
            if (fragment instanceof Updatable) {
                this.updatables.add((Updatable) fragment);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.updatables.clear();
    }
}
