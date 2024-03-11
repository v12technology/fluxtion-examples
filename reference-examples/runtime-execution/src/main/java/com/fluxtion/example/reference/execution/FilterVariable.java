package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.FilterId;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnParentUpdate;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.event.Signal;

public class FilterVariable {

    public static class MyNode {
        private final String name;

        public MyNode(String name) {
            this.name = name;
        }


        @OnEventHandler(filterVariable = "name")
        public boolean handleIntSignal(Signal.IntSignal intSignal) {
            System.out.printf("MyNode-%s::handleIntSignal - %s%n", name, intSignal.getValue());
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new MyNode("A"), new MyNode("B"));
        processor.init();

        processor.publishIntSignal("A", 22);
        processor.publishIntSignal("B", 45);
        processor.publishIntSignal("C", 100);
    }
}
