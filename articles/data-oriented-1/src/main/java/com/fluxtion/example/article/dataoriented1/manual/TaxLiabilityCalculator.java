package com.fluxtion.example.article.dataoriented1.manual;

import com.fluxtion.example.article.dataoriented1.Events.TaxPaymentEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class TaxLiabilityCalculator {

    private final BookSaleHandler bookSaleHandler;
    private final FoodSaleHandler foodSaleHandler;
    private final HardwareSaleHandler hardwareSaleHandler;
    private transient double taxLiability;
    private transient double taxPaymentToDate;

    public boolean taxPayment(TaxPaymentEvent taxPaymentEvent) {
        taxPaymentToDate += taxPaymentEvent.amount();
        boolean hasTaxLiabilityChanged = hasTaxLiabilityChanged();
        System.out.println("TAX PAID:" + taxPaymentEvent.amount() + " current liability:" + taxLiability);
        return hasTaxLiabilityChanged;
    }

    public boolean hasTaxLiabilityChanged() {
        double taxLiabilityOld = taxLiability;
        taxLiability = bookSaleHandler.taxLiability()
                + foodSaleHandler.taxLiability()
                + hardwareSaleHandler.taxLiability()
                - taxPaymentToDate;
        return taxLiability != taxLiabilityOld;
    }
}
