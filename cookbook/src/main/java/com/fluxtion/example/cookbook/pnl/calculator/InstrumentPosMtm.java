/*
 * SPDX-FileCopyrightText: © 2024 Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package com.fluxtion.example.cookbook.pnl.calculator;

import com.fluxtion.example.cookbook.pnl.events.TradeLeg;
import com.fluxtion.example.cookbook.pnl.refdata.Instrument;
import lombok.Data;

@Data
public class InstrumentPosMtm {
    private Instrument instrument;
    private double position = 0;
    private double mtmPosition = 0;

    public static InstrumentPosMtm merge(InstrumentPosMtm mtm1, InstrumentPosMtm mtm2) {
        return new InstrumentPosMtm(mtm1).combine(mtm2);
    }

    public InstrumentPosMtm() {
    }

    public InstrumentPosMtm add(TradeLeg from) {
        if (from != null) {
            this.instrument = from.instrument();
            this.position += from.volume();
        }
        return this;
    }

    public InstrumentPosMtm(InstrumentPosMtm from) {
        if (from != null) {
            this.instrument = from.instrument;
            this.position = from.position;
            this.mtmPosition = from.mtmPosition;
        }
    }

    public InstrumentPosMtm combine(InstrumentPosMtm from) {
        if (from != null) {
            this.position += from.position;
            this.mtmPosition += from.mtmPosition;
            this.instrument = instrument == null ? from.instrument : instrument;
        }
        return this;
    }

    public InstrumentPosMtm resetMtm() {
        mtmPosition = 0;
        return this;
    }
}
