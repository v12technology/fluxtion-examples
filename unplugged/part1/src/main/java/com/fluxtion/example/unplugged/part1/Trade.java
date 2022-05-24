package com.fluxtion.example.unplugged.part1;

import lombok.Value;

import java.util.Arrays;
import java.util.List;

@Value
public class Trade {
    TradeLeg dealt;
    TradeLeg contra;
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

    @Value
    public static class AssetPrice {
        String id;
        double price;
    }

    @Value
    public static class TradeLeg {
        String id;
        double amount;
    }
}
