package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.aggregate.function.primitive.IntSumFlowFunction;

import java.util.Random;
import java.util.concurrent.*;

public class TumblingWindowSample {

    public static void buildGraph(EventProcessorConfig processorConfig) {
        DataFlow.subscribe(Integer.class)
                .tumblingAggregate(IntSumFlowFunction::new, 300)
                .console("current tumble sum:{} eventTime:%e");
    }

    public static void main(String[] args) throws InterruptedException {
        var processor = Fluxtion.interpret(TumblingWindowSample::buildGraph);
        processor.init();
        Random rand = new Random();

        try (ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor()) {
            executor.scheduleAtFixedRate(
                    () -> {
                        processor.onEvent("tick");
                        processor.onEvent(rand.nextInt(100));
                    },
                    10,10, TimeUnit.MILLISECONDS);
            Thread.sleep(4_000);
        }
    }
}
