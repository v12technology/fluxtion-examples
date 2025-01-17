package com.fluxtion.example.cookbook.tempmonitoring;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;

public class AotBuilderConfig implements FluxtionGraphBuilder {
    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {
        CustomisableMachineMonitoring.buildGraph(eventProcessorConfig);
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig compilerConfig) {
        compilerConfig.setClassName("MachineMonitor");
        compilerConfig.setPackageName("com.fluxtion.example.cookbook.tempmonitoring.generated");
    }
}
