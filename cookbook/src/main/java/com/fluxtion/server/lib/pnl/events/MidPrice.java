/*
 * SPDX-FileCopyrightText: © 2024 Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package com.fluxtion.server.lib.pnl.events;

import com.fluxtion.server.lib.pnl.refdata.Instrument;
import com.fluxtion.server.lib.pnl.refdata.Symbol;

public record MidPrice(Symbol symbol, double rate) {
    public Instrument dealtInstrument() {
        return symbol().dealtInstrument();
    }

    public Instrument contraInstrument() {
        return symbol().contraInstrument();
    }

    public double getRateForInstrument(Instrument ccy) {
        if (symbol.dealtInstrument().equals(ccy)) {
            return 1 / rate;
        } else if (symbol.contraInstrument().equals(ccy)) {
            return rate;
        }
        return Double.NaN;
    }

    public Instrument getOppositeInstrument(Instrument searchCcy) {
        if (symbol.dealtInstrument().equals(searchCcy)) {
            return symbol.contraInstrument();
        } else if (symbol.contraInstrument().equals(searchCcy)) {
            return symbol.dealtInstrument();
        }
        return null;
    }
}