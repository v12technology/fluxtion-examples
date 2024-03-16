package com.fluxtion.example.reference;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.annotations.builder.Inject;

public class InjectNoFactory {
    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("MyNode::received:" + stringToProcess);
            return true;
        }
    }

    public static class Root1 {
        @Inject
        private final MyNode myNode;

        public Root1() {
            myNode = null;
        }

        public Root1(MyNode myNode) {
            this.myNode = myNode;
        }

        @OnTrigger
        public boolean trigger() {
            System.out.println("Root1::triggered");
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(cfg -> cfg.addNode(new Root1()));
        processor.init();
        processor.onEvent("TEST");
    }
}
