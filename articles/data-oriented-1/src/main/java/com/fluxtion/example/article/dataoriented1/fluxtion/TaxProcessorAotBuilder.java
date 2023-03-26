package com.fluxtion.example.article.dataoriented1.fluxtion;

import com.fluxtion.compiler.EventProcessorConfig.DISPATCH_STRATEGY;
import com.fluxtion.compiler.Fluxtion;

public class TaxProcessorAotBuilder {

    public static void main(String[] args) {
        Fluxtion.compileAot(
                c -> {
                    c.addNode(
                            new TaxLiabilityAlerter(
                                    new TaxLiabilityThresholdMonitor(TaxLiabilityCalculator.builder()
                                            .bookSaleHandler(new BookSaleHandler())
                                            .foodSaleHandler(new FoodSaleHandler())
                                            .hardwareSaleHandler(new HardwareSaleHandler())
                                            .build())));
                    c.setDispatchStrategy(DISPATCH_STRATEGY.PATTERN_MATCH);
                    c.javaTargetRelease("17");
                },
                "com.fluxtion.example.article.dataoriented1.fluxtion.generated", "TaxProcessorFluxtion"
        );
    }


}