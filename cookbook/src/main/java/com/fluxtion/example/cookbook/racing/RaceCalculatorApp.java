package com.fluxtion.example.cookbook.racing;

import com.fluxtion.example.cookbook.racing.generated.RaceCalculatorProcessor;

import java.time.Instant;

import static com.fluxtion.example.cookbook.racing.RaceCalculator.*;

public class RaceCalculatorApp {
    public static void main(String[] args) {
        RaceCalculatorProcessor raceCalculatorProcessor = new RaceCalculatorProcessor();
        raceCalculatorProcessor.init();

        ResultsPublisher resultsPublisher = raceCalculatorProcessor.getExportedService();

        //connect to event stream and process runner timing events
        raceCalculatorProcessor.onEvent(new RunnerStarted(1, Instant.now()));
        raceCalculatorProcessor.onEvent(new RunnerStarted(2, Instant.now()));
        raceCalculatorProcessor.onEvent(new RunnerStarted(3, Instant.now()));

        raceCalculatorProcessor.onEvent(new RunnerFinished(2, Instant.now()));
        raceCalculatorProcessor.onEvent(new RunnerFinished(3, Instant.now()));
        raceCalculatorProcessor.onEvent(new RunnerFinished(1, Instant.now()));

        //publish full results
        resultsPublisher.publishAllResults();
    }
}
