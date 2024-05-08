package com.fluxtion.example.jmh.pricer;

import java.util.Arrays;

public class PriceLadder {
    private final int[] bidSizes = new int[5];
    private final int[] bidPrices = new int[5];
    private final int[] askSizes = new int[5];
    private final int[] askPrices = new int[5];

    public PriceLadder() {
        Arrays.fill(bidSizes, 0);
        Arrays.fill(bidPrices, 0);
        Arrays.fill(askSizes, 0);
        Arrays.fill(askPrices, 0);
    }

    public int[] getBidSizes() {
        return bidSizes;
    }

    public int[] getBidPrices() {
        return bidPrices;
    }

    public int[] getAskSizes() {
        return askSizes;
    }

    public int[] getAskPrices() {
        return askPrices;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("PriceLadder{ bids=[");
        for (int i = 0; i < bidPrices.length; i++) {
            int bidPrice = bidPrices[i];
            builder.append(bidSizes[i]).append("@").append(bidPrice).append(", ");
        }
        builder.setLength(builder.length() - 2);
        builder.append("] asks=[");
        for (int i = 0; i < askPrices.length; i++) {
            int askPrice = askPrices[i];
            builder.append(askSizes[i]).append("@").append(askPrice).append(", ");
        }
        builder.setLength(builder.length() - 2);
        builder.append("] }");

        return builder.toString();
    }
}
