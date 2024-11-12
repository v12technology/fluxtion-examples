/*
 * SPDX-FileCopyrightText: © 2024 Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package com.fluxtion.example.cookbook.pnl_flatmap.calculator;

import com.fluxtion.example.cookbook.pnl_flatmap.events.TradeLeg;
import com.fluxtion.example.cookbook.pnl_flatmap.refdata.Instrument;
import lombok.Data;

@Data
public class InstrumentPosMtm {
    private Instrument instrument;
    private double position = 0;
    private double mtmPosition = 0;

    public InstrumentPosMtm add(TradeLeg from) {
        if (from != null) {
            this.instrument = from.instrument();
            this.position += from.volume();
        }
        return this;
    }

    public InstrumentPosMtm resetMtm() {
        mtmPosition = 0;
        return this;
    }
}
