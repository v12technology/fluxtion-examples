package com.fluxtion.example.article.dataoriented1.manual;


import com.fluxtion.example.article.dataoriented1.Events.BookSaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.BookTaxRateEvent;
import com.fluxtion.example.article.dataoriented1.Events.FoodSaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.FoodTaxRateEvent;
import com.fluxtion.example.article.dataoriented1.Events.HardwareSaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.HardwareTaxRateEvent;
import com.fluxtion.example.article.dataoriented1.Events.TaxLiabilityNotificationThresholdEvent;
import com.fluxtion.example.article.dataoriented1.Events.TaxPaymentEvent;

public class TaxProcessorManual {

    private final TaxLiabilityThresholdMonitor thresholdNotifier;
    private final BookSaleHandler bookSaleHandler;
    private final FoodSaleHandler foodSaleHandler;
    private final HardwareSaleHandler hardwareSaleHandler;
    private final TaxLiabilityCalculator taxLiabilityCalculator;
    private final TaxLiabilityAlerter alerter;

    public TaxProcessorManual() {
        bookSaleHandler = new BookSaleHandler();
        foodSaleHandler = new FoodSaleHandler();
        hardwareSaleHandler = new HardwareSaleHandler();
        taxLiabilityCalculator = TaxLiabilityCalculator.builder()
                .bookSaleHandler(bookSaleHandler)
                .foodSaleHandler(foodSaleHandler)
                .hardwareSaleHandler(hardwareSaleHandler)
                .build();
        thresholdNotifier = new TaxLiabilityThresholdMonitor(taxLiabilityCalculator);
        alerter = new TaxLiabilityAlerter(thresholdNotifier);
    }

    public void onEvent(Object event) {
        switch (event) {
            case BookSaleEvent e -> {
                bookSaleHandler.handleSale(e);
                checkLiability();
            }
            case FoodSaleEvent e -> {
                foodSaleHandler.handleSale(e);
                checkLiability();
            }
            case HardwareSaleEvent e -> {
                hardwareSaleHandler.handleSale(e);
                checkLiability();
            }
            case BookTaxRateEvent e -> {
                bookSaleHandler.handleUpdatedTaxRate(e);
                checkLiability();
            }
            case FoodTaxRateEvent e -> {
                foodSaleHandler.handleUpdatedTaxRate(e);
                checkLiability();
            }
            case HardwareTaxRateEvent e -> {
                hardwareSaleHandler.handleUpdatedTaxRate(e);
                checkLiability();
            }
            case TaxLiabilityNotificationThresholdEvent e -> {
                if (thresholdNotifier.thresholdUpdated(e)) {
                    alerter.publishTaxLiabilityWarning();
                }
            }
            case TaxPaymentEvent e -> {
                if (taxLiabilityCalculator.taxPayment(e) && thresholdNotifier.isTaxLiabilityBreached()) {
                    alerter.publishTaxLiabilityWarning();
                }
            }
            default -> {
            }
        }
    }

    private void checkLiability() {
        if (taxLiabilityCalculator.hasTaxLiabilityChanged() && thresholdNotifier.isTaxLiabilityBreached()) {
            alerter.publishTaxLiabilityWarning();
        }
    }
}