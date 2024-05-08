package com.fluxtion.example.jmh.pricer;

public interface PriceLadderConsumer {
    default boolean newPriceLadder(PriceLadder priceLadder) {
        return false;
    }
}
