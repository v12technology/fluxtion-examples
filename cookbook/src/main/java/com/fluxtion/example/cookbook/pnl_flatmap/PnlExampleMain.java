/*
 * SPDX-FileCopyrightText: © 2024 Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package com.fluxtion.example.cookbook.pnl_flatmap;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.example.cookbook.pnl.flatmapexample.TradeLegToPositionAggregate;
import com.fluxtion.example.cookbook.pnl_flatmap.calculator.*;
import com.fluxtion.example.cookbook.pnl_flatmap.events.MidPrice;
import com.fluxtion.example.cookbook.pnl_flatmap.events.MtmInstrument;
import com.fluxtion.example.cookbook.pnl_flatmap.events.Trade;
import com.fluxtion.example.cookbook.pnl_flatmap.events.TradeLeg;
import com.fluxtion.runtime.EventProcessor;

import static com.fluxtion.example.cookbook.pnl_flatmap.refdata.RefData.*;


public class PnlExampleMain {

    public static void main(String[] args) {
        var pnlCalculator2 = Fluxtion.interpret(c -> {
            DerivedRateNode derivedRate = new DerivedRateNode();
            DataFlow.subscribe(Trade.class)
                    .flatMapFromArray(Trade::tradeLegs)
                    .groupBy(TradeLeg::instrument, TradeLegToPositionAggregate::new)
                    .publishTrigger(derivedRate)
                    .mapValues(derivedRate::calculateInstrumentPosMtm)
                    .map(new PnlSummaryCalc()::updateSummary)
                    .console();
        });
        pnlCalculator2.init();
        sendEvents(pnlCalculator2);

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

        System.out.println("---------- change mtm EUR -----------");
        pnlCalculator.onEvent(new MtmInstrument(EUR));
    }


}
