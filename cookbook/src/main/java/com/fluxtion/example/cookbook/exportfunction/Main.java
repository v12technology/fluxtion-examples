package com.fluxtion.example.cookbook.exportfunction;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.example.cookbook.exportfunction.events.Food;
import com.fluxtion.example.cookbook.exportfunction.events.FxRate;
import com.fluxtion.example.cookbook.exportfunction.events.StockDelivery;
import com.fluxtion.example.cookbook.exportfunction.generated.RealtimeCashMonitor;
import com.fluxtion.example.cookbook.exportfunction.nodes.BankAlert;

import java.util.Date;

public class Main {

    private static final boolean GENERATE_PROCESSOR = false;

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
        if (GENERATE_PROCESSOR) {
            return (CashMonitor) Fluxtion.compileAot(c -> {
                c.addNode(new BankAlert());
                c.addInterfaceImplementation(CashMonitor.class);
            }, "com.fluxtion.example.cookbook.exportfunction.generated", "RealtimeCashMonitor");
        } else {
            return new RealtimeCashMonitor();
        }
    }
}
