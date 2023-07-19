package com.fluxtion.example.imperative.helloworld;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;


/**
 * creates a processing graph imperatively, extracts double values from events, calculates the sum and prints a
 * message to console if the sum is greater than a 100.
 * <p>
 * Uses @{@link OnEventHandler} annotation to declare the entry point of an execution path
 * {@link OnTrigger} annotated methods indicate call back methods to be invoked if a parent propagates a change.
 * The return flag from the {@link OnTrigger} method indicates if the event should be propagated. In this case
 * the event is only propagated if the sum > 100.
 */
public class Main {
    public static void main(String[] args) {
        var eventProcessor = Fluxtion.interpret(new BreachNotifier());
        eventProcessor.init();
        eventProcessor.onEvent(new InputDataEvent_1(34.4));
        eventProcessor.onEvent(new InputDataEvent_2(52.1));
        eventProcessor.onEvent(new InputDataEvent_1(105));//should create a breach warning
        eventProcessor.onEvent(new InputDataEvent_1(12.4));
    }

}