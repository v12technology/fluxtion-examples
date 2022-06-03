package com.fluxtion.example.imperative.helloworld;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;


/**
 * creates a processing graph imperatively, extracts double values from events, calculates the sum and prints a
 * message to console if the sum is greater than a 100.
 *
 * Uses @{@link OnEventHandler} annotation to declare the entry point of an execution path
 * {@link OnTrigger} annotated methods indicate call back methods to be invoked if a parent propagates a change.
 * The return flag from the {@link OnTrigger} method indicates if the event should be propagated. In this case
 * the event is only propagated if the sum > 100.
 */
public class Main {
    public static void main(String[] args) {
        EventProcessor eventProcessor = Fluxtion.interpret(cfg -> {
            SumLogger sumLogger = new SumLogger(
                    new DataAddition(
                            new Data1handler(), new Data2handler()));
            //need to add one root node so fluxtion can calculate the execution graph for the event processor
            cfg.addNode(sumLogger);
        });
        eventProcessor.init();
        eventProcessor.onEvent(new Data1(34.4));
        eventProcessor.onEvent(new Data2(52.1));
        eventProcessor.onEvent(new Data1(105));
        eventProcessor.onEvent(new Data1(12.4));
    }

    public record Data1(double value) {
    }

    public record Data2(double value) {
    }

    public static class Data1handler {
        private double value;

        /**
         * The {@link OnEventHandler} annotation marks this method as the start of an execution path with a {@link Data1}.
         * Invoked when the {@link com.fluxtion.runtime.EventProcessor} receives a {@link Data1} event.
         *
         * @param data1 the input event
         * @return flag indicating a change and a propagation of the event wave to child dependencies
         */
        @OnEventHandler
        public boolean data1Update(Data1 data1) {
            value = data1.value();
            return true;
        }

        public double getValue() {
            return value;
        }
    }

    public static class Data2handler {
        private double value;

        /**
         * The {@link OnEventHandler} annotation marks this method as the start of an execution path with a {@link Data2}.
         * Invoked when the {@link com.fluxtion.runtime.EventProcessor} receives a {@link Data2} event.
         *
         * @param data2 the input event
         * @return flag indicating a change and a propagation of the event wave to child dependencies
         */
        @OnEventHandler
        public boolean data1Update(Data2 data2) {
            value = data2.value();
            return true;
        }

        public double getValue() {
            return value;
        }
    }

    public static class DataAddition {

        private final Data1handler data1handler;
        private final Data2handler data2handler;
        private double sum;

        public DataAddition(Data1handler data1handler, Data2handler data2handler) {
            this.data1handler = data1handler;
            this.data2handler = data2handler;
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

    public static class SumLogger {
        private final DataAddition dataAddition;

        public SumLogger(DataAddition dataAddition) {
            this.dataAddition = dataAddition;
        }

        @OnTrigger
        public void printWarning() {
            System.out.println("WARNING Sum is greater than 100 sum = " + dataAddition.getSum());
        }
    }
}