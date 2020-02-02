package com.riar.charts;

import android.graphics.Canvas;
import android.graphics.PointF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseTextPlot<EntryType extends TextPlotEntryValue> extends BasePlot {

    // region private
    // region instance constants
    private final List<EntryType> entries                                       = new ArrayList<>();
    // endregion instance constants

    // region instance variables
    private TextPlotEntryPropertiesCallback<EntryType> properties_callback    = null;
    // endregion instance variables
    // endregion private

    // region protected
    // region hooks
    @NonNull
    protected abstract PointF hook_EntryPositionInViewCoordinates(@NonNull EntryType entry, @Nullable TextPlotEntryProperties properties, int index, int count, @NonNull Canvas canvas, @NonNull DrawContext context);
    protected void hook_DrawEntry(@NonNull EntryType entry, @NonNull PointF location, @Nullable TextPlotEntryProperties properties, int index, int count, @NonNull Canvas canvas, @NonNull DrawContext context) {
        final String text   = entry.textPlotEntryValue();

        if (text != null && properties != null && !properties.hidden) {
            for (Paint paint: properties) {
                canvas.drawText(text, location.x, location.y, paint.paint);
            }
        }
    }
    // endregion hooks
    // endregion protected

    // region public
    // region instance methods
    public final int getEntriesCount() {
        return this.entries.size();
    }
    @NonNull
    public final EntryType getEntry(int index) {
        return this.entries.get(index);
    }
    public final void setEntries(@Nullable Collection<EntryType> entries) {
        this.entries.clear();
        if (entries != null) {
            this.entries.addAll(entries);
        }
        this.invalidate();
    }

    @Nullable
    public final TextPlotEntryPropertiesCallback<EntryType> getPropertiesCallback() {
        return this.properties_callback;
    }
    public final void setPropertiesCallback(@Nullable TextPlotEntryPropertiesCallback<EntryType> callback) {
        this.properties_callback    = callback;
        this.invalidate();
    }
    // endregion instance methods
    // endregion public

    // region overrides
    // region BasePlot
    @Override
    void onDraw(@NonNull Canvas canvas, @NonNull DrawContext context) {
        super.onDraw(canvas, context);

        final int n                                                 = this.getEntriesCount();
        final TextPlotEntryPropertiesCallback<EntryType> callback = this.getPropertiesCallback();

        for (int i = 0; i < n; i++) {
            final EntryType entry                                   = this.getEntry(i);
            final TextPlotEntryProperties properties              = callback == null ? null : callback.textPlotEntryPropertiesForEntryAtIndex(entry, i, n);
            final PointF point                                      = this.hook_EntryPositionInViewCoordinates(entry, properties, i, n, canvas, context);

            this.hook_DrawEntry(entry, point, properties, i, n, canvas, context);
        }
    }
    // endregion BasePlot
    // endregion overrides

}
