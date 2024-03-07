package com.fluxtion.example.cookbook.coretech;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnParentUpdate;
import com.fluxtion.runtime.annotations.OnTrigger;

public class SimpleIdentifyParent {

    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("MyNode event received:" + stringToProcess);
            return true;
        }
    }

    public static class MyNode2 {
        @OnEventHandler
        public boolean handleIntEvent(int intToProcess) {
            boolean propagate = intToProcess > 100;
            System.out.println("MyNode2 conditional propagate:" + propagate);
            return propagate;
        }

        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("MyNode2 event received:" + stringToProcess);
            return true;
        }
    }

    public static class Child{
        private final MyNode myNode;
        private final MyNode2 myNode2;

        public Child(MyNode myNode, MyNode2 myNode2) {
            this.myNode = myNode;
            this.myNode2 = myNode2;
        }

        @OnParentUpdate
        public void node1Updated(MyNode myNode1){
            System.out.println("1 - myNode updated");
        }

        @OnParentUpdate
        public void node2Updated(MyNode2 myNode2){
            System.out.println("2 - myNode2 updated");
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
