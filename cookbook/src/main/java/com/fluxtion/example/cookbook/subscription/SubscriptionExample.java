package com.fluxtion.example.cookbook.subscription;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.builder.AssignToField;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.event.Signal.IntSignal;
import com.fluxtion.runtime.input.EventProcessorFeed;
import com.fluxtion.runtime.input.SubscriptionManager;

import java.util.HashSet;
import java.util.Set;

public class SubscriptionExample {

    public static void main(String[] args) {
        var processor1 = Fluxtion.compile(c -> c.addNode(
                new MySubscriberNode("subscribe_AGE", "graph1", null))
        );

        var processor2 = Fluxtion.compile(c -> c.addNode(
                new MySubscriberNode("subscribe_AGE", "graph2", null),
                new MySubscriberNode("subscribe_HEIGHT", "graph2", null)
                )
        );
        processor1.init();
        processor2.init();

        System.out.println("\nadding feeds:");
        IntSignalEventFeed eventFeed = new IntSignalEventFeed();
        processor1.addEventProcessorFeed(eventFeed);
        processor2.addEventProcessorFeed(eventFeed);

        //publish
        System.out.println("\npublishing from feed:");
        eventFeed.publish("subscribe_AGE", 20);
        eventFeed.publish("subscribe_AGE", 25);
        eventFeed.publish("subscribe_HEIGHT", 179);

        //stop processor2
        System.out.println("\ntear down a subscriber");
        processor2.tearDown();
        eventFeed.publish("subscribe_HEIGHT", 179);

        //restart processor2
        System.out.println("\nrestart subscriber");
        processor2.init();

        //publish
        System.out.println("\npublishing from feed:");
        eventFeed.publish("subscribe_HEIGHT", 179);
    }

    public static class MySubscriberNode {

        private final String subscriberId;
        private final String graphId;
        @Inject
        private final SubscriptionManager subscriptionManager;

        public MySubscriberNode(
                @AssignToField("subscriberId") String subscriberId,
                @AssignToField("graphId") String graphId,
                SubscriptionManager subscriptionManager) {
            this.subscriberId = subscriberId;
            this.graphId = graphId;
            this.subscriptionManager = subscriptionManager;
        }

        @Initialise
        public void init(){
            System.out.println("INIT " + graphId+ ":" + subscriberId + " subscribing");
            subscriptionManager.subscribe(subscriberId);
        }

        @OnEventHandler(filterVariable = "subscriberId")
        public void subscriptionId(IntSignal newSubscriptionSymbol) {
            System.out.println(
                    "UPDATE " + graphId+ ":" + subscriberId + " received -> " + newSubscriptionSymbol.getValue());
        }

    }

    public static class IntSignalEventFeed implements EventProcessorFeed {

        private final Set<StaticEventProcessor> targetProcessorSet = new HashSet<>();

        public void publish(String symbolId, int value){
            targetProcessorSet.forEach(e ->{
                e.publishIntSignal(symbolId, value);
            });
        }

        @Override
        public void subscribe(StaticEventProcessor target, Object subscriptionId) {
            if(!targetProcessorSet.contains(target)){
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
