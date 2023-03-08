package com.fluxtion.example.cookbook.nodefactory.factory;

import com.fluxtion.compiler.builder.factory.NodeFactory;
import com.fluxtion.compiler.builder.factory.NodeRegistry;
import com.fluxtion.example.cookbook.nodefactory.MarketDataSupplier;
import com.fluxtion.example.cookbook.nodefactory.config.MarketDataSupplierConfig;
import com.fluxtion.example.cookbook.nodefactory.config.SmoothedMarketRateConfig;
import com.fluxtion.example.cookbook.nodefactory.node.SmoothedMarketRate;
import com.fluxtion.runtime.time.FixedRateTrigger;

import java.util.Map;

public class SmoothedMarketRateFactory implements NodeFactory<SmoothedMarketRate> {
    @Override
    public SmoothedMarketRate createNode(Map<String, Object> config, NodeRegistry registry) {
        SmoothedMarketRateConfig publisherConfig = (SmoothedMarketRateConfig) config.get(
                SmoothedMarketRateConfig.class.getCanonicalName());

        return new SmoothedMarketRate(
                publisherConfig.name(),
                FixedRateTrigger.atMillis(publisherConfig.publishRate()),
                registry.findOrCreateNode(
                        MarketDataSupplier.class,
                        Map.of(MarketDataSupplierConfig.class.getCanonicalName(), publisherConfig.marketDataSupplier()),
                        null),
                publisherConfig.windowSize());
    }
}
