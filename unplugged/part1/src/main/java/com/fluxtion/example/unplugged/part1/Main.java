package com.fluxtion.example.unplugged.part1;

public class Main {
    public static void main(String[] args) {
        TradingCalculator tradingCalculator = new TradingCalculator();
        //add listener
        tradingCalculator.markToMarketListener(m -> System.out.println("Asset mark to market\t:" + m));
        tradingCalculator.positionsListener(m -> System.out.println(   "Asset positions\t\t\t:" + m));
        tradingCalculator.profitListener(d -> System.out.println(      "Total trading profit\t:" + d));
        //send trades and rates
        System.out.println("sending trades and rates:");
        tradingCalculator.priceUpdate(new PairPrice("EURUSD", 1.5));
        tradingCalculator.processTrade(Trade.bought("EURUSD", 250d, 130d));
        tradingCalculator.processTrade(Trade.sold("EURCHF", 120d, 100d));
        tradingCalculator.priceUpdate(new PairPrice("USDCHF", 1.2));

        System.out.println("\nset eur=usd rate parity:");
        tradingCalculator.priceUpdate(new PairPrice("EURUSD", 1.0));
        //reset
        System.out.println("\nresetting:");
        tradingCalculator.reset();

        //more trades
        System.out.println("\nsending trades and rates:");
        tradingCalculator.priceUpdate(new PairPrice("EURUSD", 1.5));
        tradingCalculator.priceUpdate(new PairPrice("GBPUSD", 1.25));
        tradingCalculator.priceUpdate(new PairPrice("USDJPY", 202));
        tradingCalculator.processTrade(Trade.bought("EURUSD", 20d, 11d));
        tradingCalculator.processTrade(Trade.bought("GBPJPY", 20d, 26000d));
    }
}
