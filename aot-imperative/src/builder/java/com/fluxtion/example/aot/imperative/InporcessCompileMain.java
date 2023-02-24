package com.fluxtion.example.aot.imperative;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.stream.EventFlow;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.stream.helpers.Mappers;

public class InporcessCompileMain {
    public static void main(String[] args) {
        System.out.println("Running generation");
        EventProcessor eventProcessor = Fluxtion.compile(InporcessCompileMain::sampleProcessor);
        eventProcessor.init();
        eventProcessor.onEvent("30");
    }

    private static void sampleProcessor(EventProcessorConfig c) {
        EventFlow.subscribe(String.class)
                .mapToInt(Integer::decode)
                .map(Mappers.cumSumInt())
                .console("received -> {}");
    }

}
