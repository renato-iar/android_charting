package com.riar.outlets;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Outlet<UIType extends View> {

    private static final String TAG = Outlet.class.getSimpleName();

    public interface OutletAction<UIType extends View> {
        void perform(@NonNull UIType uiElement);
    }
    public interface OutletQuery<UIType extends View, ResultType> {
        ResultType query(@Nullable UIType uiElement);
    }

    private int ui_element_id   = 0;
    private UIType ui_element   = null;

    @IdRes
    public final int getUIElementId() {
        return this.ui_element_id;
    }
    public final void setUIElementId(@IdRes int id) {
        if (id == this.ui_element_id) {
            return;
        }

        this.ui_element_id  = id;
        this.ui_element     = null;
    }

    @Nullable
    public final UIType get() {
        return this.ui_element;
    }

    public final void action(@NonNull OutletAction<UIType> action) {
        if (this.ui_element != null) {
            action.perform(this.ui_element);
        }
    }
    @Nullable
    public final <ResultType> ResultType query(@NonNull OutletQuery<UIType, ResultType> q) {
        if (this.ui_element == null) {
            return null;
        }
        return q.query(this.ui_element);
    }
    @NonNull
    public final <ResultType> ResultType query(@NonNull ResultType defaultValue, @NonNull OutletQuery<UIType, ResultType> q) {
        final ResultType r  = this.query(q);
        return r == null ? defaultValue : r;
    }

    public final boolean load(@Nullable View view) {

        this.ui_element = null;

        if (view != null && this.ui_element_id != 0) {
            this.ui_element = view.findViewById(this.ui_element_id);
        }

        return this.ui_element != null;
    }
    public final boolean load(@Nullable Activity activity) {

        this.ui_element = null;

        if (activity != null && this.ui_element_id != 0) {
            this.ui_element = activity.findViewById(this.ui_element_id);
        }

        return this.ui_element != null;

    }
    public final void unload() {
        this.ui_element = null;
    }

    public final void setVisibility(final int visibility) {
        this.action(v -> v.setVisibility(visibility));
    }
    public final int getVisibility() {
        return this.query(View.GONE, View::getVisibility);
    }

    public final void setAlpha(final float alpha) {
        this.action(v -> v.setAlpha(alpha));
    }
    public final float getAlpha() {
        return this.query(0.0f, View::getAlpha);
    }

    @Nullable
    public final ViewGroup.LayoutParams getLayoutParams() {
        return this.query(View::getLayoutParams);
    }
    public final void setLayoutParams(@NonNull ViewGroup.LayoutParams params) {
        this.action(v -> v.setLayoutParams(params));
    }

    public Outlet(@IdRes int id) {
        this.ui_element_id  = id;
    }
    public Outlet() {
        this(0);
    }

}
