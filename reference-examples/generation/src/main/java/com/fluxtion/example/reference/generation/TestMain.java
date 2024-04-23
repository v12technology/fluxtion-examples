package com.fluxtion.example.reference.generation;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;

public class TestMain {


    public static class MyNode {

        private final String name;

        public MyNode(String name) {
            this.name = name;
        }

        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println(name + " received:" + stringToProcess);
            return true;
        }
    }

    public static void main(String[] args) {
        Fluxtion.compile(
                //binds classes to event processor
                eventProcessorConfig -> eventProcessorConfig.addNode(new MyNode("node A")),
                //controls the generation
                fluxtionCompilerConfig -> {
                    fluxtionCompilerConfig.setClassName("MyEventProcessor");
                    fluxtionCompilerConfig.setPackageName("com.fluxtion.example.aot.generated");
                });
//        EventProcessor<?> processor;
//
//        //these are equivalent processors
//        processor = Fluxtion.interpret(new MyNode("node A"));
//        processor = Fluxtion.interpret(cfg -> {
//            cfg.addNode(new MyNode("node A"));
//        });
//
//        //these are equivalent processors
//        processor = Fluxtion.interpret(new MyNode("node A"), new MyNode("node B"));
//        processor = Fluxtion.interpret(cfg -> {
//            cfg.addNode(new MyNode("node A"));
//            cfg.addNode(new MyNode("node B"));
//        });
//
//        //
//        Fluxtion.interpret(cfg -> {
//            DataFlow.subscribe(String.class)
//                    .push(new MyNode("node A")::handleStringEvent);
//        });

//        processor = Fluxtion.compile(new MyNode("node A"));

        //to use the event processor
//        processor.init();
//        processor.onEvent("hello world");
    }
}
