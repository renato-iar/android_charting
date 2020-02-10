package com.riar.chartingtests.uielements;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.riar.chartingtests.R;
import com.riar.outlets.Outlet;

public class PlotLabelView extends LinearLayout {

    private final Outlet<TextView> label_textView           = new Outlet<>(R.id.chart_label_textView);
    private final Outlet<ImageView> label_color_imageView   = new Outlet<>(R.id.chart_label_color_imageView);

    private void setup_ui(@NonNull Context context) {
        LayoutInflater.from(context).inflate(R.layout.chart_label_layout, this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.label_textView.load(this);
        this.label_color_imageView.load(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        this.label_textView.unload();
        this.label_color_imageView.unload();
    }

    @Nullable
    public final CharSequence getText() {
        return this.label_textView.query(TextView::getText);
    }
    public final void setText(@Nullable CharSequence text) {
        this.label_textView.execute(tv -> tv.setText(text));
    }
    public final void setText(@StringRes int id) {
        this.label_textView.execute(tv -> tv.setText(id));
    }

    public final void setColorViewColorResource(@ColorRes int id) {
        @ColorInt final int color = this.getContext().getColor(id);
        this.setColorViewColor(color);
    }
    public final void setColorViewColor(@ColorInt int color) {
        this.label_color_imageView.execute(
                v -> v.setColorFilter(color)
        );
    }

    public PlotLabelView(Context context) {
        super(context);
        this.setup_ui(context);
    }
    public PlotLabelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setup_ui(context);
    }
    public PlotLabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setup_ui(context);
    }
    public PlotLabelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setup_ui(context);
    }

}
