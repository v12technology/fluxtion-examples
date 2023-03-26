package com.fluxtion.example.article.dataoriented1.manual;

import lombok.Value;

@Value
    public class TaxLiabilityAlerter {

    TaxLiabilityThresholdMonitor taxLiabilityThresholdMonitor;


    public boolean publishTaxLiabilityWarning(){
        System.out.println("WARNING tax outstanding:" + taxLiabilityThresholdMonitor.taxLiability());
        return true;
    }
}
