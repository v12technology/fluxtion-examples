package com.fluxtion.example.reference.integration;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.node.NamedNode;

public class GetNodeByIdExample {
    public static void main(String[] args) throws NoSuchFieldException {
        var processor = Fluxtion.interpret(c ->{
            DataFlow.subscribeToNode(new DirtyStateNode())
                    .console("Monday is triggered");
        });
        processor.init();

        processor.onEvent("Monday");
        processor.onEvent("Tuesday");
        processor.onEvent("Wednesday");

        DirtyStateNode dirtyStateNode = processor.getNodeById("MondayChecker");
        System.out.println("Monday count:" + dirtyStateNode.getMondayCount() + "\n");


        processor.onEvent("Monday");
        System.out.println("Monday count:" + dirtyStateNode.getMondayCount());
    }

    public static class DirtyStateNode implements NamedNode {

        private int mondayCounter = 0;

        @OnEventHandler
        public boolean checkIsMonday(String day){
            boolean isMonday = day.equalsIgnoreCase("monday");
            mondayCounter += isMonday ? 1 : 0;
            return isMonday;
        }

        public int getMondayCount() {
            return mondayCounter;
        }

        @Override
        public String getName() {
            return "MondayChecker";
        }
    }
}
