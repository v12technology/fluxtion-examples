package com.fluxtion.example.reference.integration;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;

public class FactoryExample {
    public static void main(String[] args) {
        var processor = Fluxtion.compile(new MyPartitionedLogic());

        var processor_A = processor.newInstance();
        var processor_B = processor.newInstance();

        //Factory method to create new event processor instances
        processor_A.init();
        processor_B.init();

        //user partitioning event flow logic
        processor_A.onEvent("for A");
        processor_B.onEvent("for B");
    }

    public static class MyPartitionedLogic {
        @OnEventHandler
        public boolean onString(String signal) {
            System.out.println(signal);
            return true;
        }
    }
}