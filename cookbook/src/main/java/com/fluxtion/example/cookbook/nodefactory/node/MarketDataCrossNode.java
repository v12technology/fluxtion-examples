package com.fluxtion.example.cookbook.nodefactory.node;

import com.fluxtion.example.cookbook.nodefactory.MarketDataSupplier;
import com.fluxtion.example.cookbook.nodefactory.event.MarketUpdate;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.annotations.builder.AssignToField;
import com.fluxtion.runtime.node.NamedNode;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class MarketDataCrossNode implements NamedNode, MarketDataSupplier {

    @EqualsAndHashCode.Include
    @ToString.Include
    private final String symbol;
    @AssignToField("rate1")
    private final MarketDataNode rate1;
    @AssignToField("rate2")
    private final MarketDataNode rate2;
    private transient double midPrice;

    @OnTrigger
    public boolean calculateCrossRate() {
        double newRate = rate2.midPrice() / rate1.midPrice() ;
        boolean updated = !(newRate == midPrice || Double.isNaN(newRate) && Double.isNaN(midPrice));
        midPrice = newRate;
        return !updated;
    }

    @Override
    public String getName() {
        return "crossMarketDataNode_" + symbol;
    }

    @Initialise
    public void init() {
        midPrice = Double.NaN;
    }

    @Override
    public MarketUpdate getMarketUpdate() {
        return new MarketUpdate(symbol, midPrice);
    }

    @Override
    public double midPrice() {
        return midPrice;
    }


}
