package com.fluxtion.example.imperative.helloworld;

import com.fluxtion.runtime.annotations.OnEventHandler;
import lombok.Getter;

/**
 * An entry point to {@link com.fluxtion.runtime.EventProcessor} for {@link Event_B} events. The {@link OnEventHandler}
 * defines a method as an entry point.
 *
 */
@Getter
public class Event_B_Handler {
    private double value;

    /**
     * The {@link OnEventHandler} annotation marks this method as the start of an execution path with a {@link Event_B}.
     * Invoked when the {@link com.fluxtion.runtime.EventProcessor} receives a {@link Event_B} event.
     *
     * @param eventB the input event
     * @return flag indicating a change and a propagation of the event wave to child dependencies
     */
    @OnEventHandler
    public boolean data1Update(Event_B eventB) {
        value = eventB.value();
        return true;
    }
}
