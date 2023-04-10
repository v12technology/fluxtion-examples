package com.fluxtion.example.cookbook.parallel;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.example.cookbook.parallel.SimulatedTask.Asynchronous;

public class Main_ParallelTrigger {

    public static void main(String[] args) {
        var eventProcessor = Fluxtion.interpret(c -> {
            RequestHandler requestHandler = new RequestHandler();
            c.addNode(
                    TaskCollector.builder()
                            .task(new Asynchronous("async1", 250, requestHandler))
                            .task(new Asynchronous("async2", 225, requestHandler))
                            .task(new Asynchronous("async3", 18, requestHandler))
                            .task(new Asynchronous("async4", 185, requestHandler))
                            .requestHandler(requestHandler)
                            .build()
            );
        });
        eventProcessor.init();
        System.out.println("Parallel trigger test");
        System.out.println("-".repeat(80));
        eventProcessor.onEvent("test");
    }
}
