/*
 * SPDX-FileCopyrightText: © 2024 Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package com.fluxtion.example.cookbook.pnl.events;

import com.fluxtion.example.cookbook.pnl.refdata.Instrument;
import com.fluxtion.example.cookbook.pnl.refdata.Symbol;

public record Trade(Symbol symbol, double dealtVolume, double contraVolume) {

    public Instrument getDealtInstrument() {
        return symbol.dealtInstrument();
    }

    public Instrument getContraInstrument() {
        return symbol.contraInstrument();
    }
}
