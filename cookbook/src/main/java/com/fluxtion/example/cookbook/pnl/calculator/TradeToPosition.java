/*
 * SPDX-FileCopyrightText: © 2024 Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package com.fluxtion.example.cookbook.pnl.calculator;

import com.fluxtion.runtime.dataflow.aggregate.AggregateFlowFunction;
import com.fluxtion.example.cookbook.pnl.events.Trade;

public class TradeToPosition implements AggregateFlowFunction<Trade, InstrumentPosMtm, TradeToPosition> {
    private InstrumentPosMtm instrumentPosMtm = new InstrumentPosMtm();
    private final boolean dealtSide;

    public TradeToPosition(boolean dealtSide) {
        this.dealtSide = dealtSide;
    }

    public static TradeToPosition aggregateDealtPosition() {
        return new TradeToPosition(true);
    }

    public static TradeToPosition aggregateContraPosition() {
        return new TradeToPosition(false);
    }

    @Override
    public InstrumentPosMtm aggregate(Trade input) {
        if (dealtSide) {
            instrumentPosMtm.setBookName(input.getDealtInstrument().instrumentName());
            final double dealtPosition = input.dealtVolume();
            instrumentPosMtm.getPositionMap().compute(input.getDealtInstrument(), (s, d) -> d == null ? dealtPosition : d + dealtPosition);
        } else {
            instrumentPosMtm.setBookName(input.getContraInstrument().instrumentName());
            final double contraPosition = input.contraVolume();
            instrumentPosMtm.getPositionMap().compute(input.getContraInstrument(), (s, d) -> d == null ? contraPosition : d + contraPosition);
        }
        return instrumentPosMtm;
    }

    @Override
    public InstrumentPosMtm get() {
        return instrumentPosMtm;
    }

    @Override
    public InstrumentPosMtm reset() {
        instrumentPosMtm = new InstrumentPosMtm();
        return instrumentPosMtm;
    }

}
