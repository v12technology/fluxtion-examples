package com.fluxtion.example.reference.racing;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;

import static com.fluxtion.example.reference.racing.RaceCalculator.*;

public class RaceCalculatorAotBuilder implements FluxtionGraphBuilder {
    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {
        RaceTimeTracker raceCalculator = eventProcessorConfig.addNode(new RaceTimeTracker(), "raceCalculator");
        eventProcessorConfig.addNode(new ResultsPublisherImpl(raceCalculator), "resultsPublisher");
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig fluxtionCompilerConfig) {
        fluxtionCompilerConfig.setClassName("RaceCalculatorProcessor");
        fluxtionCompilerConfig.setPackageName("com.fluxtion.example.reference.racing.generated");
    }
}

