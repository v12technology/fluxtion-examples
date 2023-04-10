package com.fluxtion.example.cookbook.parallel;

import com.fluxtion.runtime.annotations.OnTrigger;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class SimulatedTask {
    protected final String name;
    protected final int workDurationMillis;
    protected final RequestHandler requestHandler;
    protected long startTime;
    protected long completeTime;
    protected long totalTime;
    protected String executingThreadName;

    @SneakyThrows
    protected boolean _executeTask(){
        executingThreadName = Thread.currentThread().getName();
        startTime = System.currentTimeMillis();
        log.debug("{}: start task", getName());
        Thread.sleep(workDurationMillis);
        completeTime = System.currentTimeMillis();
        totalTime = completeTime - startTime;
        log.debug("{}: complete task", getName());
        return true;
    }

    @Slf4j
    public static class Asynchronous extends SimulatedTask{


        public Asynchronous(String name, int workDurationMillis, RequestHandler requestHandler) {
            super(name, workDurationMillis, requestHandler);
        }

        @SneakyThrows
        @OnTrigger(parallelExecution = true)
        public boolean executeTask(){
            return _executeTask();
        }
    }

    @Slf4j
    public static class Synchronous extends SimulatedTask{

        public Synchronous(String name, int workDurationMillis, RequestHandler requestHandler) {
            super(name, workDurationMillis, requestHandler);
        }

        @SneakyThrows
        @OnTrigger
        public boolean executeTask(){
            return _executeTask();
        }
    }
}
