package com.fluxtion.example.jmh.pricer;

import com.fluxtion.example.jmh.pricer.node.PriceDistributor;

public interface PriceCalculator {
    default void setSkew(int skew){}
    default void setLevels(int maxLevels){}
    default void setPriceDistributor(PriceDistributor priceDistributor){}
}
