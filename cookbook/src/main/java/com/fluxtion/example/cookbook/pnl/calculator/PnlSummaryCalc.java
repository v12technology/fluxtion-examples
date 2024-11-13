package com.fluxtion.example.cookbook.pnl.calculator;

import com.fluxtion.example.cookbook.pnl.events.PnlSummary;
import com.fluxtion.example.cookbook.pnl.flatmapexample.PnlExampleMain;
import com.fluxtion.example.cookbook.pnl.refdata.Instrument;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.annotations.builder.FluxtionIgnore;
import com.fluxtion.runtime.dataflow.groupby.GroupBy;
import com.fluxtion.runtime.event.Signal;
import lombok.Getter;

public class PnlSummaryCalc {

    @Getter
    private final MtMRateCalculator mtMRateCalculator;
    @FluxtionIgnore
    private final PnlSummary pnlSummary = new PnlSummary();

    public PnlSummaryCalc(MtMRateCalculator mtMRateCalculator) {
        this.mtMRateCalculator = mtMRateCalculator;
    }

    public PnlSummaryCalc() {
        this.mtMRateCalculator = new MtMRateCalculator();
    }

    @OnEventHandler(filterString = PnlExampleMain.EOB_TRADE_KEY)
    public boolean eobTrigger(Signal<String> eobSignal){
        return true;
    }

    public PnlSummary updateSummary(GroupBy<Instrument, InstrumentPosMtm> instrumentMtmGroupBy) {
        pnlSummary.setMtmInstrument(mtMRateCalculator.getMtmInstrument());
        pnlSummary.getMtmAssetMap().clear();
        pnlSummary.getMtmAssetMap().putAll(instrumentMtmGroupBy.toMap());
        if(pnlSummary.calcPnl()){
            return pnlSummary;
        }
        return null;
    }

    public PnlSummary calcMtmAndUpdateSummary(GroupBy<Instrument, InstrumentPosMtm> instrumentMtmGroupBy) {
        instrumentMtmGroupBy.toMap().values().forEach(mtMRateCalculator::calculateInstrumentPosMtm);
        pnlSummary.setMtmInstrument(mtMRateCalculator.getMtmInstrument());
        pnlSummary.getMtmAssetMap().clear();
        pnlSummary.getMtmAssetMap().putAll(instrumentMtmGroupBy.toMap());
        if(pnlSummary.calcPnl()){
            return pnlSummary;
        }
        return null;
    }

    @OnTrigger
    public boolean trigger() {
        return true;
    }
}
