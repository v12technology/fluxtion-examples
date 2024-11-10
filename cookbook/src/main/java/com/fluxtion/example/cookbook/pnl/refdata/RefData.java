package com.fluxtion.example.cookbook.pnl.refdata;

public interface RefData {

     Instrument EUR = new Instrument("EUR");
     Instrument USD = new Instrument("USD");
     Instrument CHF = new Instrument("CHF");
     Instrument JPY = new Instrument("JPY");
     Instrument GBP = new Instrument("GBP");
     Symbol symbolEURUSD = new Symbol("EURUSD", EUR, USD);
     Symbol symbolEURCHF = new Symbol("EURCHF", EUR, CHF);
     Symbol symbolUSDCHF = new Symbol("USDCHF", USD, CHF);
     Symbol symbolCHFUSD = new Symbol("CHFUSD", CHF, USD);
     Symbol symbolEURJPY = new Symbol("EURJPY", EUR, JPY);
     Symbol symbolUSDJPY = new Symbol("USDJPY", USD, JPY);
     Symbol symbolGBPUSD = new Symbol("GBPUSD", GBP, USD);
     Symbol symbolEURGBP = new Symbol("EURGBP", EUR, GBP);
}
