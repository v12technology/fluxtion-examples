package com.fluxtion.example.reference.dsl;

import com.fluxtion.runtime.dataflow.aggregate.AggregateFlowFunction;
import lombok.Getter;

import java.util.Date;

public class CustomAggregateFunctionSample {

    @Getter
    public static class Data3Aggregate implements AggregateFlowFunction<Date, Integer, Data3Aggregate> {
        int value;

        @Override
        public Integer reset() {
            return value;
        }

        @Override
        public Integer get() {
            return value;
        }

        @Override
        public Integer aggregate(Date input) {
            value += 1;//input.getX();
            return get();
        }
    }
}
