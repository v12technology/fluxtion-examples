package com.fluxtion.example.cookbook.exceptionauditlog;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.audit.EventLogControlEvent.LogLevel;
import com.fluxtion.runtime.audit.EventLogNode;

public class PrintAuditLogAfterException {

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(c -> {
            c.addNode(new FailingEventHandler());
            c.addEventAudit(LogLevel.TRACE);
        });
        processor.init();
        try {
            processor.onEvent("test");
            processor.onEvent("FAIL");
        } catch (Exception e) {
            String errorMessage = e.getCause() == null ? e.getMessage() : e.getCause().getMessage();
            System.out.println(errorMessage + " problem processing data, last audit log:\n" + processor.getLastAuditLogRecord());
        }
    }

    public static class FailingEventHandler extends EventLogNode {
        private int count;

        @OnEventHandler
        public boolean processString(String in) {
            auditLog
                    .debug("input", in)
                    .debug("processCount", ++count);
            if (in.equalsIgnoreCase("FAIL")) {
                auditLog
                        .error("badRecord", in)
                        .error("processCount", count);
                throw new RuntimeException("bad record - FAIL");
            }
            return true;
        }
    }
}
