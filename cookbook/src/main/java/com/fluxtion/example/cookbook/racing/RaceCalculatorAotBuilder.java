package com.fluxtion.example.cookbook.racing;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;
import com.fluxtion.example.cookbook.racing.RaceCalculator.RaceTimeTracker;
import com.fluxtion.example.cookbook.racing.RaceCalculator.ResultsPublisherImpl;

public class RaceCalculatorAotBuilder implements FluxtionGraphBuilder {
    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {
        RaceTimeTracker raceCalculator = eventProcessorConfig.addNode(new RaceTimeTracker(), "raceCalculator");
        eventProcessorConfig.addNode(new ResultsPublisherImpl(raceCalculator), "resultsPublisher");
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig fluxtionCompilerConfig) {
        fluxtionCompilerConfig.setClassName("RaceCalculatorProcessor");
        fluxtionCompilerConfig.setPackageName("com.fluxtion.example.cookbook.racing.generated");
    }
}

