package com.fluxtion.example.cookbook.parallel;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.example.cookbook.parallel.SimulatedTask.Synchronous;

public class Main_SynchronousTrigger {

    public static void main(String[] args) {
        var eventProcessor = Fluxtion.interpret(c -> {
            RequestHandler requestHandler = new RequestHandler();
            c.addNode(
                    TaskCollector.builder()
                            .task(new Synchronous("async1", 250, requestHandler))
                            .task(new Synchronous("async2", 225, requestHandler))
                            .task(new Synchronous("async3", 18, requestHandler))
                            .task(new Synchronous("async4", 185, requestHandler))
                            .requestHandler(requestHandler)
                            .build()
            );
        });
        eventProcessor.init();
        System.out.println("Synchronous trigger test");
        System.out.println("-".repeat(80));
        eventProcessor.onEvent("test");
    }
}
