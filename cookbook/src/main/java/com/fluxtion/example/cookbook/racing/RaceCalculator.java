package com.fluxtion.example.cookbook.racing;

import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnEventHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class RaceCalculator {

    public record RunnerStarted(long runnerId, Instant startTime) {
        RunnerStarted(long runnerId, String time) {
            this(runnerId, Instant.parse(time));
        }
    }

    public record RunnerFinished(long runnerId, Instant finishTime) {
        RunnerFinished(long runnerId, String time) {
            this(runnerId, Instant.parse(time));
        }
    }

    public interface ResultsPublisher {
        void publishAllResults();
    }

    private record RunningRecord(Instant startTime, Instant finishTime) {
        RunningRecord(Instant startTime) {
            this(startTime, startTime);
        }

        public String runDuration() {
            Duration duration = Duration.between(startTime, finishTime);
            return duration.toHoursPart() + ":" + duration.toMinutesPart() + ":" + duration.toSecondsPart();
        }
    }

    @Getter
    public static class RaceTimeTracker {

        private final transient Map<Long, RunningRecord> raceTimeMap = new HashMap<>();

        @OnEventHandler(propagate = false)
        public boolean runnerStarted(RunnerStarted runnerStarted) {
            raceTimeMap.put(runnerStarted.runnerId(), new RunningRecord(runnerStarted.startTime()));
            return false;
        }

        @OnEventHandler
        public boolean runnerFinished(RunnerFinished runner) {
            raceTimeMap.computeIfPresent(
                    runner.runnerId(),
                    (id, startRecord) -> new RunningRecord(startRecord.startTime(), runner.finishTime()));
            return true;
        }
    }

    @RequiredArgsConstructor
    public static class ResultsPublisherImpl implements @ExportService ResultsPublisher {

        private final RaceTimeTracker raceTimeTracker;

        @OnEventHandler(propagate = false)
        public boolean runnerFinished(RunnerFinished runner) {
            var raceTime = raceTimeTracker.getRaceTimeMap().get(runner.runnerId());
            System.out.format("Crossed the line runner:%d time:%s%n", runner.runnerId(), raceTime.runDuration());
            return false;
        }

        @Override
        public void publishAllResults() {
            System.out.println("FINAL RESULTS");
            raceTimeTracker.getRaceTimeMap().forEach((l, r) ->
                    System.out.println("id:" + l + " final time:" + r.runDuration()));
        }
    }
}
