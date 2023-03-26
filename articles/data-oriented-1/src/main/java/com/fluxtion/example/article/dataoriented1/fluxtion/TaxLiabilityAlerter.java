package com.fluxtion.example.article.dataoriented1.fluxtion;

import com.fluxtion.runtime.annotations.OnTrigger;
import lombok.Value;

@Value
public class TaxLiabilityAlerter {

    TaxLiabilityThresholdMonitor taxLiabilityThresholdMonitor;


    @OnTrigger
    public boolean publishTaxLiabilityWarning(){
        System.out.println("WARNING tax outstanding:" + taxLiabilityThresholdMonitor.taxLiability());
        return true;
    }
}
