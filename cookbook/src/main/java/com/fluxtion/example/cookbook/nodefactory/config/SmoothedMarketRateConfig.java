package com.fluxtion.example.cookbook.nodefactory.config;

public record SmoothedMarketRateConfig(
        int publishRate,
        int windowSize,
        String name,
        MarketDataSupplierConfig marketDataSupplier) {
}
