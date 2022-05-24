package com.fluxtion.example.unplugged.part1;

public class Main {
    public static void main(String[] args) {
        TradingCalculator tradingCalculator = new TradingCalculator();
        //add listener
        tradingCalculator.markToMarketListener(m -> System.out.println("Asset mark to market\t:" + m));
        tradingCalculator.positionsListener(m -> System.out.println("Asset positions\t\t\t:" + m));
        //send trades and rates
        tradingCalculator.processTrade(Trade.bought("EURUSD", 250d, 130d));
        tradingCalculator.priceUpdate(new PairPrice("EURUSD", 1.5));
        tradingCalculator.processTrade(Trade.sold("EURCHF", 120d, 100d));
        tradingCalculator.priceUpdate(new PairPrice("USDCHF", 1.2));
        //reset
        System.out.println("\nresetting:");
        tradingCalculator.reset();
    }
}