package com.fluxtion.example.reference.binding;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;

public class ImperativeAdd {

    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("received:" + stringToProcess);
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(cfg -> cfg.addNode(new MyNode()));
        processor.init();
        processor.onEvent("TEST");
    }
}
