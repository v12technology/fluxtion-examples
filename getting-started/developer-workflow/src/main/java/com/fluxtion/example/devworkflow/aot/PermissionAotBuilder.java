package com.fluxtion.example.devworkflow.aot;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;
import com.fluxtion.example.devworkflow.integrating.CommandAuthorizerNode;
import com.fluxtion.example.devworkflow.integrating.CommandExecutor;

public class PermissionAotBuilder implements FluxtionGraphBuilder {

    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {
        eventProcessorConfig.addNode(new CommandExecutor(new CommandAuthorizerNode()));
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig compilerConfig) {
        compilerConfig.setClassName("PermittedCommandProcessor");
        compilerConfig.setPackageName("com.fluxtion.example.devworkflow.aot.generated");
    }
}
