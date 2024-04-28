package com.fluxtion.example.reference.integration;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.audit.Auditor;

public class AuditLookupExample {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        var processor = Fluxtion.interpret(c -> {
            //ADDING A NAMED AUDITOR
            c.addAuditor(new MyAuditor(), "myAuditor");
            DataFlow.subscribe(String.class);
        });
        processor.init();
        processor.onEvent("A");
        processor.onEvent("B");
        processor.onEvent("C");

        //LOOKUP AUDITOR BY NAME
        MyAuditor myAuditor = processor.getAuditorById("myAuditor");
        //PULL DATA FROM AUDITOR
        System.out.println("\nPULL MyAuditor::invocationCount " + myAuditor.getInvocationCount());
    }

    public static class MyAuditor implements Auditor {
        private int invocationCount = 0;

        @Override
        public void nodeRegistered(Object node, String nodeName) {
        }

        @Override
        public void eventReceived(Object event) {
            System.out.println("MyAuditor::eventReceived " + event);
            invocationCount++;
        }

        public int getInvocationCount() {
            return invocationCount;
        }
    }
}
