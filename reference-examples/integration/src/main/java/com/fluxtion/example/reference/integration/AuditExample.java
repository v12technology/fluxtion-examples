package com.fluxtion.example.reference.integration;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.audit.EventLogControlEvent;
import com.fluxtion.runtime.audit.EventLogNode;

public class AuditExample {
    public static void main(String[] args) {
        var processor = Fluxtion.interpret(c ->{
           c.addNode(new MyAuditingNode());
           c.addEventAudit();
        });

        processor.init();

        processor.onEvent("detailed message 1");

        //change log level dynamically
        processor.setAuditLogLevel(EventLogControlEvent.LogLevel.DEBUG);
        processor.onEvent("detailed message 2");
    }

    public static class MyAuditingNode extends EventLogNode {

        @Initialise
        public void init(){
            auditLog.info("MyAuditingNode", "init");
            auditLog.info("MyAuditingNode_debug", "some debug message");
        }

        @OnEventHandler
        public boolean stringEvent(String event) {
            auditLog.info("event", event);
            auditLog.debug("charCount", event.length());
            return true;
        }
    }
}
