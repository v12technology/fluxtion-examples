package com.fluxtion.example.cookbook.pnl_flatmap.calculator;

import com.fluxtion.example.cookbook.pnl_flatmap.events.MtmInstrument;
import com.fluxtion.example.cookbook.pnl_flatmap.events.PnlSummary;
import com.fluxtion.example.cookbook.pnl_flatmap.refdata.Instrument;
import com.fluxtion.example.cookbook.pnl_flatmap.refdata.RefData;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.dataflow.groupby.GroupBy;

public class PnlSummaryCalc {

    PnlSummary pnlSummary = new PnlSummary();
    private Instrument mtmInstrument = RefData.USD;

    @OnEventHandler
    public boolean updateMtmInstrument(MtmInstrument mtmInstrumentUpdate) {
        boolean change = mtmInstrument != mtmInstrumentUpdate.instrument();
        if (change) {
            mtmInstrument = mtmInstrumentUpdate.instrument();
        }
        return change;
    }

    public PnlSummary updateSummary(GroupBy<Instrument, InstrumentPosMtm> instrumentMtmGroupBy) {
        pnlSummary.setMtmInstrument(mtmInstrument);
        pnlSummary.getMtmAssetMap().clear();
        pnlSummary.getMtmAssetMap().putAll(instrumentMtmGroupBy.toMap());
        if(pnlSummary.calcPnl()){
            return pnlSummary;
        }
        return null;
    }
}
