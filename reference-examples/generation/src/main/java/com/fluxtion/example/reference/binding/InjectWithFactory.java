package com.fluxtion.example.reference.binding;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.factory.NodeFactory;
import com.fluxtion.compiler.builder.factory.NodeRegistry;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnParentUpdate;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.annotations.builder.Config;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.google.auto.service.AutoService;

import java.util.Map;

public class InjectWithFactory {
    public static class MyNode {
        private final String filter;

        public MyNode(String filter) {
            this.filter = filter;
        }

        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            boolean match = stringToProcess.equals(filter);
            System.out.println(toString() +  " match:" + match + " for:" + stringToProcess);
            return match;
        }

        @Override
        public String toString() {
            return "MyNode{" +
                    "filter='" + filter + '\'' +
                    '}';
        }
    }

    public static class Root1 {
        @Inject
        @Config(key = "filter", value = "red")
        public MyNode myNodeRed;

        @Inject
        @Config(key = "filter", value = "blue")
        public MyNode myNodeBlue;

        @OnParentUpdate
        public void parentUpdated(Object parent){
            System.out.println("Root1::parentUpdated " + parent);
        }

        @OnTrigger
        public boolean trigger() {
            System.out.println("Root1::triggered");
            return true;
        }
    }

    @AutoService(NodeFactory.class)
    public static class MyNodeFactory implements NodeFactory<MyNode>{
        @Override
        public MyNode createNode(Map<String, Object> config, NodeRegistry registry) {

            return new MyNode((String) config.get("filter"));
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(cfg -> {
            cfg.addNode(new Root1());
        });
        processor.init();

        processor.onEvent("red");
        System.out.println();
        processor.onEvent("ignored");
        System.out.println();
        processor.onEvent("blue");
    }
}
