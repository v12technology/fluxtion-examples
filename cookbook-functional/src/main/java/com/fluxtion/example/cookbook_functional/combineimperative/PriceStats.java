package com.fluxtion.example.cookbook_functional.combineimperative;

import com.fluxtion.example.cookbook_functional.events.MarketUpdate;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.dataflow.FlowSupplier;

public class PriceStats {

    private final FlowSupplier<MarketUpdate> marketUpdateEventStream;
    private double previousHigh;

    public PriceStats(FlowSupplier<MarketUpdate> marketUpdateEventStream) {
        this.marketUpdateEventStream = marketUpdateEventStream;
    }

    @OnTrigger
    public boolean marketUpdated() {
        MarketUpdate marketUpdate = marketUpdateEventStream.get();
        boolean updated = marketUpdate.mid() > previousHigh;
        previousHigh = Math.max(marketUpdate.mid(), previousHigh);
        if (updated) {
            System.out.println("new high price:" + marketUpdate);
        }
        return updated;
    }

    @Initialise
    public void init() {
        previousHigh = 0;
    }
}
