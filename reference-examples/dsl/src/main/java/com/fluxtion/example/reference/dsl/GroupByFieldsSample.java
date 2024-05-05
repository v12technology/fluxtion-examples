package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.groupby.GroupBy;
import com.fluxtion.runtime.dataflow.groupby.GroupByKey;
import com.fluxtion.runtime.dataflow.helpers.Aggregates;

import java.util.Map;

public class GroupByFieldsSample {

    public record Pupil(int year, String sex, String name){}

    public static void buildGraph(EventProcessorConfig processorConfig) {

        DataFlow.subscribe(Pupil.class)
                .groupByFieldsAggregate(Aggregates.countFactory(), Pupil::year, Pupil::sex)
                .map(GroupByFieldsSample::formatGroupBy)
                .console("Pupil count by year/sex \n----\n{}----\n");
    }

    private static String formatGroupBy(GroupBy<GroupByKey<Pupil>, Integer> groupBy) {
        Map<GroupByKey<Pupil>, Integer> groupByMap = groupBy.toMap();
        StringBuilder stringBuilder = new StringBuilder();
        groupByMap.forEach((k, v) -> stringBuilder.append(k.getKey() + ": " + v + "\n"));
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(GroupByFieldsSample::buildGraph);
        processor.init();

        processor.onEvent(new Pupil(2015, "Female", "Bob"));
        processor.onEvent(new Pupil(2013, "Male", "Ashkay"));
        processor.onEvent(new Pupil(2013, "Male", "Channing"));
        processor.onEvent(new Pupil(2013, "Female", "Chelsea"));
        processor.onEvent(new Pupil(2013, "Female", "Tamsin"));
        processor.onEvent(new Pupil(2013, "Female", "Ayola"));
        processor.onEvent(new Pupil(2015, "Female", "Sunita"));
    }
}
