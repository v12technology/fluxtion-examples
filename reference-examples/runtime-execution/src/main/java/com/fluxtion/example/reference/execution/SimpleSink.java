package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;

import java.util.function.Consumer;

public class SimpleSink {

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(cfg ->
                DataFlow.subscribeToIntSignal("myIntSignal")
                        .mapToObj(d -> "intValue:" + d)
                        .sink("mySink"));
        processor.init();
        processor.addSink("mySink", (Consumer<String>) System.out::println);
        processor.publishSignal("myIntSignal", 10);
        processor.publishSignal("myIntSignal", 256);
    }
}
