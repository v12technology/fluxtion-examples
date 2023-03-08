package com.fluxtion.example.cookbook.nodefactory.config;

import java.util.List;

public record MarketStatsCalculatorConfig(List<SmoothedMarketRateConfig> publisherConfigList, int reportingIntervalSeconds) {
}
