package com.fluxtion.example.cookbook_functional.statefulfilter;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.stream.EventFlow;
import com.fluxtion.example.cookbook_functional.events.MarketUpdate;
import com.fluxtion.runtime.EventProcessor;
//import com.fluxtion.runtime.stream.lookup.LongLookupPredicate;

import java.util.function.ToLongFunction;

public class Main {


    public static void main(String[] args) {
//        var eventProcessor = Fluxtion.interpret(c -> {
//            EventFlow.subscribe(MarketUpdate.class)
//                    .filterByProperty(
//                            MarketUpdate::id,
//                            LongLookupPredicate.buildPredicate("EURUSD", "marketRefData"))
//                    .console("Filtered :{}");
//
//        });
//
//        eventProcessor.injectNamedInstance((ToLongFunction<String>)new MarketReferenceData()::toId, ToLongFunction.class, "marketRefData");
//        eventProcessor.init();
//        System.out.println("No filtering - ignore all MarketUpdate's");
//        sendMarketEvents(eventProcessor);
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> generify(Class<?> cls) {
        return (Class<T>)cls;
    }

    private static void sendMarketEvents(EventProcessor<?> processor) {
        processor.onEvent(new MarketUpdate(10, "EURUSD", 1.05));
        processor.onEvent(new MarketUpdate(11, "EURCHF", 1.118));
        processor.onEvent(new MarketUpdate(10, "EURUSD", 1.07));
        processor.onEvent(new MarketUpdate(11, "EURCHF", 1.11));
        processor.onEvent(new MarketUpdate(11, "EURCHF", 1.10));
        processor.onEvent(new MarketUpdate(15, "USDGBP", 1.12));
        processor.onEvent(new MarketUpdate(15, "USDGBP", 1.14));
        processor.onEvent(new MarketUpdate(11, "EURCHF", 1.06));
        processor.onEvent(new MarketUpdate(15, "USDGBP", 1.15));
    }

    public static class MarketReferenceData{

        public long toId(String marketName){
            return switch (marketName){
                case "EURUSD" -> 10;
                case "EURCHF" -> 11;
                case "USDGBP" -> 15;
                default -> Long.MAX_VALUE;
            };
        }
    }
    //OrderAcceptorImpl
}