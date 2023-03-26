package com.fluxtion.example.article.dataoriented1.manual;

import com.fluxtion.example.article.dataoriented1.Events.TaxLiabilityNotificationThresholdEvent;
import lombok.Data;

@Data
public class TaxLiabilityThresholdMonitor {
    private final TaxLiabilityCalculator taxLiabilityCalculator;
    private transient double threshold;

    public boolean thresholdUpdated(TaxLiabilityNotificationThresholdEvent thresholdEvent) {
        this.threshold = thresholdEvent.amount();
        return isTaxLiabilityBreached();
    }

    public boolean isTaxLiabilityBreached() {
        return taxLiabilityCalculator.getTaxLiability() > threshold;
    }

    public double taxLiability(){
        return taxLiabilityCalculator.getTaxLiability();
    }
}
