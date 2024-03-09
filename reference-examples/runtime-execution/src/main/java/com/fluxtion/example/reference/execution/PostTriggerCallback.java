package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.AfterEvent;
import com.fluxtion.runtime.annotations.AfterTrigger;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.OnEventHandler;

public class PostTriggerCallback {

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

        @AfterTrigger
        public void afterTrigger(){
            System.out.println("MyNode::afterTrigger");
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
