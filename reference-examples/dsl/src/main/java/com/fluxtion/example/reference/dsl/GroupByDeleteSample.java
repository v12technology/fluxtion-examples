package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.builder.FluxtionIgnore;
import com.fluxtion.runtime.dataflow.groupby.GroupBy;
import lombok.AllArgsConstructor;

import java.util.List;

public class GroupByDeleteSample {

    public record Pupil(long pupilId, int year, String name){}

    public static void main(String[] args) {
        EventProcessor processor = Fluxtion.interpret(c -> {
            DataFlow.groupByToList(Pupil::year)
                    .deleteByValue(new DeleteFilter()::leftSchool)
                    .map(GroupBy::toMap)
                    .console();
        });

        processor.init();

        processor.onEvent(new Pupil(1, 2025, "A"));
        processor.onEvent(new Pupil(2, 2025, "B"));
        processor.onEvent(new Pupil(3, 2022, "A_2022"));
        processor.onEvent(new Pupil(1, 2021, "A_2021"));

        //graduate
        System.out.println("\ngraduate 2021");
        processor.onEvent(2022);

        System.out.println("\ngraduate 2022");
        processor.onEvent(2022);

        System.out.println("\ngraduate 2023");
        processor.onEvent(2023);
    }

    public static class DeleteFilter{

        private int currentGraduationYear = Integer.MIN_VALUE;

        @OnEventHandler
        public boolean currentGraduationYear(int currentGraduationYear){
            this.currentGraduationYear = currentGraduationYear;
            return true;
        }

        public boolean leftSchool(List<Pupil> pupil){
            return !pupil.isEmpty() && pupil.getFirst().year() < this.currentGraduationYear;
        }
    }
}
