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
    private String results;
    private long duration;

    @OnTrigger
    public boolean collectResults() {
        log.debug("collectingResults");
        long startTIme = requestHandler.getStartTime();
        long endTime = System.currentTimeMillis();
        duration = endTime - startTIme;
        int timeStepSize = 10;
        results = taskList.stream()
                .map(t -> {
                    String dur = "*".repeat((int) (Math.max(1, t.getTotalTime() / timeStepSize)));
                    String delay = " ".repeat((int) ((t.getStartTime() - startTIme) / timeStepSize));
                    return "%-7s %12s | %s %s".formatted(
                            t.getName(),
                            t.getExecutingThreadName().replace("ForkJoinPool.commonPool-worker", "FJ-worker"),
                            delay,
                            dur);
                })
                .collect(Collectors.joining("\n"));
        results += "\n" + ("-".repeat(100)) + "\n";
        results += """
                Time milliesconds      0   50   100  150  200  250  300  350  400  450  500  550  600  650  700 
                """;
        return true;
    }
}
