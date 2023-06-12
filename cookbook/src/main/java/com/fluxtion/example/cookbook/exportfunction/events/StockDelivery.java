package com.fluxtion.example.cookbook.exportfunction.events;

import com.fluxtion.example.cookbook.exportfunction.events.StockTypes;

public record StockDelivery<T extends StockTypes>(boolean removingStock, int amount, double costPerItem) { }
