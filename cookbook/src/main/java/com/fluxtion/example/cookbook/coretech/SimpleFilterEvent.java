package com.fluxtion.example.cookbook.coretech;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.event.Signal;

public class SimpleFilterEvent {

    public static class MyNode {
        @OnEventHandler(filterString = "CLEAR_SIGNAL")
        public boolean allClear(Signal<String> signalToProcess) {
            System.out.println("allClear [" + signalToProcess + "]");
            return true;
        }

        @OnEventHandler(filterString = "ALERT_SIGNAL")
        public boolean alertSignal(Signal<String> signalToProcess) {
            System.out.println("alertSignal [" + signalToProcess + "]");
            return true;
        }

        @OnEventHandler()
        public boolean anySignal(Signal<String> signalToProcess) {
            System.out.println("anySignal [" + signalToProcess + "]");
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new MyNode());
        processor.init();
        processor.onEvent(new Signal<>("ALERT_SIGNAL", "power failure"));
        System.out.println();
        processor.onEvent(new Signal<>("CLEAR_SIGNAL", "power restored"));
        System.out.println();
        processor.onEvent(new Signal<>("HEARTBEAT_SIGNAL", "heartbeat message"));
    }
}
