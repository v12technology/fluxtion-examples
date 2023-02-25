package com.fluxtion.example.cookbook.subscription;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.input.EventFeed;

import java.util.HashSet;
import java.util.Set;

public class MarketDataFeed implements EventFeed {

    private final Set<StaticEventProcessor> targetProcessorSet = new HashSet<>();

    public void publish(String symbolId, double price) {
        targetProcessorSet.forEach(e -> {
            e.onEvent(new SharePrice(symbolId, price));
        });
    }

    @Override
    public void registerFeedTarget(StaticEventProcessor staticEventProcessor) {
        //do nothing feed arrived
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
