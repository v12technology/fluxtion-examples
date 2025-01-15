package com.fluxtion.example.cookbook.tempmonitoring;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.aggregate.function.primitive.DoubleAverageFlowFunction;
import com.fluxtion.runtime.dataflow.aggregate.function.primitive.DoubleMaxFlowFunction;
import com.fluxtion.runtime.dataflow.aggregate.function.primitive.DoubleMinFlowFunction;
import com.fluxtion.runtime.dataflow.helpers.Mappers;

import java.util.*;

public class Main {
    private static final int ROLLING_WINDOW_SIZE = 10;
    private static final double BASE_THRESHOLD = 100.0;

    public static void main(String[] args) {
        var eventProcessor = Fluxtion.interpret(Main::bindFunctions);
        eventProcessor.init();

        Timer timer = new Timer();
        Random random = new Random();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                double randomValue1 = random.nextDouble() * 100;
                double randomValue2 = random.nextDouble() * 100;

                eventProcessor.onEvent(new Data1(randomValue1));
                eventProcessor.onEvent(new Data2(randomValue2));
            }
        }, 0, 1000);

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private static void bindFunctions(EventProcessorConfig cfg) {

        var dataSumStream = DataFlow.mapBiFunction(
                Mappers::addDoubles,
                DataFlow.subscribe(Data1::value).defaultValue(0d),
                DataFlow.subscribe(Data2::value).defaultValue(0d));

        //sliding window monitoring
        var avgStream = dataSumStream.slidingAggregateByCount(DoubleAverageFlowFunction::new, ROLLING_WINDOW_SIZE);
        var minStream = dataSumStream.slidingAggregateByCount(DoubleMinFlowFunction::new, ROLLING_WINDOW_SIZE);
        var maxStream = dataSumStream.slidingAggregateByCount(DoubleMaxFlowFunction::new, ROLLING_WINDOW_SIZE);

        BreachMonitor breachMonitor = new BreachMonitor();
        DataFlow.push(breachMonitor::update, dataSumStream, avgStream, minStream, maxStream)
                .filter(breachMonitor::exceedsDynamicThreshold)
                .console("ALERT: Rolling average exceeds dynamic threshold! Adjust cooling system!");

        //Global monitoring
        dataSumStream.filter(d -> d > BASE_THRESHOLD)
                .console("WARNING: sum:{} exceeds base threshold:" + BASE_THRESHOLD);
    }

    public record Data1(double value) {
    }

    public record Data2(double value) {
    }

    public static class BreachMonitor{

        private double average;
        private double min;
        private double max;

        public void update(double sum, double average, double min, double max){
            System.out.println(String.format("Sum: %.2f | Avg: %.2f | Max: %.2f | Min: %.2f", sum, average, max, min));
            this.average = average;
            this.min = min;
            this.max = max;
        }

        public boolean exceedsDynamicThreshold(){
            double dynamicThreshold = BASE_THRESHOLD/2 + average / 2;
            return average > dynamicThreshold;
        }
    }
}