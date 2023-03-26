package com.fluxtion.example.article.dataoriented1.fluxtion;

import com.fluxtion.example.article.dataoriented1.Events.BookSaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.BookTaxRateEvent;
import com.fluxtion.runtime.annotations.OnEventHandler;

public class BookSaleHandler extends AbstractSaleHandler {

    @OnEventHandler
    public boolean handleSale(BookSaleEvent bookSaleEvent) {
        totalSales += bookSaleEvent.amount();
        calculateTaxLiability();
        return true;
    }

    @OnEventHandler
    public boolean handleUpdatedTaxRate(BookTaxRateEvent bookTaxRateEvent) {
        currentTaxRate = bookTaxRateEvent.amount();
        calculateTaxLiability();
        return true;
    }
}
