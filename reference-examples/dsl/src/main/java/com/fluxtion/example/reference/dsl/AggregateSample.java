package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.groupby.GroupBy;
import com.fluxtion.runtime.dataflow.helpers.Aggregates;
import com.fluxtion.runtime.dataflow.helpers.Collectors;

public class AggregateSample {

    public record ResetList() {
    }

    public static void buildGraph(EventProcessorConfig processorConfig) {
        var resetSignal = DataFlow.subscribe(ResetList.class).console("\n--- RESET ---");

        DataFlow.subscribe(String.class)
                .aggregate(Collectors.listFactory(3))
                .resetTrigger(resetSignal)
                .console("ROLLING list: {}");

        DataFlow.subscribe(Integer.class)
                .groupBy(i -> i % 2 == 0 ? "evens" : "odds", Aggregates.countFactory())
                .resetTrigger(resetSignal)
                .map(GroupBy::toMap)
                .console("ODD/EVEN map:{}");
    }


    public static void main(String[] args) {
        var processor = Fluxtion.interpret(AggregateSample::buildGraph);
        processor.init();
        processor.onEvent(1);
        processor.onEvent("A");
        processor.onEvent("B");
        processor.onEvent("C");
        processor.onEvent("D");
        processor.onEvent("E");
        processor.onEvent(2);

        processor.onEvent(new ResetList());
        processor.onEvent(5);
        processor.onEvent(7);
        processor.onEvent("P");
        processor.onEvent("Q");
        processor.onEvent("R");

        processor.onEvent(new ResetList());
        processor.onEvent("XX");
        processor.onEvent("YY");
    }
}
