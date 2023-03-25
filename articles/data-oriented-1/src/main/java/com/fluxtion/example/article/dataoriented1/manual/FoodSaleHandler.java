package com.fluxtion.example.article.dataoriented1.manual;

import com.fluxtion.example.article.dataoriented1.Events.FoodSaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.FoodTaxRateEvent;
import com.fluxtion.runtime.annotations.OnEventHandler;

public class FoodSaleHandler extends AbstractSaleHandler {

    @OnEventHandler
    public boolean handleSale(FoodSaleEvent foodSaleEvent) {
        totalSales += foodSaleEvent.amount();
        calculateTaxLiability();
        return true;
    }

    @OnEventHandler
    public boolean handleUpdatedTaxRate(FoodTaxRateEvent foodTaxRateEvent) {
        currentTaxRate = foodTaxRateEvent.amount();
        calculateTaxLiability();
        return true;
    }
}
