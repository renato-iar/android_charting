package com.riar.charts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface TextPlotEntryPropertiesCallback<EntryType extends TextPlotEntryValue> {

    @Nullable
    TextPlotEntryProperties textPlotEntryPropertiesForEntryAtIndex(@NonNull EntryType entry, int index, int count);

}
