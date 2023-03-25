package com.fluxtion.example.article.dataoriented1;

public interface Events {

    record BookSaleEvent(double amount) {
    }

    record FoodSaleEvent(double amount) {
    }

    record HardwareSaleEvent(double amount) {
    }

    record BookTaxRateEvent(double amount) {
    }

    record FoodTaxRateEvent(double amount) {
    }

    record HardwareTaxRateEvent(double amount) {
    }

    record TaxLiabilityNotificationThresholdEvent(double amount) {
    }
}
