package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;

public class MapSample {
    public static void main(String[] args) {
        var processor = Fluxtion.interpret(c ->
                DataFlow.subscribe(String.class)
                        .map(String::toLowerCase)
                        .console("string in {}")
        );
        processor.init();

        processor.onEvent("AAA");
        processor.onEvent("BBB");
    }
}
