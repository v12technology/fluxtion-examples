package com.fluxtion.example.article.dataoriented1.manual;

import com.fluxtion.runtime.annotations.OnTrigger;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@Builder
public class TotalTaxLiabilityCalculator {

    BookSaleHandler bookSaleHandler;
    FoodSaleHandler foodSaleHandler;
    HardwareSaleHandler hardwareSaleHandler;
    @NonFinal
    transient double taxLiability;

    @OnTrigger
    public boolean hasTaxLiabilityChanged() {
        taxLiability = bookSaleHandler.taxLiability()
                + foodSaleHandler.taxLiability()
                + hardwareSaleHandler.taxLiability();
        return true;
    }
}
