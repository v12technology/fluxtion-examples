package com.fluxtion.example.reference.binding;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;

public class VarArgsAddMultiple {

    public static class MyNode {

        private final String name;

        public MyNode(String name) {
            this.name = name;
        }

        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println(name + " received:" + stringToProcess);
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(
                new MyNode("node_1"),
                new MyNode("node_2"),
                new MyNode("node_3"));
        processor.init();
        processor.onEvent("TEST");
    }
}
