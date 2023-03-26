package com.fluxtion.example.article.dataoriented1.simple;

import com.fluxtion.example.article.dataoriented1.Events.SaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.SalesTaxRateEvent;
import com.fluxtion.example.article.dataoriented1.Events.TaxLiabilityNotificationThresholdEvent;

public class TaxProcessorManualSimple {

    private final SaleHandler saleHandler;
    private final TaxLiabilityAlerter taxLiabilityAlerter;

    public TaxProcessorManualSimple() {
        saleHandler = new SaleHandler();
        taxLiabilityAlerter = new TaxLiabilityAlerter(saleHandler);
    }

    public void onEvent(Object event) {
        switch (event) {
            case SaleEvent e -> {
                saleHandler.handleSale(e);
                taxLiabilityAlerter.isTaxLiabilityBreached();
            }
            case SalesTaxRateEvent e -> {
                saleHandler.handleUpdatedTaxRate(e);
                taxLiabilityAlerter.isTaxLiabilityBreached();
            }
            case TaxLiabilityNotificationThresholdEvent e -> {
                taxLiabilityAlerter.thresholdUpdated(e);
            }
            default -> {
            }
        }
    }
}
