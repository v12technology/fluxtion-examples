package com.fluxtion.example.reference.libraryfunction;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.callback.EventDispatcher;

public class CallBackExample {
    public static class MyCallbackNode{

        @Inject
        public EventDispatcher eventDispatcher;

        @OnEventHandler
        public boolean processString(String event) {
            for (String item : event.split(",")) {
                eventDispatcher.processAsNewEventCycle(Integer.parseInt(item));
            }
            return false;
        }

        @OnEventHandler
        public boolean processInteger(Integer event) {
            System.out.println("received event: " + event);
            return false;
        }

    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new MyCallbackNode());
        processor.init();

        processor.onEvent("20,45,89");
    }
}
