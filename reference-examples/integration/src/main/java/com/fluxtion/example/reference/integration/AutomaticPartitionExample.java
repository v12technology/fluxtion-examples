package com.fluxtion.example.reference.integration;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.partition.Partitioner;

public class AutomaticPartitionExample {

    public record DayEvent(String day, int amount) {
    }

    public static void main(String[] args) {
        var processorFactory = Fluxtion.compile(new DayEventProcessor());

        //Create a partitioner that will partition data based on a property
        Partitioner<StaticEventProcessor> partitioner = new Partitioner<>(processorFactory::newInstance);
        partitioner.partition(DayEvent::day);

        //new processor for Monday
        partitioner.onEvent(new DayEvent("Monday", 2));
        partitioner.onEvent(new DayEvent("Monday", 4));

        //new processor for Tuesday
        partitioner.onEvent(new DayEvent("Tuesday", 14));

        //new processor for Friday
        partitioner.onEvent(new DayEvent("Friday", 33));

        //re-use processor for Monday
        System.out.println();
        partitioner.onEvent(new DayEvent("Monday", 999));
    }

    public static class DayEventProcessor {

        @Initialise
        public void initialise() {
            System.out.println("\nDayEventProcessor::initialise");
        }

        @OnEventHandler
        public boolean onDayaEvent(DayEvent signal) {
            System.out.println(signal);
            return true;
        }
    }
}
