package com.fluxtion.example.imperative.helloworld;

import com.fluxtion.runtime.annotations.OnTrigger;

/**
 * The trigger method, printWarning on this class is invoked when a change is propagated from the parent node
 */
public class BreachNotifier {
    private final DataSumCalculator dataAddition;

    public BreachNotifier(DataSumCalculator dataAddition) {
        this.dataAddition = dataAddition;
    }

    public BreachNotifier() {
        this(new DataSumCalculator());
    }

    @OnTrigger
    public boolean printWarning() {
        System.out.println("WARNING DataSumCalculator value is greater than 100 sum = " + dataAddition.getSum());
        return true;
    }
}
