package com.fluxtion.example.reference.integration;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.audit.EventLogControlEvent;
import com.fluxtion.runtime.audit.EventLogNode;
import com.fluxtion.runtime.audit.LogRecord;
import com.fluxtion.runtime.time.Clock;

public class AuditControlExample {
    public static void main(String[] args) {
        var processor = Fluxtion.interpret(c ->{
           c.addNode(new MyAuditingNode());
           c.addEventAudit();
        });

        processor.init();

        //AUDIT IS INFO BY DEFAULT
        processor.onEvent("detailed message 1");

        //CHANGE LOG LEVEL DYNAMICALLY
        processor.setAuditLogLevel(EventLogControlEvent.LogLevel.DEBUG);
        processor.onEvent("detailed message 2");

        //REPLACE LOGRECORD ENCODER
        processor.setAuditLogRecordEncoder(new MyLogEncoder(Clock.DEFAULT_CLOCK));
        //REPLACE LOGRECORD PROCESSOR
        processor.setAuditLogProcessor(logRecord -> {
            System.err.println("WARNING -> "+ logRecord.toString());
        });


        processor.onEvent("detailed message 1");
        processor.onEvent("detailed message 2");
    }

    public static class MyLogEncoder extends LogRecord{

        public MyLogEncoder(Clock clock) {
            super(clock);
        }


        @Override
        public CharSequence asCharSequence(){
            return "IGNORING ALL RECORDS!!";
        }
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
