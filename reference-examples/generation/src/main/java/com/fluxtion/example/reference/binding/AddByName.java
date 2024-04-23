package com.fluxtion.example.reference.binding;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.node.NamedNode;

public class AddByName {

    public static class MyNode {

        private final String name;

        public MyNode(String name) {
            this.name = name;
        }


        public String getName() {
            return name;
        }
    }

    public static class MyNamedNode implements NamedNode {

        private final String name;

        public MyNamedNode(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public static void main(String[] args) throws NoSuchFieldException {
        var processor = Fluxtion.interpret(cfg -> {
            cfg.addNode(new MyNode("customName"), "overrideName");
            cfg.addNode(new MyNamedNode("name1"));
        });
        processor.init();

        System.out.println(processor.<MyNode>getNodeById("overrideName").getName());
        System.out.println(processor.<MyNamedNode>getNodeById("name1").getName());
    }
}
