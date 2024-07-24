package com.fluxtion.example.reference.serviceimport;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.EventProcessorContext;
import com.fluxtion.runtime.EventProcessorContextListener;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.runtime.ServiceRegistered;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Registers a callback listener method with an externally imported service.
 *
 * <ul>
 *     <li>Register a MarketDataPublisher service with the event processor that registers MarketDataSubscriber</li>
 *     <li>Create a MarketDataSubscriberNode that implements and exports the listener call back interface, MarketDataSubscriber</li>
 *     <li>Annotate a node method with {@code @ServiceRegistered} to access the external MarketDataPublisher service</li>
 *     <li>Register the subscription callback with the MarketDataPublisher using the EventProcessor.exportedService</li>
 * </ul>
 *
 * The event processor implements the exported listener interface, MarketDataSubscriber, and receives the market
 * data update callbacks. The processor dispatches and market updates to the MarketDataSubscriberNode or any other node
 * that exports the MarketDataSubscriber interface.
 *
 *
 * <pre>
 *
 * running the example:
 *
 * marketUpdate: AAA 23.24
 * marketUpdate: AAA 19.99
 * marketUpdate: AAA 15.67
 * </pre>
 */
public class ServiceCallback {

    public static void main(String[] args) {
        EventProcessor processor = Fluxtion.interpret(new MarketDataSubscriberNode());
        processor.init();

        //create a simple market data publisher service and register with the processor
        AtomicReference<MarketDataSubscriber> subscriber = new AtomicReference<>();
        processor.registerService(
                (symbol, callback) -> subscriber.set(callback),
                MarketDataPublisher.class
        );

        //publish some data
        subscriber.get().marketUpdate("AAA", 23.24);
        subscriber.get().marketUpdate("AAA", 19.99);
        subscriber.get().marketUpdate("AAA", 15.67);
    }

    public interface MarketDataPublisher {
        void subscribe(String symbol, MarketDataSubscriber callback);
    }

    public interface MarketDataSubscriber {
        boolean marketUpdate(String symbol, double mid);
    }

    public static class MarketDataSubscriberNode
            implements
            @ExportService MarketDataSubscriber, //callback interface exported by processor
            EventProcessorContextListener //gives access to the parent processor context
    {

        private EventProcessorContext eventProcessorContext;

        @Override
        public void currentContext(EventProcessorContext eventProcessorContext) {
            this.eventProcessorContext = eventProcessorContext;
        }

        //Annotated callback method called at runtime when a matching service is registered
        //with the parent event processor
        @ServiceRegistered
        public void marketDataPublisher(MarketDataPublisher marketDataPublisher) {
            //Subscribes the parent event processor with the exported MarketDataSubscriber interface
            marketDataPublisher.subscribe("AAA", eventProcessorContext.getExportedService());
        }

        @Override
        public boolean marketUpdate(String symbol, double mid) {
            System.out.println("marketUpdate: " + symbol + " " + mid);
            return false;
        }
    }
}
