package com.fluxtion.example.reference.integration;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.node.NamedNode;

public class GetNodeByIdExample {
    public static void main(String[] args) throws NoSuchFieldException {
        var processor = Fluxtion.interpret(new MondayChecker());
        processor.init();

        processor.onEvent("Monday");
        processor.onEvent("Tuesday");
        processor.onEvent("Wednesday");

        //LOOKUP USER NODE
        MondayChecker mondayChecker = processor.getNodeById("MondayChecker");

        //PULL DATA
        System.out.println("PULLING Monday count:" + mondayChecker.getMondayCount());

        processor.onEvent("Monday");
        //PULL DATA
        System.out.println("PULLING Monday count:" + mondayChecker.getMondayCount());
    }

    public static class MondayChecker implements NamedNode {
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
