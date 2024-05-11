package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.helpers.Mappers;

import static com.fluxtion.compiler.builder.dataflow.DataFlow.subscribe;

public class MergeSample {
    public static void main(String[] args) {
        var processor = Fluxtion.interpret(c ->
                DataFlow.merge(
                        subscribe(Long.class).console("long : {}"),
                        subscribe(String.class).console("string : {}").map(Mappers::parseLong),
                        subscribe(Integer.class).console("int : {}").map(Integer::longValue))
                        .console("MERGED FLOW -> {}")
        );
        processor.init();

        processor.onEvent(1234567890835L);
        processor.onEvent("9994567890835");
        processor.onEvent(123);
    }
}
