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
 * {@link EventProcessorFeed}'s that live outside the graph. The event handler methods apply Fluxtion in built filtering
 * using the symbol id. This removes the need to filter in the client consumer code.
 *
 * <p>
 * Running the example produces:
 * <pre>
 *
 * MarketDataFeed adding EventProcessor as a sink, count:1
 *
 * publishing prices from MarketDataFeed:
 * subscriber:MSFT -> SharePrice[symbolId=MSFT, price=21.36]
 * subscriber:MSFT -> SharePrice[symbolId=MSFT, price=22.11]
 * subscriber:AMZN -> SharePrice[symbolId=AMZN, price=72.6]
 *
 * tear down marketPriceProcessor:
 * MarketDataFeed removing EventProcessor as sink, count:0
 *
 * restart marketPriceProcessor:
 * MarketDataFeed adding EventProcessor as a sink, count:1
 * subscriber:MSFT -> SharePrice[symbolId=MSFT, price=22.51]
 * </pre>
 */
public class SubscriptionExample {

    public static void main(String[] args) {
        var marketPriceProcessor = Fluxtion.interpret(c -> c.addNode(
                new SharePriceSubscriber("MSFT"),
                new SharePriceSubscriber("AMZN")
        ));
        marketPriceProcessor.init();

        MarketDataFeed eventFeed = new MarketDataFeed();
        marketPriceProcessor.addEventProcessorFeed(eventFeed);

        System.out.println("\npublishing prices from MarketDataFeed:");
        eventFeed.publish("MSFT", 21.36);
        eventFeed.publish("MSFT", 22.11);
        eventFeed.publish("IBM", 25);
        eventFeed.publish("AMZN", 72.6);
        eventFeed.publish("GOOGL", 179);

        System.out.println("\ntear down marketPriceProcessor:");
        marketPriceProcessor.tearDown();
        eventFeed.publish("MSFT", 23.64);

        System.out.println("\nrestart marketPriceProcessor:");
        marketPriceProcessor.init();
        eventFeed.publish("MSFT", 22.51);
    }

    public record SharePrice(String symbolId, double price) implements Event {
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
        public void AssetPrice(SharePrice assetPriceUpdate) {
            System.out.println("subscriber:" + symbolId + " -> " + assetPriceUpdate);
        }
    }

    public static class MarketDataFeed implements EventProcessorFeed {

        private final Set<StaticEventProcessor> targetProcessorSet = new HashSet<>();

        public void publish(String symbolId, double price) {
            targetProcessorSet.forEach(e -> {
                e.onEvent(new SharePrice(symbolId, price));
            });
        }

        @Override
        public void subscribe(StaticEventProcessor target, Object subscriptionId) {
            if (!targetProcessorSet.contains(target)) {
                targetProcessorSet.add(target);
                System.out.println("MarketDataFeed adding EventProcessor as a sink, count:" + targetProcessorSet.size());
            }
        }

        @Override
        public void unSubscribe(StaticEventProcessor target, Object subscriptionId) {
            //some complex unsubscription logic
        }

        @Override
        public void removeAllSubscriptions(StaticEventProcessor eventProcessor) {
            targetProcessorSet.remove(eventProcessor);
            System.out.println("MarketDataFeed removing EventProcessor as sink, count:" + targetProcessorSet.size());
        }
    }
}
