package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.Start;
import com.fluxtion.runtime.annotations.Stop;
import com.fluxtion.runtime.annotations.TearDown;

public class SimpleLifecycle {

    public static class MyNode {

        @Initialise
        public void myInitMethod() {
            System.out.println("Initialise");
        }

        @Start
        public void myStartMethod() {
            System.out.println("Start");
        }

        @Stop
        public void myStopMethod() {
            System.out.println("Stop");
        }

        @TearDown
        public void myTearDownMethod() {
            System.out.println("TearDown");
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new MyNode());
        processor.init();
        processor.start();
        processor.stop();
        processor.tearDown();
    }
}
