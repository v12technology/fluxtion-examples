package com.fluxtion.example.cookbook.lottery.auditor;

import com.fluxtion.example.cookbook.lottery.api.LotterySystemMonitor;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.callback.ExportFunctionAuditEvent;
import com.fluxtion.runtime.event.Event;
import lombok.Data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SystemStatisticsAuditor implements Auditor, @ExportService LotterySystemMonitor {
    private transient final Map<Object, Stats> nodeStats = new IdentityHashMap<>();
    private transient final Map<String, Stats> eventStats = new HashMap<>();
    private transient final Map<String, Stats> methodStats = new HashMap<>();

    @Override
    public void nodeRegistered(Object o, String s) {
        nodeStats.put(o, new Stats(s));
    }

    @Override
    public void eventReceived(Object event) {
        updateEventStats(event);
    }

    @Override
    public void eventReceived(Event event) {
        updateEventStats(event);
    }

    @Override
    public void nodeInvoked(Object node, String nodeName, String methodName, Object event) {
        nodeStats.computeIfPresent(node, (n, s) -> s.incrementCallCount());
        String name = nodeName + "#" + methodName;
        methodStats.compute(name, (n, s) ->{
            s = s == null ? new Stats(n) : s;
            s.incrementCallCount();
            return s;
        });
    }

    @Override
    public void tearDown() {
        publishStats();
    }

    @Override
    public boolean auditInvocations() {
        return true;
    }

    @Override
    public void publishStats() {
        System.out.println("""
                
                -------------------------------------------------------------------------------------------
                NODE STATS START
                -------------------------------------------------------------------------------------------""");
        System.out.println(
                nodeStats.values().stream()
                        .sorted(Comparator.comparing(Stats::getCount))
                        .map(Stats::report)
                        .collect(Collectors.joining("\n\t", "Node stats:\n\t", ""))
        );
        System.out.println(
                eventStats.values().stream()
                        .sorted(Comparator.comparing(Stats::getCount))
                        .map(Stats::eventReport)
                        .collect(Collectors.joining("\n\t", "Event stats:\n\t", ""))
        );
        System.out.println(
                methodStats.values().stream()
                        .sorted(Comparator.comparing(Stats::getCount))
                        .map(Stats::methodReport)
                        .collect(Collectors.joining("\n\t", "Node method stats:\n\t", ""))
        );
        System.out.println("""
                -------------------------------------------------------------------------------------------
                NODE STATS END
                -------------------------------------------------------------------------------------------
                """);
    }

    private void updateEventStats(Object event){
        String name = event.getClass().getSimpleName();
        if(event instanceof ExportFunctionAuditEvent func){
            name = func.toString();
        }
        eventStats.compute(name, (c, s) ->{
            s = s == null ? new Stats(c) : s;
            s.incrementCallCount();
            return s;
        });
    }

    @Data
    private static final class Stats {
        private final String name;
        private int count;

        public Stats incrementCallCount() {
            count++;
            return this;
        }


        public String report() {
            return "node:" + name + ", invokeCount:" + count;
        }

        public String eventReport() {
            return "event:" + name + ", invokeCount:" + count;
        }

        public String methodReport() {
            return "method:" + name + ", invokeCount:" + count;
        }
    }
}
