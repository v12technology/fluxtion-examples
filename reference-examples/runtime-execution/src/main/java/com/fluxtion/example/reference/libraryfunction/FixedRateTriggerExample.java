package com.fluxtion.example.reference.libraryfunction;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.time.FixedRateTrigger;

public class FixedRateTriggerExample {
    public static class RegularTrigger {

        private final FixedRateTrigger fixedRateTrigger;

        public RegularTrigger(FixedRateTrigger fixedRateTrigger) {
            this.fixedRateTrigger = fixedRateTrigger;
        }

        public RegularTrigger(int sleepMilliseconds) {
            fixedRateTrigger = new FixedRateTrigger(sleepMilliseconds);
        }

        @OnTrigger
        public boolean trigger() {
            System.out.println("RegularTrigger::triggered");
            return true;
        }
    }

    public static void main(String... args) throws InterruptedException {
        var processor = Fluxtion.interpret(new RegularTrigger(100));
        processor.init();

        //NO TRIGGER - 10MS NEEDS TO ELAPSE
        processor.onEvent(new Object());
        processor.onEvent("xxx");

        //WILL TRIGGER - 10MS HAS ELAPSED
        Thread.sleep(100);
        processor.onEvent("xxx");
    }
}
