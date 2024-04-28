package com.fluxtion.example.reference.libraryfunction;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.time.Clock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.LongAdder;

public class ClockExample {

    public static class TimeLogger {
        public Clock wallClock = Clock.DEFAULT_CLOCK;

        @OnEventHandler
        public boolean publishTime(DateFormat dateFormat) {
            System.out.println("time " + dateFormat.format(new Date(wallClock.getWallClockTime())));
            return true;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var processor = Fluxtion.interpret(new TimeLogger());
        processor.init();
        //PRINT CURRENT TIME
        processor.onEvent(new SimpleDateFormat("HH:mm:ss.SSS"));

        //SLEEP AND PRINT TIME
        Thread.sleep(100);
        processor.onEvent(new SimpleDateFormat("HH:mm:ss.SSS"));
    }
}
