package com.fluxtion.example.reference.libraryfunction;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.callback.EventDispatcher;
import lombok.Data;

public class CallBackExample {
    public static class MyCallbackNode {

        @Inject
        public EventDispatcher eventDispatcher;

        @OnEventHandler
        public boolean processString(String event) {
            System.out.println("MyCallbackNode::processString - " + event);
            for (String item : event.split(",")) {
                eventDispatcher.processAsNewEventCycle(Integer.parseInt(item));
            }
            return true;
        }

        @OnEventHandler
        public boolean processInteger(Integer event) {
            System.out.println("MyCallbackNode::processInteger - " + event);
            return false;
        }

    }

    @Data
    public static class IntegerHandler {

        private final MyCallbackNode myCallbackNode;

        @OnEventHandler
        public boolean processInteger(Integer event) {
            System.out.println("IntegerHandler::processInteger - " + event + "\n");
            return true;
        }

        @OnTrigger
        public boolean triggered() {
            System.out.println("IntegerHandler::triggered\n");
            return false;
        }

    }

    public static void main(String[] args) {
        MyCallbackNode myCallbackNode = new MyCallbackNode();
        IntegerHandler intHandler = new IntegerHandler(myCallbackNode);
        var processor = Fluxtion.interpret(intHandler);
        processor.init();

        processor.onEvent("20,45,89");
    }
}
