package com.fluxtion.example.cookbook.inject.suppliedinstance;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.EventProcessor;

public class GlobalSalesTaxCalculatorMain {

    public static void main(String[] args) {
        var globalTaxProcessor = Fluxtion.interpret(c -> c.addNode(new GlobalSalesTaxCalculator()));
        globalTaxProcessor.injectNamedInstance(() -> 0.1, TaxCalculator.class, "domesticTaxCalculator");
        globalTaxProcessor.injectNamedInstance(() -> 0.3, TaxCalculator.class, "foreignTaxCalculator");

        globalTaxProcessor.init();
        globalTaxProcessor.onEvent(new DomesticSale(100));
        globalTaxProcessor.onEvent(new ForeignSale(100));
        globalTaxProcessor.onEvent(new DomesticSale(100));

        //update tax rate
        System.out.print("""
                            update foreign tax rate to 20%
                            ---------------------------------------------
                            """);
        globalTaxProcessor.injectNamedInstance(() -> 0.2, TaxCalculator.class, "domesticTaxCalculator");
        globalTaxProcessor.onEvent(new ForeignSale(100));
    }
}
