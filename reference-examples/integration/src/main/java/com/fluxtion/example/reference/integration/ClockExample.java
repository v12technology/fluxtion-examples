package com.fluxtion.example.reference.integration;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.time.Clock;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.atomic.LongAdder;

public class ClockExample {

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new TimeLogger());
        processor.init();
        //PRINT CURRENT TIME
        processor.onEvent(DateFormat.getDateTimeInstance());

        //USE A SYNTHETIC STRATEGY TO SET TIME FOR THE PROCESSOR CLOCK
        LongAdder syntheticTime = new LongAdder();
        processor.setClockStrategy(syntheticTime::longValue);

        //SET A NEW TIME - GOING BACK IN TIME!!
        syntheticTime.add(1_000_000_000);
        processor.onEvent(DateFormat.getDateTimeInstance());

        //SET A NEW TIME - BACK TO THE FUTURE
        syntheticTime.add(1_800_000_000_000L);
        processor.onEvent(DateFormat.getDateTimeInstance());
    }


    public static class TimeLogger {
        public Clock wallClock = Clock.DEFAULT_CLOCK;

        @OnEventHandler
        public boolean publishTime(DateFormat dateFormat) {
            System.out.println("time " + dateFormat.format(new Date(wallClock.getWallClockTime())));
            return true;
        }
    }
}
