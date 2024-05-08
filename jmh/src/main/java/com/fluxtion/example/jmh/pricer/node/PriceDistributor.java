package com.fluxtion.example.jmh.pricer.node;

import com.fluxtion.example.jmh.pricer.PriceCalculator;
import com.fluxtion.example.jmh.pricer.PriceLadder;
import com.fluxtion.runtime.annotations.ExportService;

public class PriceDistributor implements @ExportService(propagate = false)PriceCalculator {
    private PriceLadder priceLadder;

    public PriceLadder getPriceLadder() {
        return priceLadder;
    }

    public void setPriceLadder(PriceLadder priceLadder) {
        this.priceLadder = priceLadder;
    }
}
