package com.fluxtion.example.reference.integration.replay;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.replay.YamlReplayRecordWriter;
import com.fluxtion.compiler.replay.YamlReplayRunner;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

public class ReplayExample {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        var processor = Fluxtion.interpret(ReplayExample::buildGraph);
        //record audit log
        StringWriter stringWriter = new StringWriter();
        StringWriter output = new StringWriter();
        YamlReplayRecordWriter yamlReplayRecordWriter = processor.getAuditorById(YamlReplayRecordWriter.DEFAULT_NAME);
        yamlReplayRecordWriter.setTargetWriter(stringWriter);

        processor.init();
        processor.<String>addSink(GlobalPnl.GLOBAL_PNL_SINK_NAME, output::append);

        processor.start();
        processor.onEvent(new PnlUpdate("book1", 200));
        Thread.sleep(250);
        processor.onEvent(new PnlUpdate("bookAAA", 55));
        System.out.println(output);
        System.out.println(stringWriter);


//        //run session
//        YamlReplayRunner.newSession(new StringReader(replayEventLog), eventProcessor())
//                .callStart()
//                .runReplay();
//
//        Assert.assertEquals(replayEventLog, stringWriter.toString());
    }


    private static void buildGraph(EventProcessorConfig c) {
        c.addNode(
                new GlobalPnl(Arrays.asList(
                        new BookPnl("book1"),
                        new BookPnl("bookAAA"),
                        new BookPnl("book_XYZ")
                ))
        );
        //Inject an auditor will see events before any node
        c.addAuditor(
                new YamlReplayRecordWriter().classWhiteList(PnlUpdate.class),
                YamlReplayRecordWriter.DEFAULT_NAME);
    }
}
