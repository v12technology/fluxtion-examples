package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.annotations.PushReference;
import com.fluxtion.runtime.event.Signal;

public class PushTrigger {

    public static class MyNode {
        @PushReference
        private final PushTarget pushTarget;

        public MyNode(PushTarget pushTarget) {
            this.pushTarget = pushTarget;
        }

        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("MyNode::handleStringEvent " + stringToProcess);
            if (stringToProcess.startsWith("PUSH")) {
                pushTarget.myValue = stringToProcess;
                return true;
            }
            return false;
        }
    }

    public static class PushTarget {
        public String myValue;

        @OnTrigger
        public boolean onTrigger() {
            System.out.println("PushTarget::onTrigger ->  myValue:'" + myValue + "'");
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new MyNode(new PushTarget()));
        processor.init();
        processor.onEvent("PUSH - test 1");
        System.out.println();
        processor.onEvent("ignore me - XXXXX");
        System.out.println();
        processor.onEvent("PUSH - test 2");
    }
}
