package com.fluxtion.example.imperative.helloworld;

import com.fluxtion.runtime.annotations.OnEventHandler;

public class Data1handler {
    private double value;

    /**
     * The {@link OnEventHandler} annotation marks this method as the start of an execution path with a {@link InputDataEvent_1}.
     * Invoked when the {@link com.fluxtion.runtime.EventProcessor} receives a {@link InputDataEvent_1} event.
     *
     * @param data1 the input event
     * @return flag indicating a change and a propagation of the event wave to child dependencies
     */
    @OnEventHandler
    public boolean data1Update(InputDataEvent_1 data1) {
        value = data1.value();
        return true;
    }

    public double getValue() {
        return value;
    }
}
