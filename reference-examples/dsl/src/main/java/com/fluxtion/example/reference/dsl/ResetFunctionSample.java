package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.Stateful;

public class ResetFunctionSample {

    public static class MyResetSum implements Stateful<Integer> {
        public int count = 0;

        public int increment(Object o){
            return ++count;
        }

        @Override
        public Integer reset() {
            System.out.println("--- RESET CALLED ---");
            count = 0;
            return count;
        }
    }

    public static void buildGraph(EventProcessorConfig processorConfig) {
        DataFlow.subscribe(String.class)
                .map(new MyResetSum()::increment)
                .resetTrigger(DataFlow.subscribeToSignal("resetMe"))
                .console("count:{}");
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(ResetFunctionSample::buildGraph);
        processor.init();

        processor.onEvent("A");
        processor.onEvent("B");
        processor.onEvent("C");
        processor.onEvent("D");

        processor.publishSignal("resetMe");
        processor.onEvent("E");
        processor.onEvent("F");
    }


}
