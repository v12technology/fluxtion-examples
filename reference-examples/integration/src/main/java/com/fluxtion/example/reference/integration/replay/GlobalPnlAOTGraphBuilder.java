package com.fluxtion.example.reference.integration.replay;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;
import com.fluxtion.compiler.replay.YamlReplayRecordWriter;

import java.util.Arrays;

public class GlobalPnlAOTGraphBuilder implements FluxtionGraphBuilder {
    @Override
    public void buildGraph(EventProcessorConfig processorConfig) {
        processorConfig.addNode(
                new GlobalPnl(Arrays.asList(
                        new BookPnl("book1"),
                        new BookPnl("bookAAA"),
                        new BookPnl("book_XYZ")
                ))
        );
        //Inject an auditor will see events before any node
        processorConfig.addAuditor(
                new YamlReplayRecordWriter().classWhiteList(PnlUpdate.class),
                YamlReplayRecordWriter.DEFAULT_NAME);
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig compilerConfig) {
        compilerConfig.setClassName("GlobalPnlProcessor");
        compilerConfig.setPackageName("com.fluxtion.example.reference.integration.replay.generated");
    }
}
