package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.AfterEvent;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.OnEventHandler;

public class PostEventCallback {

    public static class MyNode {

        @Initialise
        public void init(){
            System.out.println("MyNode::init");
        }

        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("MyNode::handleStringEvent received:" + stringToProcess);
            return true;
        }

        @AfterEvent
        public void afterEvent(){
            System.out.println("MyNode::afterEvent");
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new MyNode());
        processor.init();
        System.out.println();
        processor.onEvent("TEST");
        System.out.println();
        processor.onEvent(23);
    }
}
