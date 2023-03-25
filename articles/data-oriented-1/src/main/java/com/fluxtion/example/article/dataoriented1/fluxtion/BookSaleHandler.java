package com.fluxtion.example.article.dataoriented1.fluxtion;


import com.fluxtion.example.article.dataoriented1.Events.BookSaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.BookTaxRateEvent;

public class BookSaleHandler extends AbstractSaleHandler{

    public void handleSale(BookSaleEvent bookSaleEvent) {
        totalSales += bookSaleEvent.amount();
        calculateTaxLiability();
    }

    public void handleUpdatedTaxRate(BookTaxRateEvent bookTaxRateEvent) {
        currentTaxRate = bookTaxRateEvent.amount();
        calculateTaxLiability();
    }
}
