package com.fluxtion.example.imperative.helloworld;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;

/**
 * This class is discovered by the maven plugin to generate an {@link com.fluxtion.runtime.EventProcessor} ahead of time
 * during the compilation phase. The output is the {@link com.fluxtion.example.imperative.helloworld.generated.BreachNotifierProcessor} source
 * file.
 * <p></p>
 * Nodes are added to the graph using the {{@link #buildGraph(EventProcessorConfig)}}
 * method.
 */
public class AotBuilder implements FluxtionGraphBuilder {

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
