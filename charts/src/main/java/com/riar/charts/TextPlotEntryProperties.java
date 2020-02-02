package com.riar.charts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TextPlotEntryProperties implements Iterable<Paint> {

    private final List<Paint> paints  = new ArrayList<>();

    public final float leftInset;
    public final float rightInset;
    public final boolean hidden;

    public final int getPaintsCount() {
        return this.paints.size();
    }
    @NonNull
    public final Paint getPaint(int index) {
        return this.paints.get(index);
    }

    public TextPlotEntryProperties(float leftInset, float rightInset, boolean hidden, @Nullable Collection<Paint> paints) {
        this.leftInset  = leftInset;
        this.rightInset = rightInset;
        this.hidden     = hidden;
        if (paints != null) {
            this.paints.addAll(paints);
        }
    }
    public TextPlotEntryProperties(float leftInset, float rightInset, boolean hidden) {
        this(leftInset, rightInset, hidden, null);
    }

    @NonNull
    @Override
    public Iterator<Paint> iterator() {
        return this.paints.iterator();
    }
}
