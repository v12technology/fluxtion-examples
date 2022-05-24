package com.fluxtion.example.unplugged.part1;

import lombok.Value;

import java.util.Arrays;
import java.util.List;

@Value
public class Trade {
    public static Trade bought(String instrumentId, double dealtAmount, double contraAmount) {
        return new Trade(
                new AssetAmountTraded(instrumentId.substring(0,3), dealtAmount),
                new AssetAmountTraded(instrumentId.substring(3), -1.0 * contraAmount));
    }

    public static Trade sold(String instrumentId, double dealtAmount, double contraAmount) {
        return new Trade(
                new AssetAmountTraded(instrumentId.substring(0,3), -1.0 * dealtAmount),
                new AssetAmountTraded(instrumentId.substring(3), contraAmount));
    }

    AssetAmountTraded dealt;
    AssetAmountTraded contra;

    public List<AssetAmountTraded> tradeLegs() {
        return Arrays.asList(dealt, contra);
    }

    @Value
    public static class AssetPrice {
        String id;
        double price;
    }

    @Value
    public static class AssetAmountTraded {
        String id;
        double amount;
    }
}
