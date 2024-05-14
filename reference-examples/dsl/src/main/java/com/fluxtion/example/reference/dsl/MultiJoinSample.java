package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.compiler.builder.dataflow.MultiJoinBuilder;
import com.fluxtion.runtime.dataflow.groupby.GroupBy;
import lombok.Data;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

public class MultiJoinSample {

    public static void main(String[] args) {

        var processor = Fluxtion.interpret(c -> {
            var ageDataFlow = DataFlow.groupBy(Age::getName);
            var genderDataFlow = DataFlow.groupBy(Gender::getName);
            var nationalityDataFlow = DataFlow.groupBy(Nationality::getName);
            var dependentDataFlow = DataFlow.groupByToList(Dependent::getGuardianName);

            MultiJoinBuilder.builder(String.class, MergedData::new)
                    .addJoin(ageDataFlow, MergedData::setAge)
                    .addJoin(genderDataFlow, MergedData::setGender)
                    .addJoin(nationalityDataFlow, MergedData::setNationality)
                    .addOptionalJoin(dependentDataFlow, MergedData::setDependent)
                    .dataFlow()
                    .mapValues(MergedData::formattedString)
                    .map(GroupBy::toMap)
                    .console("multi join result : {}");
        });
        processor.init();

        processor.onEvent(new Age("greg", 47));
        processor.onEvent(new Gender("greg", "male"));
        processor.onEvent(new Nationality("greg", "UK"));
        //update
        processor.onEvent(new Age("greg", 55));
        //new record
        processor.onEvent(new Age("tim", 47));
        processor.onEvent(new Gender("tim", "male"));
        processor.onEvent(new Nationality("tim", "UK"));

        processor.onEvent(new Dependent("greg", "ajay"));
        processor.onEvent(new Dependent("greg", "sammy"));

    }

    @Data
    public static class MergedData {
        private Age age;
        private Gender gender;
        private Nationality nationality;
        private List<Dependent> dependent;

        public String formattedString() {
            String dependentString = " no dependents";
            if (dependent != null) {
                dependentString = dependent.stream()
                        .map(Dependent::getDependentName)
                        .collect(Collectors.joining(", ", " guardian for: [", "]"));
            }
            return age.getAge() + " " + gender.getSex() + " " + nationality.getCountry() + dependentString;
        }
    }

    @Value
    public static class Age {
        String name;
        int age;
    }

    @Value
    public static class Gender {
        String name;
        String sex;
    }


    @Value
    public static class Nationality {
        String name;
        String country;
    }

    @Value
    public static class Dependent {
        String guardianName;
        String dependentName;
    }
}
