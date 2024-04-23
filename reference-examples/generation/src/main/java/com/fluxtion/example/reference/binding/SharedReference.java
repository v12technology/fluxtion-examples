package com.fluxtion.example.reference.binding;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;

public class SharedReference {

    public static class MyNode {
        private final String name;

        public MyNode(String name) {
            this.name = name;
        }

        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println(name + "::received:" + stringToProcess);
            return true;
        }
    }

    public static class Root1 {
        private final String name;
        private final MyNode myNode;

        public Root1(String name, MyNode myNode) {
            this.name = name;
            this.myNode = myNode;
        }

        @OnTrigger
        public boolean trigger() {
            System.out.println(name + "::triggered");
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(cfg -> {
            MyNode myNode1 = new MyNode("myNode_1");
            cfg.addNode(new Root1("root_1", myNode1));
            cfg.addNode(new Root1("root_2", myNode1));
        });
        processor.init();
        processor.onEvent("TEST");
    }
}
