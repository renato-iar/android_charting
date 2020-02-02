package com.riar.observable;

import android.os.Handler;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ExclusiveObservable<Type> {

    // region types
    public interface WillChangeObserver<Type> {
        void willChangeValue(@NonNull ExclusiveObservable<Type> observable, Type from, Type to);
    }
    public interface DidChangeObserver<Type> {
        void didChangeValue(@NonNull ExclusiveObservable<Type> observable, Type from, Type to);
    }

    public interface Predicate<Type> {
        boolean test(@Nullable Type value);
    }

    public class Writer {

        // region private
        // region instance constants
        private Handler handler;
        // endregion instance constants

        // region constructors
        private Writer(@Nullable Handler handler) {
            this.handler    = handler;
        }
        private Writer() {
            this(null);
        }
        // endregion constructors
        // endregion private

        // region public
        // region instance methods
        public final void set(Type newValue) {
            if (this.handler != null && !this.handler.getLooper().isCurrentThread()) {
                this.handler.post(() -> ExclusiveObservable.this.setValue(newValue));
            }
            else {
                ExclusiveObservable.this.setValue(newValue);
            }
        }
        // endregion instance methods
        // endregion public

    }

    private final class Internal_PredicateBoundObserver {
        final @NonNull
        Predicate<Type> predicate;
        final @NonNull WeakReference<DidChangeObserver<Type>> observer;

        Internal_PredicateBoundObserver(@NonNull DidChangeObserver<Type> observer, @NonNull Predicate<Type> predicate) {
            this.predicate = predicate;
            this.observer = new WeakReference<>(observer);
        }
    }
    // endregion types

    // region private
    // region instance variables
    private Type value          = null;
    private int __silent        = 0;

    private final List<WeakReference<WillChangeObserver<Type>>> __will_change_observers = new ArrayList<>();
    private final List<WeakReference<DidChangeObserver<Type>>> __did_change_observers = new ArrayList<>();
    private final List<Internal_PredicateBoundObserver> __predicate_bound_did_change_observers = new ArrayList<>();
    // endregion instance variables

    // region instance methods
    /* package private */ void __will_change(Type from, Type to) {
        for (int i = 0; i < this.__will_change_observers.size(); ) {
            final WeakReference<WillChangeObserver<Type>> ref = this.__will_change_observers.get(i);
            if (ref.get() != null) {
                ref.get().willChangeValue(this, from, to);
                i++;
            }
            else {
                this.__will_change_observers.remove(i);
            }
        }
    }
    /* package private */ void __did_change(Type from, Type to) {
        for (int i = 0; i < this.__did_change_observers.size(); ) {
            final WeakReference<DidChangeObserver<Type>> ref = this.__did_change_observers.get(i);
            if (ref.get() != null) {
                ref.get().didChangeValue(this, from, to);
                i++;
            }
            else {
                this.__did_change_observers.remove(i);
            }
        }

        for (int i = 0; i < this.__predicate_bound_did_change_observers.size(); ) {
            final Internal_PredicateBoundObserver wrapper = this.__predicate_bound_did_change_observers.get(i);
            final DidChangeObserver<Type> observer = wrapper.observer.get();

            if (observer != null) {
                if (wrapper.predicate.test(to)) {
                    observer.didChangeValue(this, from, to);
                }
                i++;
            }
            else {
                this.__predicate_bound_did_change_observers.remove(i);
            }
        }
    }

    /* package private */ ExclusiveObservable<Type>.Writer __get_writer(@Nullable Handler handler) {
        return new ExclusiveObservable<Type>.Writer(handler);
    }
    /* package private */ ExclusiveObservable<Type>.Writer __get_writer() {
        return new ExclusiveObservable<Type>.Writer();
    }
    /* package private */ boolean __is_silent() {
        return this.__silent > 0;
    }
    /* package private */ void __silence(boolean flag) {
        if (flag) {
            this.__silent++;
        }
        else {
            this.__silent = Math.max(0, this.__silent - 1);
        }
    }

    /* package private */ void setValue(Type newValue) {
        final Type oldValue = this.value;

        if (!this.__is_silent()) {
            this.__will_change(oldValue, newValue);
        }
        this.value = newValue;
        if (!this.__is_silent()) {
            this.__did_change(oldValue, newValue);
        }
    }
    // endregion instance methods
    // endregion private

    // region public
    // region instance methods
    public final Type getValue() {
        return this.value;
    }

    /**
     * Register an observer {@link WillChangeObserver} for the observed value, which will be called
     * whenever the value is about to change.
     * Note that a <b>weak reference is kept on the observer</b>, which means the instance must be
     * retained somewhere else.
     *
     * @param observer The observer being registered.
     *
     * @see WeakReference
     * */
    public final void registerWillChangeObserver(@NonNull WillChangeObserver<Type> observer) {
        this.__will_change_observers.add(new WeakReference<>(observer));
    }
    public final void unregisterWillChangeObserver(@NonNull WillChangeObserver<Type> observer) {
        for (int i = 0; i < this.__will_change_observers.size(); ) {
            WeakReference<WillChangeObserver<Type>> ref = this.__will_change_observers.get(i);
            if (ref.get() != null && ref.get() == observer) {
                this.__will_change_observers.remove(i);
            }
            else {
                i++;
            }
        }
    }

    /**
     * Register an observer {@link DidChangeObserver} for the observed value, which will be called
     * whenever the value effectively changes.
     * Note that a <b>weak reference is kept on the observer</b>, which means the instance must be
     * retained somewhere else.
     *
     * @param observer The observer being registered.
     *
     * @see WeakReference
     * */
    public final void registerDidChangeObserver(@NonNull DidChangeObserver<Type> observer) {
        this.__did_change_observers.add(new WeakReference<>(observer));
    }
    public final void unregisterDidChangeObserver(@NonNull DidChangeObserver<Type> observer) {
        for (int i = 0; i < this.__did_change_observers.size(); ) {
            WeakReference<DidChangeObserver<Type>> ref = this.__did_change_observers.get(i);
            if (ref.get() != null && ref.get() == observer) {
                this.__did_change_observers.remove(i);
            }
            else {
                i++;
            }
        }
    }

    public final void when(@NonNull Predicate<Type> predicate, @NonNull DidChangeObserver<Type> observer) {
        this.__predicate_bound_did_change_observers.add(new Internal_PredicateBoundObserver(observer, predicate));
    }
    public final void when(final Type value, @NonNull DidChangeObserver<Type> observer) {
        this.when(new Predicate<Type>() {
            @Override
            public boolean test(Type other) {
                if (value == null) {
                    return other == null;
                }

                return value.equals(other);
            }
        }, observer);
    }
    public final void unregisterWhen(@NonNull DidChangeObserver<Type> observer) {
        for (int i = 0; i < this.__predicate_bound_did_change_observers.size(); ) {
            final Internal_PredicateBoundObserver wrapper = this.__predicate_bound_did_change_observers.get(i);
            if (wrapper.observer.get() == observer) {
                this.__predicate_bound_did_change_observers.remove(i);
            }
            else {
                i++;
            }
        }
    }

    public final void fire() {
        this.__did_change(this.value, this.value);
    }

    public final void execute(@NonNull Observable.ObservableAction<Type> callback) {
        callback.perform(this.getValue());
    }
    public final boolean executeIfNonNull(@NonNull Observable.ObservableAction<Type> action) {
        final Type value    = this.getValue();
        if (value != null) {
            action.perform(value);
            return true;
        }

        return false;
    }
    // endregion instance methods

    // region constructors
    public ExclusiveObservable(Type initialValue) {
        this.value = initialValue;
    }
    public ExclusiveObservable() {
        this(null);
    }
    // endregion constructors

    // region static methods
    @NonNull
    public static <Type> Pair<ExclusiveObservable<Type>, ExclusiveObservable<Type>.Writer> observable(Type initialValue, @Nullable Handler writerHandler) {

        final ExclusiveObservable<Type> observable = new ExclusiveObservable<>(initialValue);
        final ExclusiveObservable<Type>.Writer writer = observable.__get_writer(writerHandler);

        return new Pair<>(observable, writer);

    }
    public static <Type> Pair<ExclusiveObservable<Type>, ExclusiveObservable<Type>.Writer> observable(Type initialValue) {
        return ExclusiveObservable.observable(initialValue, null);
    }
    // endregion static methods
    // endregion public

}
