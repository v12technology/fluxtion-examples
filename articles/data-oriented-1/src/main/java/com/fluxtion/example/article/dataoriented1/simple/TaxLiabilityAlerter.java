package com.fluxtion.example.article.dataoriented1.simple;

import com.fluxtion.example.article.dataoriented1.Events.TaxLiabilityNotificationThresholdEvent;
import lombok.Data;

@Data
public class TaxLiabilityAlerter {

    private final SaleHandler saleHandler;
    private transient double threshold;

    public void thresholdUpdated(TaxLiabilityNotificationThresholdEvent thresholdEvent) {
        this.threshold = thresholdEvent.amount();
        isTaxLiabilityBreached();
    }

    public void isTaxLiabilityBreached() {
        if (saleHandler.taxLiability() > threshold) {
            System.out.println("WARNING tax outstanding:" + saleHandler.taxLiability());
        }
    }
}
