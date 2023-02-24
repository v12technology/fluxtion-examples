package com.fluxtion.example.cookbook.subscription;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.input.EventProcessorFeed;
import com.fluxtion.runtime.input.SubscriptionManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Example demonstrating the use of the {@link SubscriptionManager} and how it binds graph node subscription request to
 * {@link EventProcessorFeed}'s that live outside the graph
 */
public class SubscriptionExample {

    public static void main(String[] args) {
        var marketPriceProcessor = Fluxtion.interpret(c -> c.addNode(
                new SharePriceSubscriber("MSFT"),
                new SharePriceSubscriber("AMZN")
        ));
        marketPriceProcessor.init();

        System.out.println("\nadding feeds:");
        MarketDataFeed eventFeed = new MarketDataFeed();
        marketPriceProcessor.addEventProcessorFeed(eventFeed);

        System.out.println("\npublishing from feed:");
        eventFeed.publish("MSFT", 21.36);
        eventFeed.publish("MSFT", 22.11);
        eventFeed.publish("AMZN", 72.6);
        eventFeed.publish("IBM", 25);
        eventFeed.publish("GOOGL", 179);

        System.out.println("\ntear down a subscriber");
        marketPriceProcessor.tearDown();
        eventFeed.publish("MSFT", 23.64);

        System.out.println("\nrestart subscriber");
        marketPriceProcessor.init();
        eventFeed.publish("MSFT", 22.51);
    }

    public record AssetPrice(String symbolId, double price) implements Event {
        public String filterString() {
            return symbolId;
        }
    }

    public static class SharePriceSubscriber {

        private final String symbolId;
        @Inject
        public SubscriptionManager subscriptionManager;

        public SharePriceSubscriber(String symbolId) {
            this.symbolId = symbolId;
        }

        @Initialise
        public void init() {
            subscriptionManager.subscribe(symbolId);
        }

        @OnEventHandler(filterVariable = "symbolId")
        public void AssetPrice(AssetPrice assetPriceUpdate) {
            System.out.println("subscriber:" + symbolId + " -> " + assetPriceUpdate);
        }
    }

    public static class MarketDataFeed implements EventProcessorFeed {

        private final Set<StaticEventProcessor> targetProcessorSet = new HashSet<>();

        public void publish(String symbolId, double price) {
            targetProcessorSet.forEach(e -> {
                e.onEvent(new AssetPrice(symbolId, price));
            });
        }

        @Override
        public void subscribe(StaticEventProcessor target, Object subscriptionId) {
            if (!targetProcessorSet.contains(target)) {
                targetProcessorSet.add(target);
                System.out.println("FEED adding processor current subscriber count:" + targetProcessorSet.size());
            }
        }

        @Override
        public void unSubscribe(StaticEventProcessor target, Object subscriptionId) {
            //some complex unsubscription logic
        }

        @Override
        public void removeAllSubscriptions(StaticEventProcessor eventProcessor) {
            targetProcessorSet.remove(eventProcessor);
            System.out.println("FEED removing processor current subscriber count:" + targetProcessorSet.size());
        }
    }
}
