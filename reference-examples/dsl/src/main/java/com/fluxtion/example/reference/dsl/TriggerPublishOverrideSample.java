package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.helpers.Collectors;

public class TriggerPublishOverrideSample {

    public static void buildGraph(EventProcessorConfig processorConfig) {
        DataFlow.subscribeToNode(new SubscribeToNodeSample.MyComplexNode())
                .console("node triggered -> {}")
                .map(SubscribeToNodeSample.MyComplexNode::getIn)
                .aggregate(Collectors.listFactory(4))
                .publishTriggerOverride(DataFlow.subscribeToSignal("publishMe"))
                .console("last 4 elements:{}\n");
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(TriggerPublishOverrideSample::buildGraph);
        processor.init();

        processor.onEvent("A");
        processor.onEvent("B");
        processor.onEvent("C");
        processor.onEvent("D");

        processor.publishSignal("publishMe");
        processor.onEvent("E");
        processor.onEvent("F");
        processor.onEvent("G");
        processor.onEvent("H");

        processor.publishSignal("publishMe");
    }
}
