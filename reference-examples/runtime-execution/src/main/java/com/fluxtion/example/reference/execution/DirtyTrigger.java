package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;

public class DirtyTrigger {

    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(int intToProcess) {
            boolean propagate = intToProcess > 100;
            System.out.println("conditional propagate:" + propagate);
            return propagate;
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

    public static class NonDirtyChild {
        private final MyNode myNode;

        public NonDirtyChild(MyNode myNode) {
            this.myNode = myNode;
        }

        @OnTrigger(dirty = false)
        public boolean triggered() {
            System.out.println("NonDirtyChild:triggered");
            return true;
        }
    }

    public static void main(String[] args) {
        MyNode myNode = new MyNode();
        var processor = Fluxtion.interpret(new Child(myNode), new NonDirtyChild(myNode));
        processor.init();
        processor.onEvent("test");
        System.out.println();
        processor.onEvent(200);
        System.out.println();
        processor.onEvent(50);
    }
}
