package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.compiler.builder.dataflow.JoinFlowBuilder;
import com.fluxtion.runtime.dataflow.groupby.GroupBy;
import com.fluxtion.runtime.dataflow.helpers.Tuples;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GroupByLeftOuterJoinSample {

    public record Pupil(int year, String school, String name){}
    public record School(String name){}

    public static void buildGraph(EventProcessorConfig processorConfig) {

        var schools = DataFlow.subscribe(School.class)
                .groupBy(School::name);
        var pupils = DataFlow.subscribe(Pupil.class)
                .groupByToList(Pupil::school)
                .defaultValue(GroupBy.emptyCollection());

        JoinFlowBuilder.leftJoin(schools, pupils)
                .mapValues(Tuples.mapTuple(GroupByLeftOuterJoinSample::prettyPrint))
                .map(GroupBy::toMap)
                .console();
    }

    private static String prettyPrint(School schoolName, List<Pupil> pupils) {
        pupils = pupils == null ? Collections.emptyList() : pupils;
        return pupils.stream().map(Pupil::name).collect(Collectors.joining(",", "pupils[", "]") );
    }


    public static void main(String[] args) {
        var processor = Fluxtion.interpret(GroupByLeftOuterJoinSample::buildGraph);
        processor.init();

        //register some schools
        processor.onEvent(new School("RGS"));
        processor.onEvent(new School("Belles"));

        //register some pupils
        processor.onEvent(new Pupil(2015, "RGS", "Bob"));
        processor.onEvent(new Pupil(2013, "RGS", "Ashkay"));
        processor.onEvent(new Pupil(2013, "Belles", "Channing"));
        processor.onEvent(new Pupil(2015, "Belles", "Sunita"));

        System.out.println("left outer join\n");
        //left outer
        processor.onEvent(new School("Framling"));
    }
}
