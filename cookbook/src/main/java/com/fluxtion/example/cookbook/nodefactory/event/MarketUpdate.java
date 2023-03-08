package com.fluxtion.example.cookbook.nodefactory.event;

import com.fluxtion.runtime.event.Event;

public record MarketUpdate(String symbol, double midPrice) implements Event {
    public String filterString() {
        return symbol;
    }
}
