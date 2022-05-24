package com.fluxtion.example.unplugged.part1;

import java.util.Arrays;
import java.util.List;

public record Trade(TradeLeg dealt, TradeLeg contra) {
    public static Trade bought(String instrumentId, double dealtAmount, double contraAmount) {
        return new Trade(
                new TradeLeg(instrumentId.substring(0, 3), dealtAmount),
                new TradeLeg(instrumentId.substring(3), -1.0 * contraAmount));
    }

    public static Trade sold(String instrumentId, double dealtAmount, double contraAmount) {
        return new Trade(
                new TradeLeg(instrumentId.substring(0, 3), -1.0 * dealtAmount),
                new TradeLeg(instrumentId.substring(3), contraAmount));
    }


    public List<TradeLeg> tradeLegs() {
        return Arrays.asList(dealt, contra);
    }

    public record AssetPrice(String id, double price) {
    }

    public record TradeLeg(String id, double amount) {
    }
}
