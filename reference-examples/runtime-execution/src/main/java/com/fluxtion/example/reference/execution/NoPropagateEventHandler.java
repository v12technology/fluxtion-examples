package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;

public class NoPropagateEventHandler {

    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("MyNode::handleStringEvent received:" + stringToProcess);
            return true;
        }

        @OnEventHandler(propagate = false)
        public boolean handleIntEvent(int intToProcess) {
            System.out.println("MyNode::handleIntEvent received:" + intToProcess);
            return true;
        }
    }


    public static class Child {
        private final MyNode myNode;

        public Child(MyNode myNode) {
            this.myNode = myNode;
        }

        @OnTrigger
        public boolean triggered() {
            System.out.println("Child:triggered");
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new Child(new MyNode()));
        processor.init();
        processor.onEvent("test");
        System.out.println();
        processor.onEvent(200);
    }
}
