package com.fluxtion.example.jmh.pricer.node;

import com.fluxtion.example.jmh.pricer.PriceLadder;
import com.fluxtion.example.jmh.pricer.PriceLadderConsumer;
import com.fluxtion.runtime.annotations.ExportService;

public class MidCalculator implements @ExportService PriceLadderConsumer {

    private int mid;
    private PriceLadder priceLadder;

    @Override
    public boolean newPriceLadder(PriceLadder priceLadder) {
        mid = (priceLadder.getAskPrices()[0] + priceLadder.getBidPrices()[0]) / 2;
        this.priceLadder = priceLadder;
        //If the JMH results seems quick un-comment the lines below to see the effect on the results
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return true;
    }

    public int getMid() {
        return mid;
    }

    public PriceLadder getPriceLadder() {
        return priceLadder;
    }
}
