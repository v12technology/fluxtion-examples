package com.fluxtion.example.cookbook.pnl.calculator;

import com.fluxtion.example.cookbook.pnl.events.TradeLeg;
import com.fluxtion.runtime.dataflow.aggregate.function.AbstractAggregateFlowFunction;

public class TradeLegToPositionAggregate extends AbstractAggregateFlowFunction<TradeLeg, InstrumentPosMtm> {

    @Override
    protected InstrumentPosMtm calculateAggregate(TradeLeg tradeLeg, InstrumentPosMtm instrumentPosMtm) {
        instrumentPosMtm = instrumentPosMtm == null ? new InstrumentPosMtm() : instrumentPosMtm;
        instrumentPosMtm.add(tradeLeg);
        return instrumentPosMtm;
    }

    @Override
    protected InstrumentPosMtm resetAction(InstrumentPosMtm instrumentPosMtm) {
        return new InstrumentPosMtm();
    }
}
