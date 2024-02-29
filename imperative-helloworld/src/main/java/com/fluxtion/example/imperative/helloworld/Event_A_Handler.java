package com.fluxtion.example.imperative.helloworld;

import com.fluxtion.runtime.annotations.OnEventHandler;
import lombok.Getter;

/**
 * An entry point to {@link com.fluxtion.runtime.EventProcessor} for {@link Event_A} events. The {@link OnEventHandler}
 * defines a method as an entry point.
 *
 */
@Getter
public class Event_A_Handler {
    private double value;

    /**
     * The {@link OnEventHandler} annotation marks this method as the start of an execution path with a {@link Event_A}.
     * Invoked when the {@link com.fluxtion.runtime.EventProcessor} receives a {@link Event_A} event.
     *
     * @param eventA the input event
     * @return flag indicating a change and a propagation of the event wave to child dependencies
     */
    @OnEventHandler
    public boolean data1Update(Event_A eventA) {
        value = eventA.value();
        return true;
    }

}
