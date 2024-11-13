package com.fluxtion.example.cookbook.pnl.joinexample;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;

public class PnlFromJoinAotBuilder implements FluxtionGraphBuilder {
    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {
        PnlExampleMain.buildPnlFromJoins(eventProcessorConfig);
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig fluxtionCompilerConfig) {
        fluxtionCompilerConfig.setPackageName("com.fluxtion.example.cookbook.pnl.joinexample.generated");
        fluxtionCompilerConfig.setClassName("PnlFromJoinCalculator");
    }
}
