package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnParentUpdate;
import com.fluxtion.runtime.annotations.OnTrigger;

public class IdentifyParentByName {

    public static class MyNode {
        private final String name;

        public MyNode(String name) {
            this.name = name;
        }

        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println(name + " event received:" + stringToProcess);
            return stringToProcess.equals("*") | stringToProcess.equals(name);
        }
    }

    public static class Child{
        private final MyNode myNode_a;
        private final MyNode myNode_b;

        public Child(MyNode myNode_a, MyNode myNode_b) {
            this.myNode_a = myNode_a;
            this.myNode_b = myNode_b;
        }

        @OnParentUpdate(value = "myNode_a")
        public void node_a_Updated(MyNode myNode_a){
            System.out.println("Parent A updated");
        }

        @OnParentUpdate("myNode_b")
        public void node_b_Updated(MyNode myNode_b){
            System.out.println("Parent B updated");
        }

        @OnTrigger
        public boolean triggered(){
            System.out.println("Child:triggered");
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new Child(new MyNode("A"), new MyNode("B")));
        processor.init();
        processor.onEvent("test");
        System.out.println();
        processor.onEvent("*");
        System.out.println();
        processor.onEvent("A");
        System.out.println();
        processor.onEvent("B");
    }
}
