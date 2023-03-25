package com.fluxtion.example.article.dataoriented1.fluxtion;

import com.fluxtion.example.article.dataoriented1.Events.HardwareSaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.HardwareTaxRateEvent;

public class HardwareSaleHandler extends AbstractSaleHandler {

    public void handleSale(HardwareSaleEvent hardwareSaleEvent) {
        totalSales += hardwareSaleEvent.amount();
        calculateTaxLiability();
    }

    public void handleUpdatedTaxRate(HardwareTaxRateEvent hardwareTaxRateEvent) {
        currentTaxRate = hardwareTaxRateEvent.amount();
        calculateTaxLiability();
    }
}
