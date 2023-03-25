package com.fluxtion.example.article.dataoriented1.fluxtion;

import com.fluxtion.example.article.dataoriented1.Events.TaxLiabilityNotificationThresholdEvent;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
import lombok.Data;

@Data
public class TaxThresholdNotifier {
    private final TotalTaxLiabilityCalculator taxLiabilityCalculator;
    transient double threshold;

    public boolean thresholdUpdated(TaxLiabilityNotificationThresholdEvent thresholdEvent) {
        this.threshold = thresholdEvent.amount();
        return notifyOfBreach();
    }

    public boolean notifyOfBreach() {
        return taxLiabilityCalculator.getTaxLiability() > threshold;
    }
}
