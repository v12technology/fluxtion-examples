package com.fluxtion.example.reference.integration;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.event.Signal;

import java.util.Date;

public class SignalInput {

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new SignalInput());
        processor.init();

        processor.publishIntSignal("1", 200);
        processor.publishSignal("ALERT_SIGNAL", "alert!!");
        processor.publishSignal("WAKEUP");
        processor.publishObjectSignal("WAKEUP");
        processor.publishObjectSignal(new Date());
    }

    @OnEventHandler(filterString = "1")
    public boolean intSignal(Signal.IntSignal value) {
        System.out.println("intSignal [" + value.getValue() + "]");
        return true;
    }

    @OnEventHandler(filterString = "ALERT_SIGNAL")
    public boolean alertSignal(Signal<String> signalToProcess) {
        System.out.println("alertStringSignal [" + signalToProcess + "]");
        return true;
    }

    @OnEventHandler(filterStringFromClass = String.class)
    public boolean anyStringSignal(Signal<String> signalToProcess) {
        System.out.println("anyStringSignal [" + signalToProcess + "]");
        return true;
    }

    @OnEventHandler(filterStringFromClass = Date.class)
    public boolean anyDateSignal(Signal<Date> signalToProcess) {
        System.out.println("anyDateSignal [" + signalToProcess + "]");
        return true;
    }

    @OnEventHandler(filterString = "WAKEUP")
    public boolean namedSignal(Signal<?> signalToProcess) {
        System.out.println("namedSignal [" + signalToProcess.filterString() + "]");
        return true;
    }
}
