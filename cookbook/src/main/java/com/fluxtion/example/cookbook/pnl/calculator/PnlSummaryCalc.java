package com.fluxtion.example.cookbook.pnl.calculator;

import com.fluxtion.example.cookbook.pnl.events.PnlSummary;
import com.fluxtion.example.cookbook.pnl.refdata.Instrument;
import com.fluxtion.runtime.dataflow.groupby.GroupBy;

public class PnlSummaryCalc {

    PnlSummary pnlSummary = new PnlSummary();

    public PnlSummary updateSummary(GroupBy<Instrument, InstrumentPosMtm> instrumentMtmGroupBy) {
        pnlSummary.getMtmAssetMap().clear();
        pnlSummary.getMtmAssetMap().putAll(instrumentMtmGroupBy.toMap());
        if(pnlSummary.calcPnl()){
            return pnlSummary;
        }
        return null;
    }
}
