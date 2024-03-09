package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.annotations.OnEventHandler;

public class ReEntrant {
    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("received [" + stringToProcess +"]");
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(cfg -> {
                    DataFlow.subscribeToIntSignal("myIntSignal")
                            .mapToObj(d -> "intValue:" + d)
                            .console("republish re-entrant [{}]")
                            .processAsNewGraphEvent();
                    cfg.addNode(new MyNode());
                }
        );
        processor.init();
        processor.publishSignal("myIntSignal", 256);
    }
}
