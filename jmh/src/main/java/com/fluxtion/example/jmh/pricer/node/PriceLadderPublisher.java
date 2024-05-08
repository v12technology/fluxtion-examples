package com.fluxtion.example.jmh.pricer.node;

import com.fluxtion.example.jmh.pricer.PriceCalculator;
import com.fluxtion.example.jmh.pricer.PriceLadder;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnTrigger;

public class PriceLadderPublisher implements @ExportService(propagate = false)PriceCalculator {
    
    private final LevelsCalculator LevelsCalculator;
    private PriceLadder skewedPriceLadder;
    private PriceDistributor priceDistributor;

    public PriceLadderPublisher(LevelsCalculator LevelsCalculator) {
        this.LevelsCalculator = LevelsCalculator;
    }

    public PriceLadderPublisher() {
        this(new LevelsCalculator());
    }

    @Override
    public void setPriceDistributor(PriceDistributor priceDistributor) {
        this.priceDistributor = priceDistributor;
    }

    @OnTrigger
    public boolean publishPriceLadder(){
        priceDistributor.setPriceLadder(LevelsCalculator.getLevelAdjustedPriceLadder());
        return true;
    }

}
