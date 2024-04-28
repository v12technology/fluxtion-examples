package com.fluxtion.example.reference.libraryfunction;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.EventProcessorContext;
import com.fluxtion.runtime.annotations.Start;
import com.fluxtion.runtime.annotations.builder.Inject;

public class ContextParamInput {
    public static class ContextParamReader {
        @Inject
        public EventProcessorContext context;

        @Start
        public void start() {
            System.out.println("myContextParam1 -> " + context.getContextProperty("myContextParam1"));
            System.out.println("myContextParam2 -> " + context.getContextProperty("myContextParam2"));
            System.out.println();
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new ContextParamReader());
        processor.init();

        processor.addContextParameter("myContextParam1", "[param1: update 1]");
        processor.start();

        processor.addContextParameter("myContextParam1", "[param1: update 2]");
        processor.addContextParameter("myContextParam2", "[param2: update 1]");
        processor.start();
    }
}
