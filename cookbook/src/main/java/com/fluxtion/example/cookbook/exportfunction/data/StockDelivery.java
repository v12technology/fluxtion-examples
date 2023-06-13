package com.fluxtion.example.cookbook.exportfunction.data;

public record StockDelivery<T extends StockTypes>(boolean removingStock, int amount, double costPerItem) { }
