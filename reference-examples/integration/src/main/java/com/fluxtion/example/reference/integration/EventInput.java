package com.fluxtion.example.reference.integration;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.EventProcessor;

public class EventInput {

    public static void main(String[] args) {
        EventProcessor<?> processor = Fluxtion.interpret(c -> DataFlow.subscribe(String.class).console("Hello {}"));
        //lifecycle init required
        processor.init();

        //send event
        processor.onEvent("WORLD");
    }
}
