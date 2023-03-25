package com.fluxtion.example.article.dataoriented1.fluxtion;

import com.fluxtion.example.article.dataoriented1.Events.FoodSaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.FoodTaxRateEvent;

public class FoodSaleHandler extends AbstractSaleHandler{

    public void handleSale(FoodSaleEvent foodSaleEvent) {
        totalSales += foodSaleEvent.amount();
        calculateTaxLiability();
    }

    public void handleUpdatedTaxRate(FoodTaxRateEvent foodTaxRateEvent) {
        currentTaxRate = foodTaxRateEvent.amount();
        calculateTaxLiability();
    }
}
