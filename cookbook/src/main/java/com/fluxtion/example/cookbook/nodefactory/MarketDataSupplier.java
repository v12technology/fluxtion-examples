package com.fluxtion.example.cookbook.nodefactory;

import com.fluxtion.example.cookbook.nodefactory.event.MarketUpdate;

public interface MarketDataSupplier {

    MarketUpdate getMarketUpdate();

    double midPrice();
}
