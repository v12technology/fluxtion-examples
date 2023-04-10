package com.fluxtion.example.article.dataoriented1;

import com.fluxtion.example.article.dataoriented1.Events.BookSaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.BookTaxRateEvent;
import com.fluxtion.example.article.dataoriented1.Events.FoodSaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.FoodTaxRateEvent;
import com.fluxtion.example.article.dataoriented1.Events.HardwareSaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.HardwareTaxRateEvent;
import com.fluxtion.example.article.dataoriented1.Events.TaxLiabilityNotificationThresholdEvent;
import com.fluxtion.example.article.dataoriented1.Events.TaxPaymentEvent;
import com.fluxtion.example.article.dataoriented1.fluxtion.generated.TaxProcessorFluxtion;
import com.fluxtion.example.article.dataoriented1.manual.TaxProcessorManual;

import java.util.List;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
        System.out.print("""
                Running TaxProcessorFluxtion 
                ----------------------------------
                """);
        TaxProcessorFluxtion taxProcessor = new TaxProcessorFluxtion();
        taxProcessor.init();
        runReplay(taxProcessor::onEvent);

        System.out.print("""
                                           
                Running TaxProcessorManual 
                ----------------------------------
                """);
        TaxProcessorManual taxProcessorManual = new TaxProcessorManual();
        runReplay(taxProcessorManual::onEvent);
    }

    private static void runReplay(Consumer<Object> streamProcessor) {
        List.of(
                new TaxLiabilityNotificationThresholdEvent(200),
                new BookTaxRateEvent(0.02),
                new FoodTaxRateEvent(0.15),
                new HardwareTaxRateEvent(0.2),
                new BookSaleEvent(150),
                new FoodSaleEvent(150),
                new HardwareSaleEvent(150),
                //breach threshold - warnings published
                new HardwareSaleEvent(1500),
                new HardwareSaleEvent(150),
                //pay some tax removes breach warnings
                new TaxPaymentEvent(300),
                new HardwareSaleEvent(150),
                //update tax rate for food will breach threshold
                new FoodSaleEvent(500),
                new FoodTaxRateEvent(0.2)
        ).forEach(streamProcessor::accept);
    }
}
