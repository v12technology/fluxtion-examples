package com.fluxtion.example.reference.dsl;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.dataflow.FlowSupplier;

public class DataFlowAsMemberSample {

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(c -> {
            var flowSupplier = DataFlow.subscribe(String.class).flowSupplier();
            new MyFlowHolder(flowSupplier);
        });
        processor.init();

        processor.onEvent("test");
    }

    public record MyFlowHolder(FlowSupplier<String> flowSupplier) {
        @OnTrigger
        public boolean onTrigger() {
            //FlowSupplier is used at runtime to access the current value of the data flow
            System.out.println("triggered by data flow -> " + flowSupplier.get().toUpperCase());
            return true;
        }
    }
}
