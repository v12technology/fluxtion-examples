package com.fluxtion.example.reference.dsl.frontpage_examples;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.dataflow.helpers.Aggregates;
import com.fluxtion.runtime.event.Event;

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
                    10, 10, TimeUnit.MILLISECONDS);
            Thread.sleep(4_000);
        }


        //create a streaming event processor
        var sep = Fluxtion.interpret(c ->
                //subscribe to "car speeds" messages
                DataFlow.subscribe(Message.class, "car speeds")
                        //map function get speed
                        .map(Message::speed)
                        //filter and warn if speed > 100
                        .filter(s -> s > 100)
                        .map(s -> "warning speed > 100 detected")
                        //publish to a sink output
                        .sink("average car speed"));

        //init the streaming event processor and connect to your event flow source
        sep.init();
        connectEventFlow(sep);
    }

    public record Message(String filterString, String message) implements Event {
        public double speed() {
            return 1;
        }
    }

    public static void connectEventFlow(EventProcessor sep) {

    }

}
