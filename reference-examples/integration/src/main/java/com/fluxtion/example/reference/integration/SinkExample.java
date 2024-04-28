package com.fluxtion.example.reference.integration;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;

import java.util.function.Consumer;

public class SinkExample {

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(cfg ->
                DataFlow.subscribeToIntSignal("myIntSignal")
                        .mapToObj(d -> "intValue:" + d)
                        .sink("mySink")//CREATE A SINK IN THE PROCESSOR
        );
        processor.init();

        //ADDING A SINK
        processor.addSink("mySink", (Consumer<String>) System.out::println);

        processor.publishSignal("myIntSignal", 10);
        processor.publishSignal("myIntSignal", 256);

        //REMOVING A SINK
        processor.removeSink("mySink");
        processor.publishSignal("myIntSignal", 512);
    }
}
