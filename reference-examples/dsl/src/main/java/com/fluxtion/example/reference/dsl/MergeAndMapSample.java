package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.compiler.builder.dataflow.MergeAndMapFlowBuilder;
import lombok.Data;

import java.util.Date;

import static com.fluxtion.compiler.builder.dataflow.DataFlow.subscribe;

public class MergeAndMapSample {
    public static void main(String[] args) {
        var processor = Fluxtion.interpret(c ->
                MergeAndMapFlowBuilder.of(MyData::new)
                        .required(subscribe(String.class), MyData::setCustomer)
                        .required(subscribe(Date.class), MyData::setDate)
                        .required(subscribe(Integer.class), MyData::setId)
                        .dataFlow()
                        .console("new customer : {}")
        );
        processor.init();

        processor.onEvent(new Date());
        processor.onEvent("John Doe");
        //only publishes when the last required flow is received
        processor.onEvent(123);
    }

    @Data
    public static class MyData {
        private String customer;
        private Date date;
        private int id;
    }
}
