package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.compiler.builder.dataflow.GroupByFlowBuilder;
import com.fluxtion.compiler.builder.dataflow.MultiJoinBuilder;
import com.fluxtion.runtime.dataflow.groupby.GroupBy;
import lombok.Data;
import lombok.Value;

public class MultiJoinSample {

    public static void main(String[] args) {

        var processor = Fluxtion.interpret(c -> {
            GroupByFlowBuilder<String, LeftData> leftBuilder = DataFlow.groupBy(LeftData::getName);
            GroupByFlowBuilder<String, MiddleData> middleBuilder = DataFlow.groupBy(MiddleData::getName);
            GroupByFlowBuilder<String, RightData> rightBuilder = DataFlow.groupBy(RightData::getName);

            MultiJoinBuilder.builder(String.class, MergedData::new)
                    .addJoin(leftBuilder, MergedData::setLeftData)
                    .addJoin(middleBuilder, MergedData::setMiddleData)
                    .addJoin(rightBuilder, MergedData::setRightData)
                    .dataFlow()
                    .mapValues(MergedData::formattedString)
                    .map(GroupBy::toMap)
                    .console("multi join result : {}");
        });
        processor.init();

        processor.onEvent(new LeftData("greg", 47));
        processor.onEvent(new MiddleData("greg", "male"));
        processor.onEvent(new RightData("greg", "UK"));
        //update
        processor.onEvent(new LeftData("greg", 55));
        //new record
        processor.onEvent(new LeftData("tim", 47));
        processor.onEvent(new MiddleData("tim", "male"));
        processor.onEvent(new RightData("tim", "UK"));

    }

    @Data
    public static class MergedData {
        private LeftData leftData;
        private MiddleData middleData;
        private RightData rightData;

        public String formattedString() {
            return leftData.getAge() + " " + middleData.getSex() + " " + rightData.getCountry();
        }
    }

    @Value
    public static class LeftData {
        String name;
        int age;
    }

    @Value
    public static class MiddleData {
        String name;
        String sex;
    }


    @Value
    public static class RightData {
        String name;
        String country;
    }
}
