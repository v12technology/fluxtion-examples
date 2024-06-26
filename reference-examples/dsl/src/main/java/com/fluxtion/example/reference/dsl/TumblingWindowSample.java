package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.helpers.Aggregates;

import java.util.Random;
import java.util.concurrent.*;

public class TumblingWindowSample {

    public static void buildGraph(EventProcessorConfig processorConfig) {
        DataFlow.subscribe(Integer.class)
                .tumblingAggregate(Aggregates.intSumFactory(), 300)
                .console("current tumble sum:{} timeDelta:%dt");
    }

    public static void main(String[] args) throws InterruptedException {
        var processor = Fluxtion.interpret(TumblingWindowSample::buildGraph);
        processor.init();
        Random rand = new Random();

        try (ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor()) {
            executor.scheduleAtFixedRate(
                    () -> processor.onEvent(rand.nextInt(100)),
                    10,10, TimeUnit.MILLISECONDS);
            Thread.sleep(4_000);
        }
    }
}
