package com.fluxtion.example.cookbook.ml.linearregression.api;

public interface HouseSalesMonitor {
    void houseSold(HouseSaleDetails soldHouse);

    void removeAllSales();
}
