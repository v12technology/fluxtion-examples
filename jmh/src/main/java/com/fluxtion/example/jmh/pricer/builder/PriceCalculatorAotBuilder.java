package com.fluxtion.example.jmh.pricer.builder;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;
import com.fluxtion.example.jmh.pricer.node.PriceLadderPublisher;

/**
 * Build the PriceLadderProcessor AOT with the maven plugin
 */
public class PriceCalculatorAotBuilder implements FluxtionGraphBuilder {
    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {
        eventProcessorConfig.addNode(new PriceLadderPublisher());
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig fluxtionCompilerConfig) {
        fluxtionCompilerConfig.setClassName("PriceLadderProcessor");
        fluxtionCompilerConfig.setPackageName("com.fluxtion.example.jmh.pricer.generated");
    }
}
