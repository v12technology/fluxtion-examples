package com.fluxtion.example.cookbook.parallel;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.example.cookbook.parallel.SimulatedTask.Asynchronous;
import com.fluxtion.example.cookbook.parallel.SimulatedTask.Synchronous;
import com.fluxtion.runtime.EventProcessor;

/**
 * Example of using parallel execution in Fluxtion. The {@link TaskCollector} collects the results from a list of parent
 * {@link SimulatedTask}, that run in parallel or synchronously. Executing the main produces a set of results for
 * both parallel and synchronously execution, displayed as an ascii bar graph:
 * <pre>
 *  Parallel trigger test
 * ====================================================================================================
 *
 * TOTAL EXECUTION TIME : 258ms
 * ----------------------------------------------------------------------------------------------------
 * async1   FJ-worker-1 |  *************************
 * async2   FJ-worker-2 |  ***********************
 * async3   FJ-worker-3 |  **
 * async4   FJ-worker-4 |  *******************
 * ----------------------------------------------------------------------------------------------------
 * Time milliesconds      0   50   100  150  200  250  300  350  400  450  500  550  600  650  700
 *
 *
 * Synchronous trigger test
 * ====================================================================================================
 *
 * TOTAL EXECUTION TIME : 694ms
 * ----------------------------------------------------------------------------------------------------
 * sync1           main |  *************************
 * sync2           main |                           ***********************
 * sync3           main |                                                  *
 * sync4           main |                                                   *******************
 * ----------------------------------------------------------------------------------------------------
 * Time milliesconds      0   50   100  150  200  250  300  350  400  450  500  550  600  650  700
 * </pre>
 *
 * The {@link com.fluxtion.runtime.annotations.OnTrigger} annotation controls the parallel execution of the trigger
 * task:
 * <ul>
 *     <li>Asynchronous {@link Asynchronous#executeTask()} OnTrigger(parallelExecution = true)</li>
 *     <li>Synchronous {@link Synchronous#executeTask()} OnTrigger()</li>
 * </ul>
 */
public class Main {

    public static void main(String[] args) throws NoSuchFieldException {
        //uncomment to see task execution log output
        //System.setProperty("org.slf4j.simpleLogger.log.com.fluxtion.example.cookbook.parallel", "DEBUG");
        var eventProcessor = Fluxtion.interpret(c -> {
            RequestHandler requestHandler = new RequestHandler();
            c.addNode(
                    TaskCollector.builder()
                            .task(new Asynchronous("async1", 250, requestHandler))
                            .task(new Asynchronous("async2", 225, requestHandler))
                            .task(new Asynchronous("async3", 18, requestHandler))
                            .task(new Asynchronous("async4", 185, requestHandler))
                            .requestHandler(requestHandler)
                            .build(), "taskCollector"
            );
        });
        runTest(eventProcessor, "Parallel trigger test");

        eventProcessor = Fluxtion.interpret(c -> {
            RequestHandler requestHandler = new RequestHandler();
            c.addNode(
                    TaskCollector.builder()
                            .task(new Synchronous("sync1", 250, requestHandler))
                            .task(new Synchronous("sync2", 225, requestHandler))
                            .task(new Synchronous("sync3", 18, requestHandler))
                            .task(new Synchronous("sync4", 185, requestHandler))
                            .requestHandler(requestHandler)
                            .build(), "taskCollector"
            );
        });
        runTest(eventProcessor, "\nSynchronous trigger test");
    }

    private static void runTest(EventProcessor<?> eventProcessor, String title) throws NoSuchFieldException {
        eventProcessor.init();
        TaskCollector taskCollector = eventProcessor.getNodeById("taskCollector");
        System.out.println(title);
        System.out.println("=".repeat(100));
        eventProcessor.onEvent("test");
        System.out.println("\nTOTAL EXECUTION TIME : " + taskCollector.getDuration() + "ms");
        System.out.println("-".repeat(100));
        System.out.println(taskCollector.getResults());
    }
}
