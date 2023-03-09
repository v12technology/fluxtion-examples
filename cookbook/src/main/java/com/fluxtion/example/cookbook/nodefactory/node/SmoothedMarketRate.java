package com.fluxtion.example.cookbook.nodefactory.node;

import com.fluxtion.example.cookbook.nodefactory.MarketDataSupplier;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.NoTriggerReference;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.node.NamedNode;
import com.fluxtion.runtime.time.FixedRateTrigger;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

@RequiredArgsConstructor
public class SmoothedMarketRate implements NamedNode {
    private final String name;
    private final FixedRateTrigger fixedRateTrigger;
    @NoTriggerReference
    private final MarketDataSupplier marketDataNode;
    private final int windowSize;
    private final transient DescriptiveStatistics stats = new DescriptiveStatistics();

    @OnTrigger
    public boolean calculateSmoothedRate() {
        stats.addValue(marketDataNode.midPrice());
        return true;
    }

    public double smoothedRate() {
        return stats.getMean();
    }

    @Initialise
    public void init() {
        stats.setWindowSize(windowSize);
    }

    @Override
    public String getName() {
        return name;
    }

    public String formattedResults() {
        return "%s, %.4f".formatted(getName(), smoothedRate());
    }
}
