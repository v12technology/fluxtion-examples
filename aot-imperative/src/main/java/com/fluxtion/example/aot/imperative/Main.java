package com.fluxtion.example.aot.imperative;

import com.fluxtion.example.aot.imperative.generator.myroot.Processor;
import com.fluxtion.runtime.EventProcessor;

public class Main {

    public static void main(String[] args) {
        EventProcessor processor = new Processor();
        processor.init();
        processor.onEvent("15");
        processor.onEvent("15");
        processor.onEvent("sdsdsd df");
        processor.onEvent("1");
        processor.onEvent("69");
    }
}
