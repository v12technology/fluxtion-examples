package com.fluxtion.example.cookbook.nodefactory.event;

public record MarketUpdate(String symbol, double midPrice) {
    public String filterString() {
        return symbol;
    }
}
