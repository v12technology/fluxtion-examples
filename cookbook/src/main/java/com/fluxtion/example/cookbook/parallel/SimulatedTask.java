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
    protected transient long startTime;
    protected transient long completeTime;
    protected transient long totalTime;
    protected transient String executingThreadName;

    @SneakyThrows
    protected boolean _executeTask(){
        executingThreadName = Thread.currentThread().getName();
        startTime = System.currentTimeMillis();
        log.debug("{}: start", getName());
        Thread.sleep(workDurationMillis);
        completeTime = System.currentTimeMillis();
        totalTime = completeTime - startTime;
        log.debug("{}: complete - {}ms", getName(), totalTime);
        return true;
    }

    @Slf4j
    public static class Asynchronous extends SimulatedTask{


        public Asynchronous(String name, int workDurationMillis, RequestHandler requestHandler) {
            super(name, workDurationMillis, requestHandler);
        }

        /**
         * The trigger method is annotated with parallelExecution, signaling this method should be run in parallel
         * if possible
         * @return notification of a change
         */
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


        /**
         * The trigger method should be run synchronously before or after sibling trigger methods
         * @return notification of a change
         */
        @SneakyThrows
        @OnTrigger
        public boolean executeTask(){
            return _executeTask();
        }
    }
}
