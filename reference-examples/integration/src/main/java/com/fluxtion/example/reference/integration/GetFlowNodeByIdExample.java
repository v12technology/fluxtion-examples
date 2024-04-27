package com.fluxtion.example.reference.integration;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.dataflow.helpers.Mappers;
import com.fluxtion.runtime.node.NamedNode;
import lombok.Data;

public class GetFlowNodeByIdExample {
    public static void main(String[] args) throws NoSuchFieldException {
        var processor = Fluxtion.interpret(c ->{
            DataFlow.subscribe(String.class)
                    .filter(s -> s.equalsIgnoreCase("monday"))
                    //ID START - this makes the wrapped value accessible via the id
                    .mapToInt(Mappers.count()).id("MondayChecker")
                    //ID END
                    .console("Monday is triggered");
        });
        processor.init();

        processor.onEvent("Monday");
        processor.onEvent("Tuesday");
        processor.onEvent("Wednesday");

        //ACCESS THE WRAPPED VALUE BY ITS ID
        Integer mondayCheckerCount = processor.getStreamed("MondayChecker");
        System.out.println("Monday count:" + mondayCheckerCount + "\n");

        //ACCESS THE WRAPPED VALUE BY ITS ID
        processor.onEvent("Monday");
        mondayCheckerCount = processor.getStreamed("MondayChecker");
        System.out.println("Monday count:" + mondayCheckerCount);
    }
}
