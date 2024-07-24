package com.fluxtion.example.reference.serviceimport;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.runtime.ServiceRegistered;
import com.fluxtion.runtime.event.Signal;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Imports a TimeService into the event processor, any node method annotated with {@code @ServiceRegistered} will receive
 * the service if the types match.
 *
 * running the example:
 *
 * <pre>
 *
 *  started at 100
 *  stopped at 500 elapsed:400
 * </pre>
 */
public class ImportService {

    public static void main(String[] args) {
        EventProcessor processor = Fluxtion.interpret(new StopWatchNode());
        processor.init();

        //register a time service, specifying the service interface TimeService
        AtomicLong syntheticTime = new AtomicLong();
        processor.registerService(syntheticTime::get, TimeService.class);

        //set time - 100
        syntheticTime.set(100);

        //start the stop watch
        processor.publishSignal("start");

        //set time - 500
        syntheticTime.set(500);

        //stop the stop watch
        processor.publishSignal("stop");
    }

    @FunctionalInterface
    public interface TimeService{
        long getTime();
    }

    //Stopwatch that uses the TimeService supplied at runtime
    public static class StopWatchNode{

        private TimeService timeService;
        private long startTime;

        //Annotate a method to receive a callback at runtime when a matching service is registered
        //with the parent event processor
        @ServiceRegistered
        public void setTimeService(TimeService timeService) {
            this.timeService = timeService;
        }

        @OnEventHandler(filterString = "start")
        public boolean startTiming(Signal start) {
            startTime = timeService.getTime();
            System.out.println("started at " + startTime);
            return false;
        }

        @OnEventHandler(filterString = "stop")
        public boolean stopTiming(Signal stop) {
            long stopTime = timeService.getTime();
            System.out.println("stopped at " + stopTime + " elapsed:" + (stopTime - startTime));
            return false;
        }
    }
}
