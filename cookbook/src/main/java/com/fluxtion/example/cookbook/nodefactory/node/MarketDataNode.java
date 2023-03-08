package com.fluxtion.example.cookbook.nodefactory.node;

import com.fluxtion.example.cookbook.nodefactory.MarketDataSupplier;
import com.fluxtion.example.cookbook.nodefactory.event.MarketUpdate;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.node.NamedNode;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class MarketDataNode implements NamedNode, MarketDataSupplier {

    @EqualsAndHashCode.Include
    @ToString.Include
    private final String symbol;
    private transient MarketUpdate marketUpdate;

    @OnEventHandler(filterVariable = "symbol")
    public boolean marketUpdate(MarketUpdate marketUpdate) {
        this.marketUpdate = marketUpdate;
        return true;
    }

    @Override
    public MarketUpdate getMarketUpdate() {
        return marketUpdate;
    }

    @Override
    public double midPrice(){
        return marketUpdate == null ? Double.NaN : marketUpdate.midPrice();
    }


    @Override
    public String getName() {
        return "marketDataNode_" + symbol;
    }
}
