package com.fluxtion.example.imperative.helloworld;

import com.fluxtion.runtime.annotations.OnTrigger;
import lombok.Getter;

/**
 * Aggregates two event sources and calculates the sum of their values, whenever either changes. The calculate method
 * notifies a propagation of a change when the sum is > 100
 */
public class DataSumCalculator {

    private final Event_A_Handler eventAHandler;
    private final Event_B_Handler eventBHandler;
    @Getter
    private double sum;

    public DataSumCalculator(Event_A_Handler eventAHandler, Event_B_Handler eventBHandler) {
        this.eventAHandler = eventAHandler;
        this.eventBHandler = eventBHandler;
    }

    public DataSumCalculator() {
        this(new Event_A_Handler(), new Event_B_Handler());
    }

    /**
     * The {@link OnTrigger} annotation marks this method to be called if any parents have changed
     *
     * @return flag indicating a change and a propagation of the event wave to child dependencies if the sum > 100
     */
    @OnTrigger
    public boolean calculate() {
        sum = eventAHandler.getValue() + eventBHandler.getValue();
        System.out.println("sum:" + sum);
        return sum > 100;
    }
}
