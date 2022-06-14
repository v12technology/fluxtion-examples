package com.fluxtion.example.aot.imperative;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.RootNodeConfig;
import com.fluxtion.compiler.builder.stream.EventFlow;
import com.fluxtion.runtime.stream.helpers.Mappers;

import java.util.Collections;

public class Generator {
    public static void main(String[] args) {
        System.out.println("Running generation");
        Fluxtion.compileAot(Generator::sampleProcessor);
        Fluxtion.compileAot(
                new RootNodeConfig("myRoot", MyRootClass.class, Collections.emptyMap()),
                "com.fluxtion.example.aot.imperative.generator");
    }

    private static void sampleProcessor(EventProcessorConfig c) {
        EventFlow.subscribe(String.class)
                .mapToInt(Integer::decode)
                .map(Mappers.cumSumInt())
                .console("cumSum -> {}");
    }
}