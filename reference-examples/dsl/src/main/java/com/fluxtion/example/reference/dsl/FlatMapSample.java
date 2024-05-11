package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;

import java.util.Arrays;

public class FlatMapSample {

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(c ->
                DataFlow.subscribe(String.class)
                        .console("\ncsv in [{}]")
                        .flatMap(s -> Arrays.asList(s.split(",")))
                        .console("flattened item [{}]"));
        processor.init();

        processor.onEvent("A,B,C");
        processor.onEvent("2,3,5,7,11");
    }
}
