package com.fluxtion.example.reference.binding;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.annotations.builder.Inject;

public class InjectSingletonNoFactory {
    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("MyNode::received:" + stringToProcess);
            return true;
        }
    }

    public static class Root1{
        @Inject(singleton = true)
        private final MyNode myNode;
        private final String id;

        public Root1(String id) {
            this(null, id);
        }

        public Root1(MyNode myNode, String id) {
            this.myNode = myNode;
            this.id = id;
        }

        @OnTrigger
        public boolean trigger() {
            System.out.println(id + "::triggered");
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new Root1("r1"), new Root1("r2"), new Root1("r3"));
        processor.init();
        processor.onEvent("TEST");
    }
}
