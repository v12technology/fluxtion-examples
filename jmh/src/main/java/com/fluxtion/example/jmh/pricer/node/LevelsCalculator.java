package com.fluxtion.example.jmh.pricer.node;

import com.fluxtion.example.jmh.pricer.PriceCalculator;
import com.fluxtion.example.jmh.pricer.PriceLadder;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnTrigger;

public class LevelsCalculator implements @ExportService(propagate = false)PriceCalculator {
    
    private final SkewCalculator SkewCalculator;
    private PriceLadder skewedPriceLadder;
    private int maxLevels;

    public LevelsCalculator(SkewCalculator SkewCalculator) {
        this.SkewCalculator = SkewCalculator;
    }

    public LevelsCalculator() {
        this(new SkewCalculator());
    }

    @Override
    public void setLevels(int maxLevels) {
        this.maxLevels = maxLevels;
    }

    @OnTrigger
    public boolean calculateLevelsForLadder(){
        PriceLadder priceLadder = SkewCalculator.getSkewedPriceLadder();

        int[] bidPrices = priceLadder.getBidPrices();
        int[] bidSizes = priceLadder.getBidSizes();
        for (int i = maxLevels, bidPricesLength = bidPrices.length; i < bidPricesLength; i++) {
            bidPrices[i] = 0;
            bidSizes[i] = 0;
        }

        int[] askPrices = priceLadder.getAskPrices();
        int[] askSizes = priceLadder.getAskSizes();
        for (int i = maxLevels, askPricesLength = askPrices.length; i < askPricesLength; i++) {
            askPrices[i] = 0;
            askSizes[i] = 0;
        }

        return true;
    }

    public PriceLadder getLevelAdjustedPriceLadder() {
        return SkewCalculator.getSkewedPriceLadder();
    }
}
