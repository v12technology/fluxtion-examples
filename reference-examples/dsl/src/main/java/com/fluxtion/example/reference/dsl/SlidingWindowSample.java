package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.helpers.Aggregates;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SlidingWindowSample {

    public static void buildGraph(EventProcessorConfig processorConfig) {
        DataFlow.subscribe(Integer.class)
                .slidingAggregate(Aggregates.intSumFactory(), 300, 4)
                .console("current sliding 1.2 second sum:{} timeDelta:%dt");
    }

    public static void main(String[] args) throws InterruptedException {
        var processor = Fluxtion.interpret(SlidingWindowSample::buildGraph);
        processor.init();
        Random rand = new Random();

        try (ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor()) {
            executor.scheduleAtFixedRate(
                    () -> {
                        processor.onEvent(rand.nextInt(100));
                    },
                    10,10, TimeUnit.MILLISECONDS);
            Thread.sleep(4_000);
        }
    }
}
