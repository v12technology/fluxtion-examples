package com.fluxtion.example.article.dataoriented1;

public interface Events {

    record SaleEvent(double amount) {
    }

    record BookSaleEvent(double amount) {
    }

    record FoodSaleEvent(double amount) {
    }

    record HardwareSaleEvent(double amount) {
    }

    record SalesTaxRateEvent(double amount) {
    }

    record BookTaxRateEvent(double amount) {
    }

    record FoodTaxRateEvent(double amount) {
    }

    record HardwareTaxRateEvent(double amount) {
    }

    record TaxLiabilityNotificationThresholdEvent(double amount) {
    }

    record TaxPaymentEvent(double amount) {
    }
}
