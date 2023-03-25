package com.fluxtion.example.article.dataoriented1.fluxtion;


public class TaxProcessor {

    private final TaxThresholdNotifier thresholdNotifier;
    private final BookSaleHandler bookSaleHandler;
    private final FoodSaleHandler foodSaleHandler;
    private final HardwareSaleHandler hardwareSaleHandler;
    private final TotalTaxLiabilityCalculator taxLiabilityCalculator;

    public TaxProcessor() {
        bookSaleHandler = new BookSaleHandler();
        foodSaleHandler = new FoodSaleHandler();
        hardwareSaleHandler = new HardwareSaleHandler();
        taxLiabilityCalculator = TotalTaxLiabilityCalculator.builder()
                .bookSaleHandler(bookSaleHandler)
                .foodSaleHandler(foodSaleHandler)
                .hardwareSaleHandler(hardwareSaleHandler)
                .build();
        thresholdNotifier = new TaxThresholdNotifier(taxLiabilityCalculator);
    }


}