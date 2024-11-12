/*
 * SPDX-FileCopyrightText: © 2024 Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package com.fluxtion.example.cookbook.pnl_flatmap.events;

import com.fluxtion.example.cookbook.pnl_flatmap.refdata.Instrument;
import com.fluxtion.example.cookbook.pnl_flatmap.refdata.Symbol;

public record Trade(Symbol symbol, double dealtVolume, double contraVolume) {

    public Instrument dealtInstrument() {
        return symbol.dealtInstrument();
    }

    public Instrument contraInstrument() {
        return symbol.contraInstrument();
    }

    public TradeLeg[] tradeLegs() {
        return new TradeLeg[]{new TradeLeg(dealtInstrument(), dealtVolume), new TradeLeg(contraInstrument(), contraVolume)};
    }
}
