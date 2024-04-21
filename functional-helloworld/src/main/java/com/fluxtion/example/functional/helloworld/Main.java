package com.fluxtion.example.functional.helloworld;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.EventProcessor;


/**
 * Simple Fluxtion hello world stream example. Add two numbers and log when sum > 100
 * <ul>
 *     <li>Subscribe to two event streams, Data1 and Data1</li>
 *     <li>Map the double values of each stream using getter</li>
 *     <li>Apply a stateless binary function {@link Double#sum(double, double)}</li>
 *     <li>Apply a filter that logs to console when the sum > 100</li>
 * </ul>
 */
public class Main {
    public static void main(String[] args) {
        //build the EventProcessor and initialise it
        var eventProcessor = Fluxtion.interpret(cfg -> {
            var data1Stream = DataFlow.subscribe(Data1.class)
                    .mapToDouble(Data1::value)
                    .defaultValue(0);

            DataFlow.subscribe(Data2.class)
                    .mapToDouble(Data2::value)
                    .defaultValue(0)
                    .mapBiFunction(Double::sum, data1Stream)
                    .console("sum:{}")
                    .filter(d -> d > 100)
                    .console("WARNING DataSumCalculator value is greater than 100 sum = {}");
        });
        eventProcessor.init();

        //send events
        eventProcessor.onEvent(new Data1(34));
        eventProcessor.onEvent(new Data2(52.1));
        eventProcessor.onEvent(new Data1(105));//should create a breach warning
        eventProcessor.onEvent(new Data1(12.4));
    }

    public record Data1(double value) {
    }

    public record Data2(double value) {
    }
}

