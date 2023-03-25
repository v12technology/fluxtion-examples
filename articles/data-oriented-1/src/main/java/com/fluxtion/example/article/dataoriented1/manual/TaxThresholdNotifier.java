package com.fluxtion.example.article.dataoriented1.manual;

import com.fluxtion.example.article.dataoriented1.Events.TaxLiabilityNotificationThresholdEvent;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
import lombok.Data;
@Data
public class TaxThresholdNotifier {
    final TotalTaxLiabilityCalculator taxLiabilityCalculator;
    transient double threshold;

    @OnEventHandler
    public boolean thresholdUpdated(TaxLiabilityNotificationThresholdEvent thresholdEvent){
        this.threshold = thresholdEvent.amount();
        return notifyOfBreach();
    }

    @OnTrigger
    public boolean notifyOfBreach(){
        return taxLiabilityCalculator.getTaxLiability() > threshold;
    }
}
