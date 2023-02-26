package com.fluxtion.example.cookbook.subscription;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.input.EventFeed;

import java.util.HashSet;
import java.util.Set;

public class MarketDataFeed implements EventFeed {

    private final Set<StaticEventProcessor> subscriberSet = new HashSet<>();

    public void publish(String symbolId, double price) {
        subscriberSet.forEach(e -> {
            e.onEvent(new SharePriceEvent(symbolId, price));
        });
    }

    @Override
    public void registerSubscriber(StaticEventProcessor subscriber) {
        System.out.println("subscriber registered");
    }

    @Override
    public void subscribe(StaticEventProcessor subscriber, Object subscriptionId) {
        if (!subscriberSet.contains(subscriber)) {
            subscriberSet.add(subscriber);
            System.out.println("MarketDataFeed adding new subscriber, count:" + subscriberSet.size());
        }
        System.out.println("MarketDataFeed subscription:" + subscriptionId);
    }

    @Override
    public void unSubscribe(StaticEventProcessor subscriber, Object subscriptionId) {
        System.out.println("remove subscription:" + subscriptionId + " subscriber:" + subscriber);
    }

    @Override
    public void removeAllSubscriptions(StaticEventProcessor subscriber) {
        subscriberSet.remove(subscriber);
        System.out.println("MarketDataFeed removing subscriber, count:" + subscriberSet.size());
    }
}
