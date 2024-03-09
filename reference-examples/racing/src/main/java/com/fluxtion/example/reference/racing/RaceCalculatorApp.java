package com.fluxtion.example.reference.racing;


import com.fluxtion.example.reference.racing.generated.RaceCalculatorProcessor;

import static com.fluxtion.example.reference.racing.RaceCalculator.*;

public class RaceCalculatorApp {
    public static void main(String[] args) {
        RaceCalculatorProcessor raceCalculator = new RaceCalculatorProcessor();
        raceCalculator.init();

        ResultsPublisher resultsPublisher = raceCalculator.getExportedService();

        //connect to event stream and process runner timing events
        raceCalculator.onEvent(new RunnerStarted(1, "2019-02-14T09:00:00Z"));
        raceCalculator.onEvent(new RunnerStarted(2, "2019-02-14T09:02:10Z"));
        raceCalculator.onEvent(new RunnerStarted(3, "2019-02-14T09:06:22Z"));

        raceCalculator.onEvent(new RunnerFinished(2, "2019-02-14T10:32:15Z"));
        raceCalculator.onEvent(new RunnerFinished(3, "2019-02-14T10:59:10Z"));
        raceCalculator.onEvent(new RunnerFinished(1, "2019-02-14T11:14:32Z"));

        //publish full results
        resultsPublisher.publishAllResults();
    }
}
