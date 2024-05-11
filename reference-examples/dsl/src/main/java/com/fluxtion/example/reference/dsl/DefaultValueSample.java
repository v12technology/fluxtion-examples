package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;

public class DefaultValueSample {
    public static void main(String[] args) {
        var processor = Fluxtion.interpret(c -> {
            var strings = DataFlow.subscribe(String.class).defaultValue("99999944");
            var ints = DataFlow.subscribe(Integer.class);
            DataFlow.mapBiFunction((a, b) -> Integer.parseInt(a) + b, strings, ints)
                    .console("biMap with default value ans: {}");
        });
        processor.init();

        processor.onEvent(55);
    }
}
