package com.fluxtion.example.unplugged.part1;

/**
 * Main entry point for running the TradingCalculator with a sample set of data.
 *
 */
public class Main {
    public static void main(String[] args) {
        TradingCalculator tradingCalculator = new TradingCalculator();
        //add listeners for output
        tradingCalculator.markToMarketConsumer(
                m -> System.out.println("Asset mark to market   :" + m));
        tradingCalculator.positionsConsumer(
                m -> System.out.println("Asset positions        :" + m));
        tradingCalculator.profitConsumer(
                d -> System.out.println("Total trading profit   :" + d));
        //send trades and rates
        tradingCalculator.processTrade(Trade.bought("EURUSD", 250d, 130d));
        tradingCalculator.processTrade(Trade.bought("EURUSD", 250d, 130d));
        tradingCalculator.processTrade(Trade.sold("EURCHF", 120d, 100d));
        tradingCalculator.priceUpdate(new PairPrice("EURUSD", 1.5));
        tradingCalculator.priceUpdate(new PairPrice("USDCHF", 1.2));
        tradingCalculator.processTrade(Trade.bought("GBPJPY", 20d, 26000d));
        tradingCalculator.priceUpdate(new PairPrice("EURUSD", 1.0));
        //reset
        tradingCalculator.reset();
        //trade batch after reset
        tradingCalculator.priceUpdate(new PairPrice("EURUSD", 1.5));
        tradingCalculator.priceUpdate(new PairPrice("GBPUSD", 1.25));
        tradingCalculator.priceUpdate(new PairPrice("USDJPY", 202));
        tradingCalculator.priceUpdate(new PairPrice("USDCHF", 1.25));
        tradingCalculator.processTrades(
                Trade.bought("EURUSD", 20d, 11d),
                Trade.bought("GBPJPY", 20d, 26000d),
                Trade.sold("EURCHF", 120d, 100d)
        );
    }
}
