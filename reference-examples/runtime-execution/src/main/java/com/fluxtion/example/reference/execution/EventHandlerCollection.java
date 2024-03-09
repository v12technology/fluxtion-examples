package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.FilterId;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnParentUpdate;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.event.Signal;

import static com.fluxtion.runtime.event.Signal.*;

public class EventHandlerCollection {

    public static class MyNode {

        @FilterId
        private final String filter;
        private final String name;

        public MyNode(String filter, String name) {
            this.filter = filter;
            this.name = name;
        }

        @OnEventHandler()
        public boolean handleIntSignal(IntSignal intSignal) {
            System.out.printf("MyNode-%s::handleIntSignal - %s%n", filter, intSignal.getValue());
            return true;
        }
    }

    public static class Child {
        private final MyNode[] nodes;
        private int updateCount;

        public Child(MyNode... nodes) {
            this.nodes = nodes;
        }

        @OnParentUpdate
        public void parentUpdated(MyNode updatedNode) {
            updateCount++;
            System.out.printf("parentUpdated '%s'%n", updatedNode.name);
        }

        @OnTrigger
        public boolean triggered() {
            System.out.printf("Child::triggered updateCount:%d%n%n", updateCount);
            updateCount = 0;
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new Child(
                new MyNode("A", "a_1"),
                new MyNode("A", "a_2"),
                new MyNode("B", "b_1")));
        processor.init();
        processor.publishIntSignal("A", 10);
        processor.publishIntSignal("B", 25);
        processor.publishIntSignal("A", 12);
        processor.publishIntSignal("C", 200);
    }
}
