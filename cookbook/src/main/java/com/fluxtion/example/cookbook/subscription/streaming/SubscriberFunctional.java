package com.fluxtion.example.cookbook.subscription.streaming;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.stream.EventFlow;
import com.fluxtion.example.cookbook.subscription.MarketDataFeed;
import com.fluxtion.example.cookbook.subscription.SharePriceEvent;

/**
 * Example demonstrating the use of the {@link com.fluxtion.runtime.input.SubscriptionManager} and how it binds graph node subscription request to
 * {@link com.fluxtion.runtime.input.EventFeed}'s that live outside the graph. Stream subscriptions for the {@link SharePriceEvent}
 * are dynamically created and added to the graph. The streaming api removes the need to create
 * {@link com.fluxtion.example.cookbook.subscription.imperative.SharePriceNode} by the user.
 *
 * <p>
 * Running the example produces:
 * <pre>
 *
 * subscriber registered
 * MarketDataFeed adding new subscriber, count:1
 * MarketDataFeed subscription:EventSubscription{eventClass=class com.fluxtion.example.cookbook.subscription.SharePriceEvent, filterString=MSFT}
 * MarketDataFeed subscription:EventSubscription{eventClass=class com.fluxtion.example.cookbook.subscription.SharePriceEvent, filterString=AMZN}
 *
 * publishing prices from MarketDataFeed:
 * SharePriceNode:MSFT -> SharePriceEvent[symbolId=MSFT, price=21.36]
 * SharePriceNode:MSFT -> SharePriceEvent[symbolId=MSFT, price=22.11]
 * SharePriceNode:AMZN -> SharePriceEvent[symbolId=AMZN, price=72.6]
 *
 * tear down marketPriceProcessor:
 * remove subscription:EventSubscription{eventClass=class com.fluxtion.example.cookbook.subscription.SharePriceEvent, filterString=MSFT} subscriber:com.fluxtion.compiler.generation.targets.InMemoryEventProcessor@13c9d689
 * remove subscription:EventSubscription{eventClass=class com.fluxtion.example.cookbook.subscription.SharePriceEvent, filterString=AMZN} subscriber:com.fluxtion.compiler.generation.targets.InMemoryEventProcessor@13c9d689
 * MarketDataFeed removing subscriber, count:0
 *
 * restart marketPriceProcessor:
 * MarketDataFeed adding new subscriber, count:1
 * MarketDataFeed subscription:EventSubscription{eventClass=class com.fluxtion.example.cookbook.subscription.SharePriceEvent, filterString=AMZN}
 * MarketDataFeed subscription:EventSubscription{eventClass=class com.fluxtion.example.cookbook.subscription.SharePriceEvent, filterString=MSFT}
 *
 * publishing prices from MarketDataFeed:
 * SharePriceNode:MSFT -> SharePriceEvent[symbolId=MSFT, price=22.51]
 * </pre>
 */
public class SubscriberFunctional {

    public static void main(String[] args) {
        var marketPriceProcessor = Fluxtion.interpret(c -> {
            EventFlow.subscribe(SharePriceEvent.class, "MSFT").console("SharePriceNode:MSFT -> {}");
            EventFlow.subscribe(SharePriceEvent.class, "AMZN").console("SharePriceNode:AMZN -> {}");
        });
        marketPriceProcessor.init();

        MarketDataFeed eventFeed = new MarketDataFeed();
        marketPriceProcessor.addEventFeed(eventFeed);

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
        System.out.println("\npublishing prices from MarketDataFeed:");
        eventFeed.publish("MSFT", 22.51);
    }

}
