package com.fluxtion.example.reference;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;

public class FunctionalStatic {

    public static String toUpper(String incoming){
        return incoming.toUpperCase();
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(cfg -> {
            DataFlow.subscribe(String.class)
                    .console("input:{}")
                    .map(FunctionalStatic::toUpper)
                    .console("transformed:{}");
        });

        processor.init();
        processor.onEvent("hello world");
    }
}
