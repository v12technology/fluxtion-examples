/*
 * SPDX-FileCopyrightText: © 2024 Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package com.fluxtion.example.cookbook.pnl.flatmapexample;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.example.cookbook.pnl.calculator.PnlSummaryCalc;
import com.fluxtion.example.cookbook.pnl.calculator.TradeLegToPositionAggregate;
import com.fluxtion.example.cookbook.pnl.events.*;

import com.fluxtion.example.cookbook.pnl.flatmapexample.generated.PnlFromFlatMapCalculator;
import com.fluxtion.runtime.EventProcessor;

import static com.fluxtion.example.cookbook.pnl.refdata.RefData.*;

/**
 * Pnl calculator example that uses flatmap operations, no joins and single groupBy methods to achieve the same result as
 * {@link com.fluxtion.example.cookbook.pnl.joinexample.PnlExampleMain}
 *
 * This results in less memory allocations and an additional event cycle for the flatmap. Fluxtion is very efficient in
 * processing an event cycle, down in the nanosecond range in current hardware, so this is probably a good trade off.
 */
public class PnlExampleMain {

    public static final String EOB_TRADE_KEY = "eob";

    public static void main(String[] args) {
        var pnlCalculator = Fluxtion.interpret(PnlExampleMain::buildPnlFlatMap);
        pnlCalculator.init();
        sendEvents(pnlCalculator);
    }

    public static void buildPnlFlatMap(EventProcessorConfig c) {
        PnlSummaryCalc pnlSummaryCalc = new PnlSummaryCalc();
        DataFlow.subscribe(Trade.class)
                .flatMapFromArray(Trade::tradeLegs, EOB_TRADE_KEY)
                .groupBy(TradeLeg::instrument, TradeLegToPositionAggregate::new)
                .publishTriggerOverride(pnlSummaryCalc)
                .map(pnlSummaryCalc::calcMtmAndUpdateSummary)
                .console();

        c.setSupportBufferAndTrigger(false);
    }

    private static void sendEvents(EventProcessor pnlCalculator) {
        pnlCalculator.onEvent(new Trade(symbolEURJPY, -400, 80000));

        pnlCalculator.onEvent(new Trade(symbolEURUSD, 500, -1100));

        pnlCalculator.onEvent(new Trade(symbolUSDCHF, 500, -1100));

        pnlCalculator.onEvent(new Trade(symbolEURGBP, 1200, -1000));

        pnlCalculator.onEvent(new Trade(symbolGBPUSD, 1500, -700));

        pnlCalculator.onEvent(new MidPrice(symbolEURGBP, 0.9));
        pnlCalculator.onEvent(new MidPrice(symbolEURUSD, 1.1));
        pnlCalculator.onEvent(new MidPrice(symbolEURCHF, 1.2));
        System.out.println("---------- final rate -----------");
        pnlCalculator.onEvent(new MidPrice(symbolEURJPY, 200));

        System.out.println("---------- final trade -----------");
        pnlCalculator.onEvent(new Trade(symbolGBPUSD, 20, -25));
//        pnlCalculator.publishSignal(EOB_TRADE_KEY);

        System.out.println("---------- change mtm EUR -----------");
        pnlCalculator.onEvent(new MtmInstrument(EUR));
    }
}