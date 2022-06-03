package com.fluxtion.example.functional.slidingwindow;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.SEPConfig;
import com.fluxtion.compiler.builder.stream.EventFlow;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.stream.helpers.Aggregates;
import com.fluxtion.runtime.stream.helpers.Predicates;


public class Main {

    public static void main(String[] args) {
        EventProcessor eventProcessor = Fluxtion.compile(Main::buildTradeMonitor);
        eventProcessor.init();
        TradeHelper.publishTrades(eventProcessor::onEvent, 15);
    }

    private static void buildTradeMonitor(SEPConfig cfg) {
        EventFlow.subscribe(Trade.class)
                .console("\t rcvd {}")
                .groupBySliding(Trade::id, Aggregates.counting(), 1000, 3)
                .map(Predicates.topN(5))
                .console("top trades by count:{}\n");
    }

    public record Trade(String id, double value, double size, long timeMillis) {
    }

}