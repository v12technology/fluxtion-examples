package com.fluxtion.example.reference.binding;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.annotations.builder.ExcludeNode;

public class ExcludeUserNode {

    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("MyNode::received:" + stringToProcess);
            return true;
        }
    }

    @ExcludeNode
    public static class Root1 {
        private final MyNode myNode;

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
        var processor = Fluxtion.interpret(new Root1(new MyNode()));
        processor.init();
        processor.onEvent("TEST");
    }
}
