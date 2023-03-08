package com.fluxtion.example.cookbook.nodefactory.factory;

import com.fluxtion.compiler.builder.factory.NodeFactory;
import com.fluxtion.compiler.builder.factory.NodeRegistry;
import com.fluxtion.example.cookbook.nodefactory.config.MarketStatsCalculatorConfig;
import com.fluxtion.example.cookbook.nodefactory.config.SmoothedMarketRateConfig;
import com.fluxtion.example.cookbook.nodefactory.node.MarketStatsCalculator;
import com.fluxtion.example.cookbook.nodefactory.node.SmoothedMarketRate;
import com.fluxtion.runtime.time.FixedRateTrigger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MarketStatsCalculatorFactory implements NodeFactory<MarketStatsCalculator> {
    @Override
    public MarketStatsCalculator createNode(Map<String, Object> config, NodeRegistry registry) {
        MarketStatsCalculatorConfig statsCalcConfig = (MarketStatsCalculatorConfig) config.get(MarketStatsCalculatorConfig.class.getCanonicalName());
        List<SmoothedMarketRate> throttledPublishers = statsCalcConfig.publisherConfigList().stream()
                .map(cfg -> registry.findOrCreateNode(
                        SmoothedMarketRate.class,
                        Map.of(SmoothedMarketRateConfig.class.getCanonicalName(), cfg),
                        null
                )).collect(Collectors.toList());
        return new MarketStatsCalculator(
                throttledPublishers, FixedRateTrigger.atMillis(1_000 * statsCalcConfig.reportingIntervalSeconds()));
    }
}
