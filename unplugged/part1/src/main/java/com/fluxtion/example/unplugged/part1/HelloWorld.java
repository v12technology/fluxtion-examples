package com.fluxtion.example.unplugged.part1;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.TearDown;

import static com.fluxtion.compiler.builder.stream.EventFlow.subscribe;

/**
 * Simple Fluxtion hello world stream example. Add two numbers and log when sum > 100
 * <ul>
 *     <li>Subscribe to two event streams, Data1 and Data1</li>
 *     <li>Map the double values of each stream using getter</li>
 *     <li>Apply a stateless binary function {@link Double#sum(double, double)}</li>
 *     <li>Apply a filter that logs to console when the sum > 100</li>
 * </ul>
 */
public class HelloWorld {
    public static void main(String[] args) {
        //builds the EventProcessor
        EventProcessor eventProcessor = Fluxtion.compile(cfg -> {
            var data1Stream = subscribe(Data1.class)
                    .console("rcvd -> {}")
                    .mapToDouble(Data1::value);

            subscribe(Data2.class)
                    .console("rcvd -> {}")
                    .mapToDouble(Data2::value)
                    .map(Double::sum, data1Stream)
//                    .filter(d -> d > 100)
                    .console("OUT: sum {} > 100");
        });
        //init and send events
        eventProcessor.init();
        //no output < 100
        eventProcessor.onEvent(new Data1(20.5));
        //no output < 100
        eventProcessor.onEvent(new Data2(63));
        //output > 100 - log to console
        eventProcessor.onEvent(new Data1(56.8));
    }

    public record Data1(double value) {
    }

    public record Data2(double value) {
    }

    public static class MyNode{
        @Initialise
        public void myInitMethod(){}

        @TearDown
        public void myTearDownMethod(){}

    }
}
