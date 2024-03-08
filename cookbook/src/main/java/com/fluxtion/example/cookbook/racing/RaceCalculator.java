package com.fluxtion.example.cookbook.racing;

import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
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

    private record RunningRecord(long runnerId, Instant startTime, Instant finishTime) {
        RunningRecord(long runnerId, Instant startTime) {
            this(runnerId, startTime, startTime);
        }

        public String runDuration() {
            Duration duration = Duration.between(startTime, finishTime);
            return duration.toHoursPart() + ":" + duration.toMinutesPart() + ":" + duration.toSecondsPart();
        }
    }

    @Getter
    public static class RaceTimeTracker {

        private final transient Map<Long, RunningRecord> raceTimeMap = new HashMap<>();
        private RunningRecord latestFinisher;

        @OnEventHandler(propagate = false)
        public boolean runnerStarted(RunnerStarted runnerStarted) {
            long runnerId = runnerStarted.runnerId();
            raceTimeMap.put(runnerId, new RunningRecord(runnerId, runnerStarted.startTime()));
            return false;
        }

        @OnEventHandler
        public boolean runnerFinished(RunnerFinished runner) {
            latestFinisher = raceTimeMap.computeIfPresent(
                    runner.runnerId(),
                    (id, startRecord) -> new RunningRecord(id, startRecord.startTime(), runner.finishTime()));
            return true;
        }
    }

    @RequiredArgsConstructor
    public static class ResultsPublisherImpl implements @ExportService ResultsPublisher {

        private final RaceTimeTracker raceTimeTracker;

        @OnTrigger
        public boolean sendIndividualRunnerResult(){
            var raceRecord = raceTimeTracker.getLatestFinisher();
            System.out.format("Crossed the line runner:%d time [%s]%n", raceRecord.runnerId(), raceRecord.runDuration());
            return false;
        }

        @Override
        public void publishAllResults() {
            System.out.println("\nFINAL RESULTS");
            raceTimeTracker.getRaceTimeMap().forEach((l, r) ->
                    System.out.println("id:" + l + " time [" + r.runDuration() + "]"));
        }
    }
}
