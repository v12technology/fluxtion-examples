package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.aggregate.AggregateFlowFunction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomAggregateFunctionSample {
    public static class DateRangeAggregate implements AggregateFlowFunction<LocalDate, String, DateRangeAggregate> {
        private LocalDate startDate;
        private LocalDate endDate;
        private String message;
        private final transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public String reset() {
            System.out.println("--- RESET ---");
            startDate = null;
            endDate = null;
            message = null;
            return get();
        }

        @Override
        public String get() {
            return message;
        }

        @Override
        public String aggregate(LocalDate input) {
            startDate = startDate == null ? input : startDate;
            endDate = endDate == null ? input : endDate;
            if (input.isBefore(startDate)) {
                startDate = input;
            } else if (input.isAfter(endDate)) {
                endDate = input;
            } else {
                //RETURN NULL -> NO CHANGE NOTIFICATIONS FIRED
                return null;
            }
            message = formatter.format(startDate) + " - " + formatter.format(endDate);
            return message;
        }
    }

    public static void buildGraph(EventProcessorConfig processorConfig) {
        DataFlow.subscribe(LocalDate.class)
                .aggregate(DateRangeAggregate::new)
                .resetTrigger(DataFlow.subscribeToSignal("resetDateRange"))
                .console("UPDATED date range : '{}'");
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(CustomAggregateFunctionSample::buildGraph);
        processor.init();

        processor.onEvent(LocalDate.of(2019, 8, 10));
        processor.onEvent(LocalDate.of(2009, 6, 14));
        processor.onEvent(LocalDate.of(2024, 4, 22));
        processor.onEvent(LocalDate.of(2021, 3, 30));

        //reset
        processor.publishSignal("resetDateRange");
        processor.onEvent(LocalDate.of(2019, 8, 10));
        processor.onEvent(LocalDate.of(2021, 3, 30));
    }
}
