package com.fluxtion.example.article.dataoriented1.simple;

import com.fluxtion.example.article.dataoriented1.Events.SaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.SalesTaxRateEvent;

public class SaleHandler {

    protected double totalSales;
    protected double currentTaxRate = Double.NaN;
    protected double taxLiability = Double.NaN;

    protected void calculateTaxLiability() {
        taxLiability = totalSales * currentTaxRate;
    }

    public double taxLiability() {
        return taxLiability;
    }

    public void handleSale(SaleEvent saleEvent) {
        totalSales += saleEvent.amount();
        calculateTaxLiability();
    }

    public void handleUpdatedTaxRate(SalesTaxRateEvent foodTaxRateEvent) {
        currentTaxRate = foodTaxRateEvent.amount();
        calculateTaxLiability();
    }
}
