package com.fluxtion.example.generation.gradle;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;
import com.fluxtion.compiler.builder.dataflow.DataFlow;

public class SampleAotBuilder implements FluxtionGraphBuilder {
    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {
        DataFlow.subscribe(String.class)
                .console("received: {}");
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig fluxtionCompilerConfig) {
        fluxtionCompilerConfig.setClassName("SampleAot");
        fluxtionCompilerConfig.setPackageName("com.fluxtion.example.generation.gradle.generated");
    }
}
