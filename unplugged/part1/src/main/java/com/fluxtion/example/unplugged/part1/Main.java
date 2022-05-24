package com.fluxtion.example.unplugged.part1;

public class Main {
    public static void main(String[] args) {
        TradingCalculator tradingCalculator = new TradingCalculator();
        tradingCalculator.processTrade(Trade.bought("EURUSD", 250d, 130d));
        tradingCalculator.priceUpdate(new PairPrice("EURUSD", 1.5));
        tradingCalculator.processTrade(Trade.sold("EURCHF", 120d, 100d));
        tradingCalculator.reset();
    }
}
