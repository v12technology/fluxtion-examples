package com.fluxtion.example.cookbook.ml.linearregression;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;

public class AotOpportunityFluxtionBuilder implements FluxtionGraphBuilder {
    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {
        Main.buildLogic(eventProcessorConfig);
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig fluxtionCompilerConfig) {
        fluxtionCompilerConfig.setClassName("OpportunityMlProcessor");
        fluxtionCompilerConfig.setPackageName("com.fluxtion.example.cookbook.ml.linearregression.generated");
    }
}