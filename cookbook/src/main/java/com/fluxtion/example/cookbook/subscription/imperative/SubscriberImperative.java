package com.fluxtion.example.cookbook.subscription.imperative;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.example.cookbook.subscription.MarketDataFeed;

/**
 * Example demonstrating the use of the {@link com.fluxtion.runtime.input.SubscriptionManager} and how it binds graph node subscription request to
 * {@link com.fluxtion.runtime.input.EventFeed}'s that live outside the graph. The event handler methods apply Fluxtion in built filtering
 * using the symbol id. This removes the need to filter in the nodes eventhandler code.
 *
 * <p>
 * Running the example produces:
 * <pre>
 *
 * subscriber registered
 * MarketDataFeed adding new subscriber, count:1
 * MarketDataFeed subscription:MSFT
 * MarketDataFeed subscription:AMZN
 *
 * publishing prices from MarketDataFeed:
 * SharePriceNode:MSFT -> SharePriceEvent[symbolId=MSFT, price=21.36]
 * SharePriceNode:MSFT -> SharePriceEvent[symbolId=MSFT, price=22.11]
 * SharePriceNode:AMZN -> SharePriceEvent[symbolId=AMZN, price=72.6]
 *
 * tear down marketPriceProcessor:
 * remove subscription:AMZN subscriber:com.fluxtion.compiler.generation.targets.InMemoryEventProcessor@29f69090
 * remove subscription:MSFT subscriber:com.fluxtion.compiler.generation.targets.InMemoryEventProcessor@29f69090
 * MarketDataFeed removing subscriber, count:0
 *
 * restart marketPriceProcessor:
 * MarketDataFeed adding new subscriber, count:1
 * MarketDataFeed subscription:MSFT
 * MarketDataFeed subscription:AMZN
 *
 * publishing prices from MarketDataFeed:
 * SharePriceNode:MSFT -> SharePriceEvent[symbolId=MSFT, price=22.51]
 * </pre>
 */
public class SubscriberImperative {

    public static void main(String[] args) {
        var marketPriceProcessor = Fluxtion.interpret(c -> c.addNode(
                new SharePriceNode("MSFT"),
                new SharePriceNode("AMZN")
        ));
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
