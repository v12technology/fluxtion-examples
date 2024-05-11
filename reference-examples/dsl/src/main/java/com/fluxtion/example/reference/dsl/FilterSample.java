package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;

public class FilterSample {
    public static void main(String[] args) {
        var processor = Fluxtion.interpret(c ->
                DataFlow.subscribe(Integer.class)
                        .filter(i -> i > 10)
                        .console("int {} > 10 ")
        );
        processor.init();

        processor.onEvent(1);
        processor.onEvent(17);
        processor.onEvent(4);
    }
}
