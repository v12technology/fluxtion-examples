package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.groupby.GroupBy;
import com.fluxtion.runtime.dataflow.groupby.GroupByKey;
import com.fluxtion.runtime.dataflow.helpers.Aggregates;
import com.fluxtion.runtime.partition.LambdaReflection;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SlidingGroupByCompoundKeySample {
    public record Trade(String symbol, String client, int amountTraded) {}
    private static String[] symbols = new String[]{"GOOG", "AMZN", "MSFT", "TKM"};
    private static String[] clients = new String[]{"client_A", "client_B", "client_D", "client_E"};

    public static void buildGraph(EventProcessorConfig processorConfig) {
        DataFlow.subscribe(Trade.class)
                .groupBySliding(
                        GroupByKey.build(Trade::client, Trade::symbol),
                        Trade::amountTraded,
                        Aggregates.intSumFactory(),
                        250, 4)
                .map(SlidingGroupByCompoundKeySample::formatGroupBy)
                .console("Trade volume for last second by client and symbol time:%e:\n{}----------------------\n");
    }

    private static <T> String formatGroupBy(GroupBy<GroupByKey<T>, Integer> groupBy) {
        Map<GroupByKey<T>, Integer> groupByMap = groupBy.toMap();
        StringBuilder stringBuilder = new StringBuilder();
        groupByMap.forEach((k, v) -> stringBuilder.append(k.getKey() + ": " + v + "\n"));
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws InterruptedException {
        var processor = Fluxtion.interpret(SlidingGroupByCompoundKeySample::buildGraph);
        processor.init();
        Random rand = new Random();

        try (ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor()) {
            executor.scheduleAtFixedRate(
                    () -> {
                        processor.onEvent("tick");
                        processor.onEvent(new Trade(symbols[rand.nextInt(symbols.length)], clients[rand.nextInt(clients.length)], rand.nextInt(100)));
                    },
                    10,10, TimeUnit.MILLISECONDS);
            Thread.sleep(4_000);
        }
    }
}
