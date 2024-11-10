/*
 * SPDX-FileCopyrightText: © 2024 Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package com.fluxtion.server.lib.pnl.calculator;

import com.fluxtion.server.lib.pnl.refdata.Instrument;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class InstrumentPosMtm {
    private String bookName;
    private double tradePnl;
    private Map<Instrument, Double> positionMap = new HashMap<>();
    private Map<Instrument, Double> mtmPositionsMap = new HashMap<>();

    public static InstrumentPosMtm merge(InstrumentPosMtm mtm1, InstrumentPosMtm mtm2) {
        return new InstrumentPosMtm(mtm1).combine(mtm2);
    }

    public InstrumentPosMtm() {}

    public InstrumentPosMtm(InstrumentPosMtm from) {
        if (from != null) {
            this.bookName = from.bookName;
            this.tradePnl = from.tradePnl;
            this.positionMap.putAll(from.positionMap);
            this.mtmPositionsMap.putAll(from.mtmPositionsMap);
        }
    }

    public InstrumentPosMtm combine(InstrumentPosMtm from) {
        if (from != null) {
            this.tradePnl += from.tradePnl;

            from.positionMap.forEach((key, value) -> {
                positionMap.merge(key, value, Double::sum);
            });

            from.mtmPositionsMap.forEach((key, value) -> {
                mtmPositionsMap.merge(key, value, Double::sum);
            });

            this.bookName = bookName == null ? from.bookName : bookName;
        }
        return this;
    }

    public InstrumentPosMtm resetMtm() {
        getMtmPositionsMap().clear();
        tradePnl = Double.NaN;
        return this;
    }

    public double calcTradePnl() {
        return tradePnl = mtmPositionsMap.values().stream().mapToDouble(Double::doubleValue).sum();
    }
}
