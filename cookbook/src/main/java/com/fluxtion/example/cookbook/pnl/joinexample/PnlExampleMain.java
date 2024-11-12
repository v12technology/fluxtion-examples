/*
 * SPDX-FileCopyrightText: © 2024 Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package com.fluxtion.example.cookbook.pnl.joinexample;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.compiler.builder.dataflow.JoinFlowBuilder;
import com.fluxtion.example.cookbook.pnl.calculator.*;
import com.fluxtion.example.cookbook.pnl.events.MidPrice;
import com.fluxtion.example.cookbook.pnl.events.MtmInstrument;
import com.fluxtion.example.cookbook.pnl.events.Trade;
import com.fluxtion.runtime.EventProcessor;

import static com.fluxtion.example.cookbook.pnl.refdata.RefData.*;


public class PnlExampleMain {

    public static void main(String[] args) {
        var pnlCalculator = Fluxtion.interpret(c -> {
                    var tradeStream = DataFlow.subscribe(Trade.class);
                    var dealtPosition = tradeStream.groupBy(Trade::dealtInstrument, TradeToPosition::aggregateDealt);
                    var contraPosition = tradeStream.groupBy(Trade::contraInstrument, TradeToPosition::aggregateContra);

                    DerivedRateNode derivedRate = new DerivedRateNode();
                    JoinFlowBuilder.outerJoin(dealtPosition, contraPosition, InstrumentPosMtm::merge)
                            .publishTrigger(derivedRate)
                            .mapValues(derivedRate::calculateInstrumentPosMtm)
                            .map(new PnlSummaryCalc()::updateSummary)
                            .console();
                }
        );
        pnlCalculator.init();
        sendEvents(pnlCalculator);
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
