package com.fluxtion.example.cookbook.racing;

import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnEventHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class RaceCalculator {

    public record RunnerStarted(long runnerId, Instant startTime) {
    }

    public record RunnerFinished(long runnerId, Instant finishTime) {
    }

    public interface ResultsPublisher {
        void publishAllResults();
    }

    @Getter
    public static class RaceTimeTracker {

        private final transient Map<Long, Long> runnerRaceTimeMap = new HashMap<>();

        @OnEventHandler(propagate = false)
        public boolean runnerStarted(RunnerStarted runnerStarted) {
            //add runner start time to map
            return false;
        }

        @OnEventHandler
        public boolean runnerFinished(RunnerFinished runnerFinished) {
            //calc runner total race time and add to map
            return true;
        }
    }

    @RequiredArgsConstructor
    public static class ResultsPublisherImpl implements @ExportService ResultsPublisher{

        private final RaceTimeTracker raceTimeTracker;

        @OnEventHandler(propagate = false)
        public boolean runnerFinished(RunnerFinished runnerFinished) {
            //get the runner race time and send individual their results
            long raceTime = raceTimeTracker.getRunnerRaceTimeMap().get(runnerFinished.runnerId());
            return false;
        }

        @Override
        public void publishAllResults() {
            //get all results and publish
            var runnerRaceTimeMap = raceTimeTracker.getRunnerRaceTimeMap();
        }
    }
}
