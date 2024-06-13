package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.annotations.builder.FluxtionIgnore;

public class GroupByDeleteSample {

    public record Pupil(long pupilId, int year, String name){}

//    public static void main(String[] args) {
//        Fluxtion.interpret(c ->{
//            DataFlow.groupByToList(Pupil::year)
//                    .deleteByKey(DataFlow.subscribe(Integer.class).mapToList().map(f -> f.stream().toList()))
//        });
//    }
//
//    public static class
}
