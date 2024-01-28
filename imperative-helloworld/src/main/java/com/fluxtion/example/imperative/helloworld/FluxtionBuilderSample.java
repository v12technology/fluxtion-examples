package com.fluxtion.example.imperative.helloworld;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;

public class FluxtionBuilderSample implements FluxtionGraphBuilder {
    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {
        eventProcessorConfig.addNode(new BreachNotifier());
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig fluxtionCompilerConfig) {
        fluxtionCompilerConfig.setClassName("BreachNotifierProcessor");
        fluxtionCompilerConfig.setPackageName("com.fluxtion.example.imperative.helloworld.generated");
    }
}
