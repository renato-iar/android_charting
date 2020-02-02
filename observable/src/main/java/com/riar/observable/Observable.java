package com.riar.observable;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Observable<Type> extends ExclusiveObservable<Type> {

    // region public
    // region types
    public interface ObservableAction<Type> {
        void perform(@NonNull Type object);
    }
    // endregion types

    // region instance methods
    @NonNull
    public final Observable<Type>.Writer getWriter(@Nullable Handler handler) {
        return this.__get_writer(handler);
    }
    @NonNull
    public final Observable<Type>.Writer getWriter() {
        return this.getWriter(null);
    }

    public final boolean isSilent() {
        return this.__is_silent();
    }
    public final void silently(@NonNull ObservableAction<Writer> completion) {
        this.__silence(true);
        completion.perform(this.getWriter());
        this.__silence(false);
    }
    // endregion instance methods

    // region constructors
    public Observable(Type value) {
        super(value);
    }
    public Observable() {
        super();
    }
    // endregion constructors
    // endregion public

}
