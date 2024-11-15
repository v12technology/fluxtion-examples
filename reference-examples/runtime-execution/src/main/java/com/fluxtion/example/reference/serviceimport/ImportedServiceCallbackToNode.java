package com.fluxtion.example.reference.serviceimport;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.annotations.runtime.ServiceRegistered;
import com.fluxtion.runtime.callback.CallBackNode;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Registers a callback node listener method with an externally imported service. The callback will be to the node directly
 * and not the parent event processor. This one to one callback to the node instance bypasses the event processor dispatch.
 *
 * <ul>
 *     <li>Create a MarketDataSubscriberNode that implements and exports the listener call back interface, MarketDataSubscriber and extends CallBackNode</li>
 *     <li>Annotate a node method with {@code @ServiceRegistered} to access the external MarketDataPublisher service</li>
 *     <li>Register a MarketDataPublisher service with the event processor</li>
 *     <li>Register the node instance as a market data callback with the MarketDataPublisher</li>
 * </ul>
 *
 * <p>
 * The MarketDataSubscriberNode implements listener interface, MarketDataSubscriber, and receives the market
 * data update callbacks directly and not via the parent event processor. The node decides whether to trigger dependent
 * nodes by calling CallBackNode#fireCallback from the super class {@link CallBackNode}, in this case MidLoggerNode
 *
 *
 * <pre>
 *
 * running the example:
 *
 * MarketDataSubscriberNode - triggerGraphCycle mid changed AAA 23.24
 * MidLoggerNode - NEW mid: 23.24
 *
 * MarketDataSubscriberNode - ignore marketUpdate: AAA 23.24
 * MarketDataSubscriberNode - triggerGraphCycle mid changed AAA 19.99
 * MidLoggerNode - NEW mid: 19.99
 *
 * MarketDataSubscriberNode - ignore marketUpdate: AAA 19.99
 * MarketDataSubscriberNode - ignore marketUpdate: AAA 19.99
 * MarketDataSubscriberNode - ignore marketUpdate: AAA 19.99
 * MarketDataSubscriberNode - triggerGraphCycle mid changed AAA 15.67
 * MidLoggerNode - NEW mid: 15.67
 * MarketDataSubscriberNode - triggerGraphCycle mid changed AAA 23.24
 * MidLoggerNode - NEW mid: 23.24
 *
 * MarketDataSubscriberNode - ignore marketUpdate: AAA 23.24
 * MarketDataSubscriberNode - triggerGraphCycle mid changed AAA 19.99
 * MidLoggerNode - NEW mid: 19.99
 *
 * MarketDataSubscriberNode - ignore marketUpdate: AAA 19.99
 * MarketDataSubscriberNode - ignore marketUpdate: AAA 19.99
 * MarketDataSubscriberNode - ignore marketUpdate: AAA 19.99
 * MarketDataSubscriberNode - triggerGraphCycle mid changed AAA 15.67
 * MidLoggerNode - NEW mid: 15.67
 * marketUpdate: AAA 15.67
 * </pre>
 */
public class ImportedServiceCallbackToNode {

    public static void main(String[] args) {
        EventProcessor<?> processor = Fluxtion.interpret(c -> c.addNode(new MidLoggerNode(new MarketDataSubscriberNode())));
        processor.init();

        //create a simple market data publisher service and register with the processor
        AtomicReference<MarketDataSubscriber> subscriber = new AtomicReference<>();
        processor.registerService(
                (symbol, callback) -> subscriber.set(callback),
                MarketDataPublisher.class
        );

        //publish some data
        subscriber.get().marketUpdate("AAA", 23.24);
        subscriber.get().marketUpdate("AAA", 23.24);
        subscriber.get().marketUpdate("AAA", 19.99);
        subscriber.get().marketUpdate("AAA", 19.99);
        subscriber.get().marketUpdate("AAA", 19.99);
        subscriber.get().marketUpdate("AAA", 19.99);
        subscriber.get().marketUpdate("AAA", 15.67);
    }

    public interface MarketDataPublisher {
        void subscribe(String symbol, MarketDataSubscriber callback);
    }

    public interface MarketDataSubscriber {
        boolean marketUpdate(String symbol, double mid);
    }

    //The node in the graph that is a MarketDataSubscriber.
    //DOES NOT EXPORT SERVICE MarketDataSubscriber.
    //Receives updates from MarketDataPublisher directly into this instance bypassing the event processor dispatch
    //Extend the CallBackNode and call CallBackNode#fireCallback to trigger the event processor with this node as the root dirty node
    public static class MarketDataSubscriberNode
            extends CallBackNode
            implements
            MarketDataSubscriber {

        private double mid;

        //Annotated callback method called at runtime when a matching service is registered
        //with the parent event processor
        @ServiceRegistered
        public void marketDataPublisher(MarketDataPublisher marketDataPublisher) {
            //Subscribes the parent event processor with this instance
            marketDataPublisher.subscribe("AAA", this);
        }

        //MarketDataPublisher service directly invokes callback method, this node can call CallBackNode#fireCallback
        //to start a graph cycle and trigger any dependent nodes such as the MidLoggerNode
        @Override
        public boolean marketUpdate(String symbol, double mid) {
            double oldMid = this.mid;
            this.mid = mid;
            if (oldMid == this.mid) {
                System.out.println("MarketDataSubscriberNode - ignore marketUpdate: " + symbol + " " + mid);
            } else {
                System.out.println("MarketDataSubscriberNode - triggerGraphCycle mid changed " + symbol + " " + mid);
                fireCallback();
            }
            return true;//ignored value as this method is not called via the parent eventprocessor
        }
    }

    public record MidLoggerNode(MarketDataSubscriberNode marketDataSubscriber) {
        @OnTrigger
        public boolean midUpdated() {
            System.out.println("MidLoggerNode - NEW mid: " + marketDataSubscriber.mid + "\n");
            return true;
        }
    }
}
