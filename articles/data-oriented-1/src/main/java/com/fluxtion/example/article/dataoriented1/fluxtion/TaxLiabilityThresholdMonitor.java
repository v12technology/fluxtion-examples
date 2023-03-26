package com.fluxtion.example.article.dataoriented1.fluxtion;

import com.fluxtion.example.article.dataoriented1.Events.TaxLiabilityNotificationThresholdEvent;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
import lombok.Data;

@Data
public class TaxLiabilityThresholdMonitor {
    final TaxLiabilityCalculator taxLiabilityCalculator;
    transient double threshold;

    @OnEventHandler
    public boolean thresholdUpdated(TaxLiabilityNotificationThresholdEvent thresholdEvent) {
        this.threshold = thresholdEvent.amount();
        return isTaxLiabilityBreached();
    }

    @OnTrigger
    public boolean isTaxLiabilityBreached() {
        return taxLiabilityCalculator.getTaxLiability() > threshold;
    }

    public double taxLiability(){
        return taxLiabilityCalculator.getTaxLiability();
    }
}
