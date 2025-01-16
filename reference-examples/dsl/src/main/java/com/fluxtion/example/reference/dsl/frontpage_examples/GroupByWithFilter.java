package com.fluxtion.example.reference.dsl.frontpage_examples;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.groupby.GroupBy;
import com.fluxtion.runtime.dataflow.groupby.GroupByKey;
import com.fluxtion.runtime.dataflow.helpers.Aggregates;
import com.fluxtion.runtime.dataflow.helpers.Collectors;
import com.fluxtion.runtime.dataflow.helpers.Predicates;
import lombok.Data;

import java.util.List;

public class GroupByWithFilter {

    public static void main(String[] args) {
        var sep = Fluxtion.interpret(c -> {
            DataFlow.subscribe(MachineReadings.class)
                    .groupByFieldsGetAndAggregate(
                            MachineReadings::temp,
                            Aggregates.doubleAverageFactory(),
                            MachineReadings::id)
                    .resetTrigger(DataFlow.subscribeToSignal("reset"))
                    .filterValues(d -> d > 48)
                    .mapKeys(GroupByKey::getKey)
                    .filter(g -> !g.values().isEmpty())
                    .map(GroupBy::toMap)
                    .filter(Predicates.hasMapChanged())
                    .console("WARNING - INVESTIGATE THESE MACHINES AVG TEMP > 45C - {}");

            var workScheduler = new WorkScheduler();
            DataFlow.subscribe(MachineLocation.class).push(workScheduler::setMachineLocation);
            DataFlow.subscribe(SupportContact.class).push(workScheduler::setSupportContact);

            DataFlow.subscribe(MachineReadings.class)
                    .groupByFieldsGetAndAggregate(MachineReadings::temp, Collectors.listFactory(50), MachineReadings::id)
                    .resetTrigger(DataFlow.subscribeToSignal("reset"))
                    .filterValues(tempReadings -> tempReadings.size() > 10)
                    .mapValues(GroupByWithFilter::listToAverage)
                    .filterValues(temp -> temp > 48)
                    .push(workScheduler::investigateMachine);

        });
        sep.init();

        sep.onEvent(new MachineReadings("A", 34));
        sep.onEvent(new MachineReadings("B", 34));

        sep.onEvent(new MachineReadings("A", 42));
        sep.onEvent(new MachineReadings("B", 49));

        sep.onEvent(new MachineReadings("A", 39));
        sep.onEvent(new MachineReadings("B", 65));

        sep.onEvent(new MachineReadings("B", 35));
        sep.onEvent(new MachineReadings("B", 95));

        sep.publishSignal("reset");
        sep.onEvent(new MachineReadings("A", 95));
        sep.onEvent(new MachineReadings("B", 35));
    }


    public record MachineReadings(String id, double temp) { }

    public record MachineLocation(String id, String locationCode) { }

    public record SupportContact(String name, String locationCode, String contactDetails) { }

    public static double listToAverage(List<Double> list) {
        return list.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    }

    @Data
    public static class WorkScheduler {
        private MachineLocation machineLocation;
        private SupportContact supportContact;

        public void investigateMachine(Object o) {

        }
    }
}
