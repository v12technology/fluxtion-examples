package com.fluxtion.example.reference.integration;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.example.reference.integration.genoutput.VanillaProcessor;

public class VanillaCreateEventProcessor {
    public static void main(String[] args) {
        //IN PROCESS CREATION
        var processor = Fluxtion.interpret(new VanillaAotBuilder()::buildGraph);
        processor.init();
        processor.onEvent("hello world - in process");

        //AOT USE AS POJO
        processor = new VanillaProcessor();
        processor.init();
        processor.onEvent("hello world - AOT");
    }

    public static class VanillaAotBuilder implements FluxtionGraphBuilder{
        @Override
        public void buildGraph(EventProcessorConfig eventProcessorConfig) {
            DataFlow.subscribe(String.class)
                    .console("received -> {}");
        }

        @Override
        public void configureGeneration(FluxtionCompilerConfig compilerConfig) {
            compilerConfig.setClassName("VanillaProcessor");
            compilerConfig.setPackageName("com.fluxtion.example.reference.integration.genoutput");
        }
    }
}
