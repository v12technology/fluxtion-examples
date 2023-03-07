package com.fluxtion.example.cookbook_functional.dynamicfilter;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.stream.EventFlow;
import com.fluxtion.compiler.builder.stream.EventStreamBuilder;
import com.fluxtion.example.cookbook_functional.events.MarketUpdate;
import com.fluxtion.example.cookbook_functional.events.Subscription;
import com.fluxtion.runtime.EventProcessor;

public class Main {

    public static void main(String[] args) {

        var eventProcessor = Fluxtion.interpret(c -> {
            EventFlow.subscribe(MarketUpdate.class)
                    .filter(Main::isSubscribed, EventFlow.subscribe(Subscription.class))
                    .console("Filtered :{}");

        });
        eventProcessor.init();
        System.out.println("No filtering - ignore all MarketUpdate's");
        sendMarketEvents(eventProcessor);
        //now set the filter for EURUSD and send the same events
        System.out.println("\nSet dynamically filter to id:10, should see EURUSD MarketUpdate's");
        eventProcessor.onEvent(new Subscription(10));
        sendMarketEvents(eventProcessor);
        //now set the filter EURCH and send the same events
        System.out.println("\nSet dynamically filter to id:11, should see EURCHF MarketUpdate's");
        eventProcessor.onEvent(new Subscription(11));
        sendMarketEvents(eventProcessor);
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


    public static boolean isSubscribed(MarketUpdate id1, Subscription id2){
        return id1.id() == id2.id();
    }
}
