package com.fluxtion.example.article.dataoriented1.fluxtion;

import com.fluxtion.example.article.dataoriented1.Events.HardwareSaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.HardwareTaxRateEvent;
import com.fluxtion.runtime.annotations.OnEventHandler;

public class HardwareSaleHandler extends AbstractSaleHandler {

    @OnEventHandler
    public boolean handleSale(HardwareSaleEvent hardwareSaleEvent) {
        totalSales += hardwareSaleEvent.amount();
        calculateTaxLiability();
        return true;
    }

    @OnEventHandler
    public boolean handleUpdatedTaxRate(HardwareTaxRateEvent hardwareTaxRateEvent) {
        currentTaxRate = hardwareTaxRateEvent.amount();
        calculateTaxLiability();
        return true;
    }
}
