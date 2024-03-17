package com.fluxtion.example.reference;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.node.NamedNode;
import com.fluxtion.runtime.node.SingleNamedNode;

public class AddNamedNode {

    public static class MyNode extends SingleNamedNode {

        public MyNode(String name) {
            super(name);
        }
    }

    public static void main(String[] args) throws NoSuchFieldException {
        var processor = Fluxtion.interpret(new MyNode("name1"));
        processor.init();

        System.out.println(processor.<MyNode>getNodeById("name1").getName());
    }
}
