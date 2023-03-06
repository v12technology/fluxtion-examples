package com.fluxtion.example.cookbook.auditlog;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.audit.EventLogControlEvent.LogLevel;
import com.fluxtion.runtime.audit.EventLogNode;
import com.fluxtion.runtime.audit.LogRecord;
import com.fluxtion.runtime.node.NamedNode;

import java.util.List;

public class AudiLogExample {

    public static void main(String[] args) {
        var eventProcessor = Fluxtion.interpret(AudiLogExample::buildProcessor);

        eventProcessor.init();
        eventProcessor.setAuditLogProcessor(AudiLogExample::printLogRecord);
        eventProcessor.onEvent(new DataEvent("A"));
        eventProcessor.onEvent(new DataEvent("B"));
        eventProcessor.onEvent(new PublishEvent());
        eventProcessor.onEvent(new CalculateEvent("ABC"));
        eventProcessor.onEvent(new ConfigEvent());
        eventProcessor.onEvent(new DataEvent("EFG"));

        System.out.println("\nXXXXXX upping the audit log level to DEBUG XXXX\n");
        eventProcessor.setAuditLogLevel(LogLevel.DEBUG);
        eventProcessor.onEvent("TEST_STRING_EVENT");
        eventProcessor.onEvent(new DataEvent("C"));
        eventProcessor.onEvent(new CalculateEvent("AB"));
        eventProcessor.onEvent(new CalculateEvent("ABNHGH"));
        eventProcessor.onEvent(new ConfigEvent());
        eventProcessor.onEvent(new PublishEvent());

        System.out.println("\nXXXXXX log level NONE for all XXXX\n");
        eventProcessor.setAuditLogLevel(LogLevel.NONE);
        eventProcessor.onEvent(new DataEvent("C"));
        eventProcessor.onEvent(new CalculateEvent("AB"));
        eventProcessor.onEvent(new CalculateEvent("ABNHGH"));
        eventProcessor.onEvent(new ConfigEvent());
        eventProcessor.onEvent(new PublishEvent());

        System.out.println("\nXXXXXX log level DEBUG for publisher XXXX\n");
        eventProcessor.setAuditLogLevel(LogLevel.DEBUG, "publisher");
        ;
        eventProcessor.onEvent(new DataEvent("A"));
        eventProcessor.onEvent(new CalculateEvent("AB"));
        eventProcessor.onEvent(new DataEvent("B"));
        eventProcessor.onEvent(new CalculateEvent("ABNHGH"));
        eventProcessor.onEvent(new ConfigEvent());
        eventProcessor.onEvent(new PublishEvent());
    }

    private static void printLogRecord(LogRecord logRecord) {
        System.out.println(logRecord.toString() + "\n---");
    }

    private static void buildProcessor(EventProcessorConfig cfg) {
        var configHandler = new ConfigHandler();
        var dataHandlerA = new DataHandler("A");
        var dataHandlerB = new DataHandler("B");
        var dataHandlerC = new DataHandler("C");

        var calcHandlerAB = new CalcHandler(
                configHandler,
                "AB",
                List.of(dataHandlerA, dataHandlerB));

        var calcHandlerA = new CalcHandler(
                configHandler,
                "A",
                List.of(dataHandlerA));

        var calcHandlerAC = new CalcHandler(
                configHandler,
                "AC",
                List.of(dataHandlerA, dataHandlerC));

        var calcHandlerABC = new CalcHandler(
                configHandler,
                "ABC",
                List.of(dataHandlerA, dataHandlerB, dataHandlerC));

        var publishHandler = new PublishCalcHandler(List.of(
                calcHandlerA,
                calcHandlerAB,
                calcHandlerAC,
                calcHandlerABC
        ));
        cfg.addNode(publishHandler);
        cfg.addEventAudit(LogLevel.DEBUG);
    }

    public record ConfigEvent() {
    }

    public record CalculateEvent(String name) {
    }

    public record DataEvent(String name) {
    }

    public record PublishEvent() {
    }


    public static class ConfigHandler {

        @OnEventHandler
        public boolean configUpdated(ConfigEvent configEvent) {
            return false;
        }
    }

    public static class DataHandler implements NamedNode {
        private final String dataId;

        public DataHandler(String dataId) {
            this.dataId = dataId;
        }

        @OnEventHandler
        public boolean dataUpdated(DataEvent dataEvent) {
            return dataEvent.name().equals(dataId);
        }

        @Override
        public String getName() {
            return "dataHandler_" + dataId;
        }
    }

    public static class PublishCalcHandler extends EventLogNode implements NamedNode {
        private final List<CalcHandler> calcHandlerList;

        public PublishCalcHandler(List<CalcHandler> calcHandlerList) {
            this.calcHandlerList = calcHandlerList;
        }

        @OnTrigger
        public boolean calculatorTriggered() {
            auditLog.info("recalculate", true);
            return true;
        }

        @OnEventHandler
        public boolean publish(PublishEvent publishEvent) {
            return true;
        }

        @Override
        public String getName() {
            return "publisher";
        }
    }

    public static class CalcHandler extends EventLogNode implements NamedNode {
        private final List<DataHandler> calcHandlerList;
        private final ConfigHandler configHandler;
        private final String calculatorName;

        public CalcHandler(ConfigHandler configHandler,
                           String calculatorName,
                           List<DataHandler> calcHandlerList) {
            this.calcHandlerList = calcHandlerList;
            this.configHandler = configHandler;
            this.calculatorName = calculatorName;
        }

        @OnEventHandler
        public boolean recalculate(CalculateEvent calculateEvent) {
            boolean matchId = calculateEvent.name().equals(calculatorName);
            auditLog.warn("matchedRecalcId", matchId);
            return matchId;
        }

        @OnTrigger
        public boolean triggered() {
            return false;
        }

        @Override
        public String getName() {
            return "calcHandler_" + calculatorName;
        }
    }
}
