package com.fluxtion.example.imperative.helloworld;

import com.fluxtion.runtime.annotations.OnTrigger;

public class DataSumCalculator {

    private final Data1handler data1handler;
    private final Data2handler data2handler;
    private double sum;

    public DataSumCalculator(Data1handler data1handler, Data2handler data2handler) {
        this.data1handler = data1handler;
        this.data2handler = data2handler;
    }

    public DataSumCalculator() {
        this(new Data1handler(), new Data2handler());
    }

    /**
     * The {@link OnTrigger} annotation marks this method to be called if any parents have changed
     *
     * @return flag indicating a change and a propagation of the event wave to child dependencies if the sum > 100
     */
    @OnTrigger
    public boolean calculate() {
        sum = data1handler.getValue() + data2handler.getValue();
        System.out.println("sum:" + sum);
        return sum > 100;
    }

    public double getSum() {
        return sum;
    }
}
