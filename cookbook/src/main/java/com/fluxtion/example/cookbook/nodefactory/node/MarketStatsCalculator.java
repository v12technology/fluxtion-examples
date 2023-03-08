package com.fluxtion.example.cookbook.nodefactory.node;

import com.fluxtion.runtime.annotations.NoTriggerReference;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.time.FixedRateTrigger;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MarketStatsCalculator {

    @NoTriggerReference
    private final List<SmoothedMarketRate> publishers;
    private final FixedRateTrigger fixedRateTrigger;

    @OnTrigger
    public boolean calculateMarketStats(){
        String statsString = publishers.stream()
                .map(SmoothedMarketRate::formattedResults)
                .collect(Collectors.joining("\n", "-----------------------\n", "\n-----------------------\n"));
        System.out.println(statsString);
        return true;
    }

}
