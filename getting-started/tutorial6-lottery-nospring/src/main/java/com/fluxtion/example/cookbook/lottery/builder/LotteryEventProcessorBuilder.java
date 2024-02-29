package com.fluxtion.example.cookbook.lottery.builder;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;
import com.fluxtion.example.cookbook.lottery.nodes.LotteryMachineNode;
import com.fluxtion.example.cookbook.lottery.nodes.TicketStoreNode;
import com.fluxtion.runtime.audit.EventLogControlEvent;

/**
 * This class is discovered by the maven plugin to generate an {@link com.fluxtion.runtime.EventProcessor} ahead of time
 * during the compilation phase. The output is the {@link com.fluxtion.example.cookbook.lottery.aot.LotteryProcessor} source
 * file.
 *
 * <p>
 * Nodes are added to the graph using the {{@link #buildGraph(EventProcessorConfig)}} method.
 * </p>
 *
 * <p>
 * Source file class and package name are configured in the {@link #configureGeneration(FluxtionCompilerConfig)} method
 * </p>
 */
public class LotteryEventProcessorBuilder implements FluxtionGraphBuilder {
    @Override
    public void buildGraph(EventProcessorConfig eventProcessorConfig) {
        TicketStoreNode ticketStore = new TicketStoreNode();
        LotteryMachineNode lotteryMachine = new LotteryMachineNode(ticketStore);
        eventProcessorConfig.addNode(lotteryMachine, "lotteryMachine");
        //no need to do this as fluxtion will automatically add the ticketStore instance by inspection of lotteryMachine
        //but nice to make the names the same as the spring file, can prove the generated files are identical
        eventProcessorConfig.addNode(ticketStore, "ticketStore");
        eventProcessorConfig.addEventAudit(EventLogControlEvent.LogLevel.DEBUG);
    }

    @Override
    public void configureGeneration(FluxtionCompilerConfig fluxtionCompilerConfig) {
        fluxtionCompilerConfig.setClassName("LotteryProcessor");
        fluxtionCompilerConfig.setPackageName("com.fluxtion.example.cookbook.lottery.aot");
    }
}
