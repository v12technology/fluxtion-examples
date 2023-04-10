package com.fluxtion.example.cookbook.parallel;

import com.fluxtion.runtime.annotations.OnTrigger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskCollector {

    @Singular("task")
    private List<SimulatedTask> taskList;
    private RequestHandler requestHandler;

    @OnTrigger
    public boolean collectResults() {
        long startTIme = requestHandler.getStartTime();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTIme;
        String results = taskList.stream()
                .map(t -> {
                    String dur = "*".repeat((int) (Math.max(1, t.getTotalTime()/25)));
                    String delay = "_".repeat((int) ((t.getStartTime() - startTIme)/25));
                    return "%s %35s | %s %s".formatted(
                            t.getName(), t.getExecutingThreadName(), delay, dur);
                })
                .collect(Collectors.joining("\n"));
        System.out.println(results);
        System.out.println("-".repeat(80));
        log.info("collecting results duration:" + duration);
        return true;
    }

    private static class ThreadTiming {
        String name;
        long startTime;
        long stopTime;
    }
}
