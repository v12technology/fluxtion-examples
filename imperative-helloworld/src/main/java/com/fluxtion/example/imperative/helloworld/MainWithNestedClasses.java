package com.fluxtion.example.imperative.helloworld;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
import lombok.Getter;

public class MainWithNestedClasses {

    public static void main(String[] args) {
        var eventProcessor = Fluxtion.interpret(new BreachNotifier());
        eventProcessor.init();
        eventProcessor.onEvent(new Event_A(34.4));
        eventProcessor.onEvent(new Event_B(52.1));
        eventProcessor.onEvent(new Event_A(105));//should create a breach warning
        eventProcessor.onEvent(new Event_A(12.4));
    }

    public record Event_A(double value) {
    }

    public record Event_B(double value) {
    }

    /**
     * An entry point to {@link com.fluxtion.runtime.EventProcessor} for {@link Event_A} events. The {@link OnEventHandler}
     * defines a method as an entry point.
     *
     */
    @Getter
    public static class Event_A_Handler {
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

    /**
     * An entry point to {@link com.fluxtion.runtime.EventProcessor} for {@link Event_B} events. The {@link OnEventHandler}
     * defines a method as an entry point.
     *
     */
    @Getter
    public static class Event_B_Handler {
        private double value;

        /**
         * The {@link OnEventHandler} annotation marks this method as the start of an execution path with a {@link Event_B}.
         * Invoked when the {@link com.fluxtion.runtime.EventProcessor} receives a {@link Event_B} event.
         *
         * @param eventB the input event
         * @return flag indicating a change and a propagation of the event wave to child dependencies
         */
        @OnEventHandler
        public boolean data1Update(Event_B eventB) {
            value = eventB.value();
            return true;
        }
    }

    /**
     * Aggregates two event sources and calculates the sum of their values, whenever either changes. The calculate method
     * notifies a propagation of a change when the sum is > 100
     */
    public static class DataSumCalculator {

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

    /**
     * The trigger method, printWarning on this class is invoked when a change is propagated from the parent node
     */
    public static class BreachNotifier {
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
}
