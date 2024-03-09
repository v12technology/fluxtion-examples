package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;

public class SimpleConditionalTriggerChild {

    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("received:" + stringToProcess);
            return true;
        }
    }

    public static class MyNode2 {
        @OnEventHandler
        public boolean handleStringEvent(int intToProcess) {
            boolean propagate = intToProcess > 100;
            System.out.println("conditional propagate:" + propagate);
            return propagate;
        }
    }

    public static class Child{
        private final MyNode myNode;
        private final MyNode2 myNode2;

        public Child(MyNode myNode, MyNode2 myNode2) {
            this.myNode = myNode;
            this.myNode2 = myNode2;
        }

        @OnTrigger
        public boolean triggered(){
            System.out.println("Child:triggered");
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new Child(new MyNode(), new MyNode2()));
        processor.init();
        processor.onEvent("test");
        System.out.println();
        processor.onEvent(200);
        System.out.println();
        processor.onEvent(50);
    }
}
