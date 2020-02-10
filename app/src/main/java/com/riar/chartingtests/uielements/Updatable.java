package com.riar.chartingtests.uielements;

public interface Updatable {

    void update(boolean force);

    default void update() {
        this.update(false);
    }

}
