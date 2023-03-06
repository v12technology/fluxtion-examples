package com.fluxtion.example.cookbook.inject.suppliedinstance;

import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.node.InstanceSupplier;

public class GlobalSalesTaxCalculator {

    @Inject(instanceName = "foreignTaxCalculator")
    public InstanceSupplier<TaxCalculator> taxCalculatorForeign;

    @Inject(instanceName = "domesticTaxCalculator")
    public InstanceSupplier<TaxCalculator> taxCalculatorDomestic;

    private int foreignUnitsSoldTotal;
    private int domesticUnitSoldTotal;
    private double globalTax;

    @OnEventHandler
    public boolean overseasSales(ForeignSale foreignSale) {
        foreignUnitsSoldTotal += foreignSale.amount();

        dumpTax();
        return true;
    }

    @OnEventHandler
    public boolean domesticSale(DomesticSale domesticSale) {
        domesticUnitSoldTotal += domesticSale.amount();
        globalTax += domesticUnitSoldTotal * taxCalculatorDomestic.get().taxRate();
        dumpTax();
        return true;
    }

    private void dumpTax() {
        globalTax = foreignUnitsSoldTotal * taxCalculatorForeign.get().taxRate()
                + domesticUnitSoldTotal * taxCalculatorDomestic.get().taxRate();
        System.out.printf("""
                [domestic sold: %d] [tax rate: %f] [Tax due: %f]
                [foreign  sold: %d] [tax rate: %f] [Tax due: %f]
                Total global tax due : %f
                                
                """.formatted(
                domesticUnitSoldTotal,
                taxCalculatorDomestic.get().taxRate(),
                domesticUnitSoldTotal * taxCalculatorDomestic.get().taxRate(),
                foreignUnitsSoldTotal,
                taxCalculatorForeign.get().taxRate(),
                foreignUnitsSoldTotal * taxCalculatorForeign.get().taxRate(),
                globalTax));
    }
}
