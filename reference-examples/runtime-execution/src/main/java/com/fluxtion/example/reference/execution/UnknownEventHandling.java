package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;

import java.util.Collections;

public class UnknownEventHandling {

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new MyNode());
        processor.init();
        //set an unknown event handler
        processor.setUnKnownEventHandler(e -> System.out.println("Unregistered event type -> " + e.getClass().getName()));
        processor.onEvent("TEST");
        //handled by unKnownEventHandler
        processor.onEvent(Collections.emptyList());
    }

    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("received:" + stringToProcess);
            return true;
        }
    }
}
