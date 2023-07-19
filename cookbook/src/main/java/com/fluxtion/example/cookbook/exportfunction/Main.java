package com.fluxtion.example.cookbook.exportfunction;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.example.cookbook.exportfunction.data.Food;
import com.fluxtion.example.cookbook.exportfunction.data.StockDelivery;
import com.fluxtion.example.cookbook.exportfunction.events.FxRate;
import com.fluxtion.example.cookbook.exportfunction.generated.RealtimeCashMonitor;
import com.fluxtion.example.cookbook.exportfunction.nodes.BankAlert;
import com.fluxtion.example.cookbook.util.GenerationStrategy;

import java.util.Date;

public class Main {

    private static final GenerationStrategy generationStrategy = GenerationStrategy.USE_AOT;

    public static void main(String[] args) {
        final CashMonitor realtimeCashMonitor = generateRealtimeProcessor();
        realtimeCashMonitor.init();
        //add fx rate or everything is NaN for gbp
        realtimeCashMonitor.onEvent(new FxRate("GBPUSD", 1.2));

        //generics
        StockDelivery<Food> foodStockDelivery = new StockDelivery<>(false, 30, 4);
        realtimeCashMonitor.foodStockUpdate(foodStockDelivery);

        //add some cash - we are negative
        realtimeCashMonitor.addCash("investment-1", new Date(), 250);

        //general trading
        realtimeCashMonitor.payBill("rent", new Date(), 125);
        realtimeCashMonitor.saleUpdate("online-xxxx", 5, 125);
        realtimeCashMonitor.saleUpdate("online-255", 9, 500);

        //update rates - GBPUSD is filtered, other rates ignored
        realtimeCashMonitor.onEvent(new FxRate("GBPUSD", 1.50));
        realtimeCashMonitor.onEvent(new FxRate("EURUSD", 1.16));
    }

    private static CashMonitor generateRealtimeProcessor() {
        return switch (generationStrategy) {
            case USE_AOT -> new RealtimeCashMonitor();
            case INTERPRET -> Fluxtion.interpret(Main::buildGraph).asInterface();
            case COMPILE -> Fluxtion.compile(Main::buildGraph).asInterface();
            case GENERATE_AOT -> Fluxtion.compileAot(Main::buildGraph,
                    "com.fluxtion.example.cookbook.exportfunction.generated",
                    "RealtimeCashMonitor").asInterface();
        };
    }

    private static void buildGraph(EventProcessorConfig processorConfig) {
        processorConfig.addNode(new BankAlert());
        processorConfig.addInterfaceImplementation(CashMonitor.class);
    }

}
