package com.fluxtion.example.cookbook.pnl_flatmap.events;

import com.fluxtion.example.cookbook.pnl_flatmap.calculator.InstrumentPosMtm;
import com.fluxtion.example.cookbook.pnl_flatmap.refdata.Instrument;
import com.fluxtion.example.cookbook.pnl_flatmap.refdata.RefData;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class PnlSummary {
    private Instrument mtmInstrument = RefData.USD;
    private double pnl;
    private Map<Instrument, InstrumentPosMtm> mtmAssetMap = new HashMap<>();

    public boolean calcPnl() {
        double oldVal = this.pnl;
        this.pnl = mtmAssetMap.values().stream().mapToDouble(InstrumentPosMtm::getMtmPosition).sum();
        return Double.isNaN(oldVal) | oldVal != pnl;
    }

    @Override
    public String toString() {
        return "PnlSummary{" +
               "\n\tmtmInstrument=" + mtmInstrument.instrumentName() +
               "\n\tpnl=" + pnl +
               "\n\t" + mtmAssetMap.values().stream()
                       .map(i -> i.getInstrument().instrumentName() + " pos:" + i.getPosition() + " mtmPos:" + i.getMtmPosition())
                       .collect(Collectors.joining("\n\t")) +
               '}';
    }
}
