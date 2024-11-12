package com.fluxtion.example.cookbook.pnl.flatmapexample;

import com.fluxtion.example.cookbook.pnl_flatmap.calculator.InstrumentPosMtm;
import com.fluxtion.example.cookbook.pnl_flatmap.events.TradeLeg;
import com.fluxtion.runtime.dataflow.aggregate.AggregateFlowFunction;

public class TradeLegToPositionAggregate implements AggregateFlowFunction<TradeLeg, InstrumentPosMtm, TradeLegToPositionAggregate> {
    private InstrumentPosMtm instrumentPosMtm = new InstrumentPosMtm();

    @Override
    public InstrumentPosMtm get() {
        return instrumentPosMtm;
    }

    @Override
    public InstrumentPosMtm aggregate(TradeLeg input) {
        instrumentPosMtm.add(input);
        return instrumentPosMtm;
    }

    @Override
    public InstrumentPosMtm reset() {
        instrumentPosMtm = new InstrumentPosMtm();
        return instrumentPosMtm;
    }
}
