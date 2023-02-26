package com.fluxtion.example.imperative.helloworld;

import com.fluxtion.runtime.annotations.OnTrigger;

public class BreachNotifier {
    private final DataSumCalculator dataAddition;

    public BreachNotifier(DataSumCalculator dataAddition) {
        this.dataAddition = dataAddition;
    }

    public BreachNotifier() {
        this(new DataSumCalculator());
    }

    @OnTrigger
    public void printWarning() {
        System.out.println("WARNING DataSumCalculator value is greater than 100 sum = " + dataAddition.getSum());
    }
}
