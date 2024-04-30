package com.fluxtion.example.reference.integration.replay;

import com.fluxtion.compiler.replay.YamlReplayRecordWriter;
import com.fluxtion.compiler.replay.YamlReplayRunner;
import com.fluxtion.example.reference.integration.replay.generated.GlobalPnlProcessor;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public class GeneraEventLogMain {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        StringWriter eventLog = new StringWriter();
        //run the processor and capture event log
        System.out.println("CAPTURE RUN:");
        generateEventLog(eventLog);

        //run a replay
        System.out.println("\nREPLAY RUN:");
        runReplay(eventLog.toString());
    }

    private static void generateEventLog(Writer writer) throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        GlobalPnlProcessor globalPnlProcessor = new GlobalPnlProcessor();
        globalPnlProcessor.init();

        YamlReplayRecordWriter yamlReplayRecordWriter = globalPnlProcessor.getAuditorById(YamlReplayRecordWriter.DEFAULT_NAME);
        yamlReplayRecordWriter.setTargetWriter(writer);

        globalPnlProcessor.start();
        globalPnlProcessor.onEvent(new PnlUpdate("book1", 200));
        Thread.sleep(250);
        globalPnlProcessor.onEvent(new PnlUpdate("bookAAA", 55));
    }

    private static void runReplay(String eventLog){
        YamlReplayRunner.newSession(new StringReader(eventLog), new GlobalPnlProcessor())
                .callInit()
                .callStart()
                .runReplay();
    }
}
