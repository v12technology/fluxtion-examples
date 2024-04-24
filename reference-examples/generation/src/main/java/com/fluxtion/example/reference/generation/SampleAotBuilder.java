package com.fluxtion.example.reference.generation;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;
import com.fluxtion.compiler.builder.dataflow.DataFlow;

import java.nio.file.Path;

//Uncomment to disable discovery for this FluxtionGraphBuilder
//@com.fluxtion.runtime.annotations.builder.Disabled
public class SampleAotBuilder implements FluxtionGraphBuilder {
    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {
        DataFlow.subscribe(String.class)
                .mapToInt(String::length);
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig compilerConfig) {
        compilerConfig.setClassName("SampleAotBuilderProcessor");
        compilerConfig.setPackageName("com.fluxtion.example.reference.generation.genoutput");
    }

    public static void main(String[] args) {
        Fluxtion.scanAndCompileFluxtionBuilders(Path.of("target/classes/").toFile());
    }
}
