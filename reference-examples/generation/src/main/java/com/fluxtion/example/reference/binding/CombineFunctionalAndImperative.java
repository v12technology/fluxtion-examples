package com.fluxtion.example.reference.binding;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.annotations.OnEventHandler;

public class CombineFunctionalAndImperative {

    public static String toUpper(String incoming){
        return incoming.toUpperCase();
    }

    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("IMPERATIVE received:" + stringToProcess);
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(cfg -> {
            DataFlow.subscribe(String.class)
                    .console("FUNCTIONAL input: '{}'")
                    .map(CombineFunctionalAndImperative::toUpper)
                    .console("FUNCTIONAL transformed: '{}'");

            cfg.addNode(new MyNode());
        });

        processor.init();
        processor.onEvent("hello world");
    }
}
