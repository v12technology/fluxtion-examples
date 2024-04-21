package com.fluxtion.example.imperative.helloworld;

import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnTrigger;
import lombok.Getter;

/**
 * Aggregates two event sources and calculates the sum of their values, whenever either changes. The calculate method
 * notifies a propagation of a change when the sum is > 100
 */
public class DataSumCalculatorImpl implements @ExportService DataSumCalculator {

    @Getter
    private double sum;
    private double aValue = 0;
    private double bValue = 0;

    @Override
    public boolean updateA(Event_A eventA) {
        aValue = eventA.value();
        return checkSum();
    }

    @Override
    public boolean updateB(Event_B eventB) {
        bValue = eventB.value();
        return checkSum();
    }

    private boolean checkSum() {
        sum = aValue + bValue;
        System.out.println("sum:" + sum);
        return sum > 100;
    }
}
