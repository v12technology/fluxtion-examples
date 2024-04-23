package com.fluxtion.example.reference.binding;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;

public class SharedEqualityReference {

    public static class MyNode {
        private final String name;
        int identifier;

        public MyNode(String name, int identifier) {
            this.name = name;
            this.identifier = identifier;
        }

        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println(name + " identifier:" + identifier + " received:" + stringToProcess);
            return true;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyNode myNode = (MyNode) o;
            return name.equals(myNode.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public String toString() {
            return "MyNode{" +
                    "name='" + name + '\'' +
                    ", identifier=" + identifier +
                    '}';
        }
    }

    public static class Root1 {
        private final String name;
        private final MyNode myNode;

        public Root1(String name, MyNode myNode) {
            this.name = name;
            this.myNode = myNode;
            System.out.println(name + "::new " + myNode);
        }

        @OnTrigger
        public boolean trigger() {
            System.out.println(name + "::triggered " + myNode);
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(cfg -> {
            MyNode myNode1 = new MyNode("myNode_1", 999);
            MyNode myNode2 = new MyNode("myNode_1", 444);
            cfg.addNode(new Root1("root_1", myNode1));
            cfg.addNode(new Root1("root_2", myNode2));
        });
        processor.init();
        System.out.println();
        processor.onEvent("TEST");
    }
}
