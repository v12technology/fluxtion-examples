package com.fluxtion.example.cookbook.pnl.flatmapexample;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;

public class PnlFromFlatMapAotBuilder implements FluxtionGraphBuilder {
    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {
        PnlExampleMain.buildPnlFlatMap(eventProcessorConfig);
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig fluxtionCompilerConfig) {
        fluxtionCompilerConfig.setPackageName("com.fluxtion.example.cookbook.pnl.flatmapexample.generated");
        fluxtionCompilerConfig.setClassName("PnlFromFlatMapCalculator");
    }
}
