package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;

public class SimpleMultipleEventTypes {

    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("String received:" + stringToProcess);
            return true;
        }

        @OnEventHandler
        public boolean handleIntEvent(int intToProcess) {
            System.out.println("Int received:" + intToProcess);
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new MyNode());
        processor.init();
        processor.onEvent("TEST");
        processor.onEvent(16);
    }
}
