package com.fluxtion.example.article.dataoriented1.simple;

import com.fluxtion.example.article.dataoriented1.Events.FoodSaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.FoodTaxRateEvent;
import com.fluxtion.example.article.dataoriented1.Events.TaxLiabilityNotificationThresholdEvent;

public class Main {

    public static void main(String[] args) {
        System.out.print("""
                Running TaxProcessorManualSimple initial tax rate:[15%] initial warning threshold:[200]
                --------------------------------------------------------------------------------------------
                """);
        TaxProcessorManualSimple taxProcessor = new TaxProcessorManualSimple();
        taxProcessor.onEvent(new FoodTaxRateEvent(0.15));
        taxProcessor.onEvent(new TaxLiabilityNotificationThresholdEvent(200));
        taxProcessor.onEvent(new FoodSaleEvent(150));
        taxProcessor.onEvent(new FoodSaleEvent(150));
        taxProcessor.onEvent(new FoodSaleEvent(150));
        taxProcessor.onEvent(new FoodSaleEvent(150));
        taxProcessor.onEvent(new TaxLiabilityNotificationThresholdEvent(100));
        taxProcessor.onEvent(new FoodSaleEvent(150));
        taxProcessor.onEvent(new FoodSaleEvent(150));
    }
}
