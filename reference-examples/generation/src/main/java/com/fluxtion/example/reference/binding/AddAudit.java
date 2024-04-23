package com.fluxtion.example.reference.binding;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.node.SingleNamedNode;

public class AddAudit {

    public static class MyNode extends SingleNamedNode {

        public MyNode(String name) {
            super(name);
        }

        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            return true;
        }

        @Override
        public String toString() {
            return "MyNode{}";
        }
    }


    public static class Root1 {
        private final MyNode myNode;

        public Root1(MyNode myNode) {
            this.myNode = myNode;
        }

        @OnTrigger
        public boolean trigger() {
            return true;
        }

        @Override
        public String toString() {
            return "Root1{" +
                    "myNode=" + myNode +
                    '}';
        }
    }

    public static class MyAuditor implements Auditor{
        @Override
        public void nodeRegistered(Object node, String nodeName) {
            System.out.printf("nodeRegistered  nodeName:'%s'  node:'%s'%n", nodeName, node);
        }

        @Override
        public void eventReceived(Object event) {
            System.out.println("eventReceived " + event);
        }

        @Override
        public void nodeInvoked(Object node, String nodeName, String methodName, Object event) {
            System.out.printf("nodeInvoked  nodeName:'%s' invoked:'%s' node:'%s'%n", nodeName, methodName, node);
        }

        @Override
        public boolean auditInvocations() {
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(cfg -> {
            cfg.addNode(new MyNode("unlinked"), new Root1(new MyNode("linked")));
            cfg.addAuditor(new MyAuditor(), "myAuditor");
        });
        processor.init();

        processor.onEvent("TEST");
    }
}
