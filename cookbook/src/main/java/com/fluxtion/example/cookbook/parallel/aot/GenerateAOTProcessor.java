package com.fluxtion.example.cookbook.parallel.aot;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.example.cookbook.parallel.RequestHandler;
import com.fluxtion.example.cookbook.parallel.SimulatedTask.Asynchronous;
import com.fluxtion.example.cookbook.parallel.TaskCollector;

/**
 * Builds an AOT processor
 */
public class GenerateAOTProcessor {

    public static void main(String[] args) throws NoSuchFieldException {
        Fluxtion.compileAot(c -> {
            RequestHandler requestHandler = new RequestHandler();
            c.addNode(
                    TaskCollector.builder()
                            .task(new Asynchronous("async1", 250, requestHandler))
                            .task(new Asynchronous("async2", 225, requestHandler))
                            .task(new Asynchronous("async3", 18, requestHandler))
                            .task(new Asynchronous("async4", 185, requestHandler))
                            .requestHandler(requestHandler)
                            .build(), "taskCollector"
            );
        }, "com.fluxtion.example.cookbook.parallel.aot.generated", "AotParallelProcessor");
    }

}
