/*
 * SPDX-FileCopyrightText: © 2024 Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package com.fluxtion.example.cookbook.pnl.calculator;

import com.fluxtion.example.cookbook.pnl.events.Trade;
import com.fluxtion.runtime.dataflow.aggregate.AggregateFlowFunction;

public class TradeToPositionAggregate implements AggregateFlowFunction<Trade, InstrumentPosMtm, TradeToPositionAggregate> {
    private InstrumentPosMtm instrumentPosMtm = new InstrumentPosMtm();
    private final boolean dealtSide;

    public TradeToPositionAggregate(boolean dealtSide) {
        this.dealtSide = dealtSide;
    }

    public static TradeToPositionAggregate aggregateDealt() {
        return new TradeToPositionAggregate(true);
    }

    public static TradeToPositionAggregate aggregateContra() {
        return new TradeToPositionAggregate(false);
    }

    @Override
    public InstrumentPosMtm aggregate(Trade input) {
        final double previousPosition = instrumentPosMtm.getPosition();
        if (dealtSide) {
            instrumentPosMtm.setInstrument(input.dealtInstrument());
            final double dealtPosition = input.dealtVolume();
            instrumentPosMtm.setPosition(Double.isNaN(previousPosition) ? dealtPosition : dealtPosition + previousPosition);
        } else {
            instrumentPosMtm.setInstrument(input.contraInstrument());
            final double contraPosition = input.contraVolume();
            instrumentPosMtm.setPosition(Double.isNaN(previousPosition) ? contraPosition : contraPosition + previousPosition);
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
