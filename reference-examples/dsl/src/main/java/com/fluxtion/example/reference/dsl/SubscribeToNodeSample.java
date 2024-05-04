package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.dataflow.helpers.Collectors;
import lombok.Getter;
import lombok.ToString;

public class SubscribeToNodeSample {

    @Getter
    @ToString
    public static class MyComplexNode {
        private String in;

        @OnEventHandler
        public boolean stringUpdate(String in) {
            this.in = in;
            return true;
        }
    }

    public static void buildGraph(EventProcessorConfig processorConfig) {
        DataFlow.subscribeToNode(new MyComplexNode())
                .console("node update trigger :{}")
                .map(MyComplexNode::getIn)
                .aggregate(Collectors.listFactory(4))
                .console("last 4 elements:{}");
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(SubscribeToNodeSample::buildGraph);
        processor.init();

        processor.onEvent("A");
        processor.onEvent("B");
        processor.onEvent("C");
        processor.onEvent("D");
        processor.onEvent("E");
        processor.onEvent("F");
    }
}
